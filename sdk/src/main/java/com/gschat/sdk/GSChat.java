package com.gschat.sdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;

import com.github.gschat.gsim.MessageType;
import com.gschat.cached.GSCachedService;
import com.gschat.cached.GSCloud;
import com.gschat.core.CoreService;
import com.gschat.events.Event;
import com.gschat.events.EventListener;
import com.gschat.events.Slot;
import com.gschat.ipc.IClient;
import com.gschat.ipc.IMessagePuller;
import com.gschat.ipc.IPCChatRoomEvent;
import com.gschat.ipc.IPCMessage;
import com.gschat.ipc.IPCMessageEvent;
import com.gschat.ipc.IPCNetEvent;
import com.gschat.ipc.IPCUser;
import com.gschat.ipc.IPCUserEvent;
import com.gschat.ipc.IService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public final class GSChat implements ServiceConnection {

    /**
     * GSChat state
     */
    public enum State {
        Closed, Disconnected, Connecting, Connected
    }

    /**
     * logger
     */
    private final static Logger logger = LoggerFactory.getLogger("GSChat");

    /**
     * android context
     */
    private final Context context;

    /**
     * core service client id
     */
    private final String uri;
    private final GSCachedService cachedService;

    /**
     * service proxy
     */
    private IService service;

    /**
     * service state
     */
    private volatile State state = State.Disconnected;

    /**
     * async callback execute handler,on android all ui operation must execute in main thread
     */
    private final Handler handler;

    /**
     * service event
     */
    private final Event<State> serviceEvent;

    /**
     * net event
     */
    private final Event<IPCNetEvent> netEvent;

    /**
     * user state event
     */
    private final Event<IPCUserEvent> userEvent;

    /**
     * message event
     */
    private final Event<IPCMessageEvent> messageEvent;


    /**
     * chat room event
     */
    private final Event<IPCChatRoomEvent> chatRoomEvent;

    /**
     * locker
     */
    private final ReentrantLock lock = new ReentrantLock();


    /**
     * async call executor
     */
    private final Executor executor = Executors.newFixedThreadPool(1);

    /**
     * last user event
     */
    private IPCUserEvent lastUserEvent;


    /**
     * create new gschat instance
     *
     * @param context android context
     * @param uri     gschat service client uri
     */
    public GSChat(Context context, String uri,GSCloud gsCloud) {
        this.context = context;

        this.uri = uri;

        this.cachedService = new GSCachedService(uri,gsCloud,executor);

        this.handler = new Handler();

        serviceEvent = new Event<State>(new Executor() {
            @Override
            public void execute(@NonNull Runnable command) {
                GSChat.this.handler.post(command);
            }
        });

        netEvent = new Event<IPCNetEvent>(new Executor() {
            @Override
            public void execute(@NonNull Runnable command) {
                GSChat.this.handler.post(command);
            }
        });

        userEvent = new Event<>(new Executor() {
            @Override
            public void execute(@NonNull Runnable command) {
                GSChat.this.handler.post(command);
            }
        });

        messageEvent = new Event<>(new Executor() {
            @Override
            public void execute(@NonNull Runnable command) {
                GSChat.this.handler.post(command);
            }
        });

        chatRoomEvent = new Event<>(new Executor() {
            @Override
            public void execute(@NonNull Runnable command) {
                GSChat.this.handler.post(command);
            }
        });
    }


    public void close() {
        synchronized (lock) {

            context.unbindService(this);

            this.state = State.Closed;
        }
    }

    public void connectService() throws Exception {
        synchronized (lock) {

            if (state == GSChat.State.Closed) {
                throw new GSException("gschat had closed",GSError.CORE_SERVICE_LOST);
            }

            if (state != GSChat.State.Disconnected) {
                return;
            }

            Intent intent = new Intent(context, CoreService.class);

            intent.putExtra("clientURI", this.uri);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if (context.startService(intent) == null) {
                throw new GSException(String.format("unknown android service :%s", CoreService.class),GSError.CORE_SERVICE_LOST);
            }

            if (!context.bindService(intent, this, 0)) {
                throw new GSException(String.format("unknown android service :%s", CoreService.class),GSError.CORE_SERVICE_LOST);
            }

            this.state = GSChat.State.Connecting;

            this.serviceEvent.raise(GSChat.State.Connecting);
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        synchronized (lock) {
            logger.debug("gschat connected");

            IService proxy = IService.Stub.asInterface(service);

            try {
                proxy.register(new IClient.Stub(){

                    @Override
                    public void onMessageEvent(IPCMessageEvent event) throws RemoteException {
                        messageEvent.raise(event);
                    }

                    @Override
                    public void onChatRoomEvent(IPCChatRoomEvent event) throws RemoteException {
                        chatRoomEvent.raise(event);
                    }

                    @Override
                    public void onNetEvent(IPCNetEvent event) throws RemoteException {
                        netEvent.raise(event);
                    }

                    @Override
                    public void onUserEvent(IPCUserEvent event) throws RemoteException {

                        synchronized (lock) {
                            GSChat.this.lastUserEvent = event;
                        }

                        userEvent.raise(event);
                    }
                });

                this.service = proxy;

                this.state = GSChat.State.Connected;

                this.serviceEvent.raise(GSChat.State.Connected);

            } catch (RemoteException e) {
                logger.error("register client error",e);
            }
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

        logger.info("gschat disconnected");

        this.service = null;

        this.state = GSChat.State.Disconnected;

        this.serviceEvent.raise(GSChat.State.Disconnected);
    }


    private void callCheck() throws Exception {
        if (this.state != GSChat.State.Connected) {

            if (this.state == State.Disconnected) {

            }

            throw new GSException(GSError.CORE_SERVICE_LOST);
        }
    }

    /**
     * gschat login action
     * @param userName user name
     * @param properties login properties
     * @throws Exception throws RemoteException or GSException
     */
    public void login(String userName,Map<String,String> properties) throws Exception {
        synchronized (lock){
            callCheck();

            IPCUser user = new IPCUser(userName);

            if(properties != null) {
                user.getProperties().putAll(properties);
            }

            this.service.login(user);
        }
    }

    public void login(String userName) throws Exception {
        login(userName,null);
    }

    /**
     * logoff from gschat service
     * @throws Exception
     */
    public void logoff() throws Exception {
        synchronized (lock){
            callCheck();

            this.service.logoff();
        }
    }

    /**
     * auto login
     * @throws Exception
     */
    public void autoLogin() throws Exception {
        synchronized (lock){
            callCheck();

            this.service.autoLogin();
        }
    }

    /**
     * send message
     * @param message ipc message
     */
    public void sendMessage(final GSMessage message) throws Exception {

        logger.debug("send message ...");

        synchronized (lock){
            callCheck();

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    IPCMessage ipcMessage = new IPCMessage(message.getId(), message.getType(), message.getSource(), message.getTarget());

                    ipcMessage.setDirect(GSDirect.To);

                    GSMessageBody body = message.getBody();

                    ipcMessage.setContent(body.toJson(), body.getType());

                    try {
                        GSChat.this.service.sendMessage(ipcMessage);

                        logger.debug("send message -- success");

                    } catch (RemoteException e) {
                        logger.error("send message -- failed", e);
                    }


                }
            });


        }
    }


    /**
     * get chat room list
     * @return chatroom list
     * @throws Exception throw GSException if not connected to gschat service
     */
    public List<GSChatRoom> getChatRoomList() throws Exception {
        synchronized (lock){
            callCheck();

            return this.service.getChatRooms();
        }
    }


    public Slot addGSChatListener(final GSChatListener listener) {
        synchronized (lock) {

            listener.onStateChanged(this.state);

            return this.serviceEvent.connect(new EventListener<State>() {
                @Override
                public void call(State arg) {
                    listener.onStateChanged(state);
                }
            });
        }
    }

    public Slot addGSChatRoomListener(final GSChatRoomListener listener) {
        synchronized (lock) {

            return this.chatRoomEvent.connect(new EventListener<IPCChatRoomEvent>() {
                @Override
                public void call(IPCChatRoomEvent arg) {
                    if (arg.getAction() == IPCChatRoomEvent.Action.Update) {
                        listener.onUpdate(arg.getChatRoom());
                    } else {
                        listener.onRemove(arg.getChatRoom());
                    }
                }
            });
        }
    }

    public Slot addUserListener(final GSUserListener listener) {
        synchronized (lock) {


            if(lastUserEvent != null) {
                listener.onStateChanged(lastUserEvent.getUserName(),lastUserEvent.getAction(),lastUserEvent.getErrorCode());
            } else {
                listener.onStateChanged("",GSUserState.Logoff,GSError.SUCCESS);
            }

            return this.userEvent.connect(new EventListener<IPCUserEvent>() {
                @Override
                public void call(IPCUserEvent arg) {
                    listener.onStateChanged(arg.getUserName(),arg.getAction(),arg.getErrorCode());
                }
            });
        }
    }

    public Slot addMessageListener(final GSMessageListener listener,final String chatRoom) {
        synchronized (lock){
            return this.messageEvent.connect(new EventListener<IPCMessageEvent>() {
                @Override
                public void call(IPCMessageEvent arg) {

                    GSMessage message = toGSMessage(arg.getMessage());

                    if(message != null) {

                        if(message.getDirect() == GSDirect.From) {
                            if(message.getType() == MessageType.Multi) {
                                if(!message.getTarget().equals(chatRoom)){
                                    return;
                                }
                            } else {
                                if(!message.getSource().equals(chatRoom)){
                                    return;
                                }
                            }
                        } else {
                            if(!message.getTarget().equals(chatRoom)){
                                return ;
                            }
                        }

                        message.setErrorCode(arg.getErrorCode());

                        listener.onStateChanged(message);
                    }
                }
            });
        }
    }

    private GSMessage toGSMessage(IPCMessage ipcMessage) {
        GSMessageBody messageBody = null;

        logger.debug("message {} state changed  {}",ipcMessage,ipcMessage.getState());

        switch (ipcMessage.getContentType()) {
            case Text:
                messageBody = GSText.fromJson(ipcMessage.getContent());
                break;
            case Image:
                messageBody = GSImage.fromJson(this,ipcMessage.getContent());
                break;
            default:
                logger.warn("unsupported message type {}",ipcMessage.getContentType());
                return null;
        }

        GSMessage message = new GSMessage(messageBody);

        message.setDirect(ipcMessage.getDirect());

        message.setSource(ipcMessage.getSource());

        message.setTarget(ipcMessage.getTarget());

        message.setId(ipcMessage.getId());

        message.setType(ipcMessage.getType());

        message.setState(ipcMessage.getState());

        return message;
    }

    public void pullMessages(final GSMessagePuller puller,String chatRoom,int offset, int length,boolean desc) throws Exception {

        synchronized (lock) {
            callCheck();

            service.getMessages(new IMessagePuller.Stub() {
                @Override
                public void onSuccess(List<IPCMessage> messages) throws RemoteException {

                    final ArrayList<GSMessage> gsMessages = new ArrayList<>();

                    Collections.reverse(messages);

                    for(IPCMessage ipcMessage : messages) {

                        GSMessage message = toGSMessage(ipcMessage);

                        if (message != null) {
                            gsMessages.add(message);
                        }
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            puller.onReceived(gsMessages);
                        }
                    });
                }

            },chatRoom, offset, length, desc);


        }
    }

    public GSCachedService getCachedService() {
        return cachedService;
    }
}
