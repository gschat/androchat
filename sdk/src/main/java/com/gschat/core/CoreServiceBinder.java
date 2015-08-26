package com.gschat.core;


import android.content.Context;
import android.os.RemoteException;

import com.github.gschat.gsim.IMClient;
import com.github.gschat.gsim.IMGatewayRPC;
import com.github.gschat.gsim.IMServerRPC;
import com.github.gschat.gsim.Message;
import com.github.gsdocker.gsrpc.Service;
import com.github.gsdocker.gsrpc.State;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gschat.database.Client;
import com.gschat.ipc.IClient;
import com.gschat.ipc.IMessagePuller;
import com.gschat.ipc.IPCChatRoomEvent;
import com.gschat.ipc.IPCMessage;
import com.gschat.ipc.IPCMessageEvent;
import com.gschat.ipc.IPCNetEvent;
import com.gschat.ipc.IPCUser;
import com.gschat.ipc.IPCUserEvent;
import com.gschat.ipc.IService;
import com.gschat.sdk.GSChat;
import com.gschat.sdk.GSChatListener;
import com.gschat.sdk.GSChatRoom;
import com.gschat.sdk.GSChatRoomListener;
import com.gschat.sdk.GSDirect;
import com.gschat.sdk.GSError;
import com.gschat.sdk.GSMessageState;
import com.gschat.sdk.GSUserState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

final class CoreServiceBinder extends IService.Stub implements IMClient,GSChatRoomListener {

    /**
     * CoreServiceBinder logger
     */
    private static final Logger logger = LoggerFactory.getLogger("CoreServiceBinder");

    /**
     * core service binder belongs to
     */
    private final CoreService coreService;

    /**
     * binder service client
     */
    private final Client client;

    /**
     * im channel
     */
    private IMChannel channel;

    /**
     * gateway service
     */
    private final IMGateway imGateway;


    /**
     * im server
     */
    private final IMServer imServer;


    /**
     * user state
     */
    private GSUserState userState = GSUserState.Logoff;

    /**
     * bind  database
     */
    private final CoreServiceBinderDataBase binderDataBase;

    /**
     * client service
     */
    private IClient clientService;

    /**
     * executor
     */
    private final Executor executor = Executors.newFixedThreadPool(1);

    /**
     * create core service binder
     *
     * @param coreService core service
     * @param client client
     */
    public CoreServiceBinder(CoreService coreService, Client client) {

        this.binderDataBase = new CoreServiceBinderDataBase(coreService.getApplicationContext(),client.getUrl());

        this.coreService = coreService;

        this.client = new Client(client);

        this.channel = new IMChannel(this);

        this.channel.connect();

        this.imGateway = new IMGateway(this, new IMGatewayRPC(this.channel, Service.GW.getValue()));

        this.imServer = new IMServer(this, new IMServerRPC(this.channel, Service.GS.getValue()));

        if (!emptyUser()) {

            this.binderDataBase.openUserDataBase(this.client.getUser());

            this.autoLogin();
        }
    }

    private boolean emptyUser() {
        return this.client.getUser() == null || "".equals(this.client.getUser());
    }

    /**
     * close binder
     */
    public void close() {
        this.channel.close();
    }

    public String getClientURI() {
        return client.getUrl();
    }

    public void handleRPCTimeout() {
        this.channel.timeoutHandle();
    }

    public void reconnect(boolean force) {

        if (force) {
            this.channel.disconnect();
        }

        this.channel.connect();
    }

    public void ping() {

        this.channel.ping();
    }


    @Override
    public void Heartbeat(int arg0) throws Exception {
        synchronized (this) {
            if (this.userState != GSUserState.Login) {
                logger.warn("drop heartbeat -- user state({}) error", this.userState);
                return;
            }

            int receivedID = binderDataBase.getReceivedSeqID();

            logger.debug("received heartbeat {} local {}", arg0, receivedID);

            if (receivedID < arg0) {

                imServer.pollMessage(this.client.getToken(), receivedID);
            }
        }
    }

    @Override
    public void KickOff() throws Exception {
        synchronized (this) {

            this.userState = GSUserState.Logoff;

            onUserStateChanged(GSError.KICKOFF);

            if (this.client.getUser() != null) {
                this.client.setUser(null);

                this.coreService.updateClient(client);
            }

            this.binderDataBase.closeUserDataBase();
        }
    }

    @Override
    public void RecvMessage(Message message) throws Exception {

        logger.debug("recv message :{}", message.getContent());

        synchronized (this) {
            if(this.userState != GSUserState.Login) {
                //TODO: may lost message
                return;
            }


            Gson gson = new Gson();

            String content = new String(message.getContent(), "UTF8");

            JsonObject json = (JsonObject)new JsonParser().parse(content);

            JsonArray bodies = json.getAsJsonArray("bodies");

            JsonObject object = (JsonObject) bodies.get(0);

            int messageType = object.get("messageBodyType").getAsInt();

            String id = json.get("id").toString();

            IPCMessage ipcMessage = new IPCMessage(id,message.getType(),message.getSource(),message.getTarget());

            ipcMessage.setContent(object.toString(),messageType);

            ipcMessage.setDirect(GSDirect.From);

            ipcMessage.setSeqID(message.getSeqID());

            ipcMessage.setState(GSMessageState.LocalApply);

            binderDataBase.saveMessage(ipcMessage,this);

            onMessageStateChanged(ipcMessage, GSError.SUCCESS);

            logger.debug("recv message :{} -- success", ipcMessage);
        }
    }


    @Override
    public void RecvPushMessage(Message arg0) throws Exception {

    }

    @Override
    public void register(IClient client) throws RemoteException {
        this.clientService = client;

        this.clientService.asBinder().linkToDeath(new DeathRecipient() {
            @Override
            public void binderDied() {
                synchronized (CoreServiceBinder.this) {
                    logger.debug("client died");
                    CoreServiceBinder.this.clientService = null;
                }
            }
        }, 0);

        synchronized (this) {
            onUserStateChanged(GSError.SUCCESS);
        }
    }

    @Override
    public void login(IPCUser user) throws RemoteException {

        if ("".equals(user.getUserName())) {
            onUserStateChanged(user.getUserName(), GSUserState.Logoff,GSError.UNKNOWN_ERROR);

            return;
        }

        synchronized (this) {

            if (this.userState != GSUserState.Logoff) {

                if (this.userState == GSUserState.Login && user.getUserName().equals(this.client.getUser())) {
                    onUserStateChanged(user.getUserName(), GSUserState.Login,GSError.SUCCESS);

                    return;
                }

                logger.warn("other login({}) processing {}",this.client.getUser(),this.userState);

                onUserStateChanged(user.getUserName(), GSUserState.Logoff, GSError.OTHER_USER_LOGIN);

                return;
            }

            logger.debug("login with user({})",user.getUserName());

            this.client.setUser(user.getUserName());

            this.userState = GSUserState.LoginProcessing;

            onUserStateChanged(GSError.SUCCESS);

            try {

                this.imGateway.Login(this.client.getUser());

                coreService.updateClient(this.client);

                this.binderDataBase.openUserDataBase(client.getUser());

            } catch (Exception e) {

                this.userState = GSUserState.Logoff;

                onUserStateChanged(GSError.UNKNOWN_ERROR);

                logger.error("login catch exception", e);
            }
        }
    }

    private boolean emptyToken() {
        return this.client.getToken() == null || this.client.getToken().length == 0;
    }

    @Override
    public void autoLogin() {
        synchronized (this) {

            if (this.userState != GSUserState.Logoff) {
                logger.warn("skip auto login -- user state({}) error", this.userState);
                return;
            }

            if (this.emptyUser()) {

                logger.warn("skip auto login -- can't find user info", this.userState);

                return;
            }

            this.userState = GSUserState.LoginProcessing;

            onUserStateChanged(GSError.SUCCESS);

            try {

                if (emptyToken()) {

                    logger.debug("start login({})",this.client.getUser());

                    imGateway.Login(this.client.getUser());

                    logger.debug("start login({}) -- success", this.client.getUser());

                } else {

                    logger.debug("start fastlogin({})",this.client.getUser());

                    imGateway.fastLogin(this.client.getUser(), this.client.getToken());

                    logger.debug("start fastlogin({}) -- success",this.client.getUser());
                }

                this.binderDataBase.openUserDataBase(client.getUser());

            } catch (Exception e) {

                logger.error("fast login with token {} error", Arrays.toString(this.client.getToken()), e);

                this.userState = GSUserState.Logoff;

                onUserStateChanged(GSError.UNKNOWN_ERROR);
            }

        }
    }

    private void onUserStateChanged(final GSError error) {

        final IPCUserEvent userEvent = new IPCUserEvent(this.userState, this.client.getUser(), error);

        if (this.clientService != null) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        logger.debug("notify user({}) state changed({}) error ({})", userEvent.getUserName(), userEvent.getAction(), error);
                        clientService.onUserEvent(userEvent);
                        logger.debug("notify user({}) state changed({}) error ({}) -- success", userEvent.getUserName(), userEvent.getAction(), error);
                    } catch (RemoteException e) {
                        logger.error("call UserStateListener#onStateCHanged method error", e);
                    }
                }
            });
        }
    }

    private void onUserStateChanged(final String user,GSUserState userState,GSError error) {

        final IPCUserEvent userEvent = new IPCUserEvent(userState, user, error);

        if (this.clientService != null) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        logger.debug("notify user({}) state changed({}) error ({})", userEvent.getUserName(), userEvent.getAction(), userEvent.getErrorCode());
                        clientService.onUserEvent(userEvent);
                        logger.debug("notify user({}) state changed({}) error ({}) -- success", userEvent.getUserName(), userEvent.getAction(), userEvent.getErrorCode());
                    } catch (RemoteException e) {
                        logger.error("call UserStateListener#onStateCHanged method error", e);
                    }
                }
            });
        }
    }


    private void onMessageStateChanged(IPCMessage ipcMessage, GSError error) {

        final IPCMessageEvent event = new IPCMessageEvent(ipcMessage,ipcMessage.getState(),error);


        if (this.clientService != null) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        clientService.onMessageEvent(event);
                    } catch (RemoteException e) {
                        logger.error("send message event error", e);
                    }
                }
            });
        }
    }

    @Override
    public void logoff() throws RemoteException {
        synchronized (this) {

            String username = this.client.getUser();

            if(username == null) {
                return;
            }

            if(userState == GSUserState.Logoff) {
                return;
            }

            logger.debug("user {} logoff ...", username);


            this.userState = GSUserState.LogoffProcessing;

            onUserStateChanged(GSError.SUCCESS);

            try {
                imGateway.Logoff(this.client.getToken());

            } catch (Exception e) {

                this.userState = GSUserState.Login;

                onUserStateChanged(GSError.UNKNOWN_ERROR);
            }
        }
    }

    @Override
    public List<GSChatRoom> getChatRooms() throws RemoteException {
        synchronized (this) {
            logger.debug("query session");

            if (this.userState != GSUserState.Login) {

                logger.debug("query session error");

                return new ArrayList<>();
            }

            List<GSChatRoom> chatRooms = null;
            try {

                chatRooms = binderDataBase.getChatRooms();

                logger.debug("query session -- success");

            } catch (Exception e) {
                logger.error("get chat rooms error", e);
            }

            return chatRooms;
        }
    }

    @Override
    public void sendMessage(final IPCMessage ipcMessage) throws RemoteException {

        synchronized (this) {
            if (this.userState != GSUserState.Login) {

                ipcMessage.setState(GSMessageState.SendFailed);

                onMessageStateChanged(ipcMessage,GSError.LOGIN_FIRST);

                return;
            }

            ipcMessage.setSource(this.client.getUser());

            try {

                Gson gson = new Gson();

                JsonObject json = (JsonObject)gson.toJsonTree(ipcMessage);

                JsonArray bodies = new JsonArray();

                bodies.add(new JsonParser().parse(ipcMessage.getContent()));

                json.add("bodies", bodies);

                final String content = json.toString();

                logger.debug("send message :{}", content);

                ipcMessage.setState(GSMessageState.LocalApply);

                this.binderDataBase.saveMessage(ipcMessage,this);

                onMessageStateChanged(ipcMessage,GSError.SUCCESS);

                // create rpc message package
                Message message = new Message();

                message.setSeqID(ipcMessage.getSeqID());

                message.setSource(ipcMessage.getSource());

                message.setTarget(ipcMessage.getTarget());

                message.setType(ipcMessage.getType());

                message.setContent(content.getBytes("UTF-8"));

                imServer.sendMessage(ipcMessage.getId(), message);

                ipcMessage.setState(GSMessageState.Sending);

                this.binderDataBase.updateMessageState(ipcMessage.getId(),ipcMessage.getState());

                onMessageStateChanged(ipcMessage, GSError.SUCCESS);

            } catch (Exception e) {

                logger.error("send message({}) error",ipcMessage.getId(), e);

                ipcMessage.setState(GSMessageState.SendFailed);

                try {
                    this.binderDataBase.updateMessageState(ipcMessage.getId(), ipcMessage.getState());
                } catch (Exception e1) {
                    logger.error("save message({}) state({}) error",ipcMessage.getId(),ipcMessage.getState(), e);
                }

                onMessageStateChanged(ipcMessage, GSError.UNKNOWN_ERROR);

            }
        }
    }

    @Override
    public void getMessages(final IMessagePuller puller, final String chatRoom, final int offset, final int length, final boolean desc) throws RemoteException {
        synchronized (this) {

            if(this.userState != GSUserState.Login) {
                return;
            }

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        puller.onSuccess(CoreServiceBinder.this.binderDataBase.getMessages(chatRoom, offset, length, desc));
                    } catch (Exception e) {
                        logger.error("pull messages error",e);
                    }
                }
            });
        }
    }


    public Context getContext() {
        return this.coreService.getApplicationContext();
    }


    public void onLoginComplete(GSError error, byte[] token) {

        synchronized (this) {

            if(error == GSError.SUCCESS) {
                this.userState = GSUserState.Login;

                this.client.setToken(token);
            } else {
                this.client.setToken(null);
                this.userState = GSUserState.Logoff;
            }

            onUserStateChanged(error);

            this.coreService.updateClient(client);
        }
    }

    public void onLogoffComplete(GSError errorCode) {
        synchronized (this) {

            if(errorCode == GSError.SUCCESS) {
                this.client.setUser(null);
                this.userState = GSUserState.Logoff;
            }

            onUserStateChanged(errorCode);
        }
    }

    public void onSendMessageComplete(GSError errorCode,final String id) {
        synchronized (this) {

            try {

                GSMessageState state = GSMessageState.RemoteApply;

                if(errorCode != GSError.SUCCESS) {
                    state = GSMessageState.RemoteReject;
                }

                IPCMessage ipcMessage = this.binderDataBase.updateMessageState(id,state);

                onMessageStateChanged(ipcMessage, errorCode);

            } catch (Exception e) {
                logger.error("update message state error",e);
            }
        }
    }

    public void onNetStateChanged(final IPCNetEvent event) {

        synchronized (this){

            logger.debug("net event {} client {}",event.getState(),this.clientService);

            if (event.getState() == State.Connected) {
                this.executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        autoLogin();
                    }
                });
            }

            if(event.getState() == State.Disconnected) {
                this.userState = GSUserState.Logoff;
            }

            if(this.clientService != null){

                if(event.getState() == State.Disconnected) {
                    onUserStateChanged(GSError.SUCCESS);
                }

                this.executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            clientService.onNetEvent(event);
                        } catch (RemoteException e) {
                            logger.error("send net event error",e);
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onUpdate(final GSChatRoom chatRoom) {
        synchronized (this){
            if(this.clientService != null){

                this.executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            clientService.onChatRoomEvent(new IPCChatRoomEvent(IPCChatRoomEvent.Action.Update,chatRoom));
                        } catch (RemoteException e) {
                            logger.error("send net event error",e);
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onRemove(final GSChatRoom chatRoom) {
        synchronized (this){
            if(this.clientService != null){

                this.executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            clientService.onChatRoomEvent(new IPCChatRoomEvent(IPCChatRoomEvent.Action.Remove,chatRoom));
                        } catch (RemoteException e) {
                            logger.error("send net event error",e);
                        }
                    }
                });
            }
        }
    }
}
