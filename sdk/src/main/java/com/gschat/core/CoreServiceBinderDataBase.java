package com.gschat.core;

import android.content.Context;

import com.github.gschat.gsim.MessageType;
import com.google.gson.Gson;
import com.gschat.database.DBChatRoom;
import com.gschat.database.DBMessage;
import com.gschat.database.DBUserInfo;
import com.gschat.database.UserModule;
import com.gschat.ipc.IPCMessage;
import com.gschat.sdk.GSChatListener;
import com.gschat.sdk.GSChatRoom;
import com.gschat.sdk.GSChatRoomListener;
import com.gschat.sdk.GSConfig;
import com.gschat.sdk.GSDirect;
import com.gschat.sdk.GSError;
import com.gschat.sdk.GSException;
import com.gschat.sdk.GSMessageState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * core service database facade
 */
final class CoreServiceBinderDataBase {

    /**
     * core service database logger
     */
    private static final Logger logger = LoggerFactory.getLogger("CoreServiceBinderDataBase");

    /**
     * android context
     */
    private final Context context;
    /**
     * client uri
     */
    private final String clientURI;
    /**
     * database config
     */
    private RealmConfiguration dbConfig;

    /**
     * google GSON object
     */
    private final Gson gson = new Gson();

    /**
     * opened user database
     */
    private String userName = "";
    private List<GSChatRoom> chatRooms;

    public CoreServiceBinderDataBase(Context context, String clientURI) {

        this.context = context;

        this.clientURI = clientURI;
    }

    public void openUserDataBase(String userName) {

        this.userName = userName;

        logger.debug("open user database({}) for {}", clientURI, userName);

        File targetDir = new File(String.format("%s/%s",GSConfig.StoreRootPath,clientURI));

        if(!targetDir.mkdirs()) {
            logger.debug("target dir already exists :{}", targetDir);
        }

        dbConfig = new RealmConfiguration.Builder(targetDir)
                .name(String.format("%s.realm", userName))
                .setModules(new UserModule())
                .build();
    }

    public void closeUserDataBase() {

        logger.debug("close user database({}) for {}", clientURI, userName);

        dbConfig = null;

        userName = "";
    }

    private void checkDataBaseState() throws Exception {
        if(dbConfig == null) {
            throw new GSException("user database is closed",GSError.RESOURCE);
        }
    }

    /**
     * save ipc message
     * @param ipcMessage
     */
    public void saveMessage(final IPCMessage ipcMessage,final GSChatRoomListener listener) throws Exception {

        checkDataBaseState();

        Realm realm = Realm.getInstance(dbConfig);

        try{

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    DBUserInfo userInfo = realm.where(DBUserInfo.class).findFirst();

                    if(userInfo == null) {
                        userInfo = realm.createObject(DBUserInfo.class);
                    }

                    if (ipcMessage.getDirect() == GSDirect.To) {

                        ipcMessage.setSeqID(userInfo.getSendSeqID());

                        userInfo.setSendSeqID(userInfo.getSendSeqID() + 1);

                    } else {

                        userInfo.setReceivedSeqID(ipcMessage.getSeqID());
                    }

                    final DBMessage dbMessage = toDBMessage(ipcMessage);

                    dbMessage.setLocalSeqID(userInfo.getLocalSeqID());

                    userInfo.setLocalSeqID(userInfo.getLocalSeqID() + 1);

                    dbMessage.setUpdateTime(new Date());

                    realm.copyToRealmOrUpdate(dbMessage);

                    String sessionID;

                    if (ipcMessage.getDirect() == GSDirect.To) {
                        sessionID = dbMessage.getTarget();
                    } else {
                        if (ipcMessage.getType() == MessageType.Multi) {
                            sessionID = dbMessage.getTarget();
                        } else {
                            sessionID = dbMessage.getSource();
                        }
                    }

                    DBChatRoom dbChatRoom = realm.where(DBChatRoom.class).equalTo("sessionID", sessionID).findFirst();

                    if (dbChatRoom == null) {
                        dbChatRoom = new DBChatRoom();
                        dbChatRoom.setChatRoomType(ipcMessage.getType().toString());
                        dbChatRoom.setSessionID(sessionID);
                        realm.copyToRealm(dbChatRoom);

                        listener.onUpdate(new GSChatRoom(sessionID,ipcMessage.getType()));
                    }
                }
            });

        }finally {
            realm.close();
        }
    }


    /**
     * get max received seq id
     */
    public int getReceivedSeqID() throws Exception {

        checkDataBaseState();

        Realm realm = Realm.getInstance(dbConfig);

        try {

            DBUserInfo userInfo = realm.where(DBUserInfo.class).findFirst();

            if(userInfo == null) {
                realm.beginTransaction();

                userInfo = realm.createObject(DBUserInfo.class);

                realm.commitTransaction();
            }

            return userInfo.getReceivedSeqID();

        } finally {
            realm.close();
        }
    }


    public IPCMessage updateMessageState(final String id, final GSMessageState state) throws Exception {

        checkDataBaseState();

        Realm realm = Realm.getInstance(dbConfig);

        try{

            realm.beginTransaction();

            DBMessage message = realm.where(DBMessage.class).equalTo("id",id).findFirst();

            if(message == null) {
                logger.warn("can't found message({})",id);
                return null;
            }

            logger.debug("update message({}) state from {} to {}", message.getMessageState(), state);

            message.setMessageState(state.toString());

            realm.commitTransaction();

            return toIPCMessage(message);

        } finally {
            realm.close();
        }
    }

    public IPCMessage getMessage(String id) throws Exception {

        checkDataBaseState();

        Realm realm = Realm.getInstance(dbConfig);

        try{

            DBMessage message = realm.where(DBMessage.class).equalTo("id",id).findFirst();

            if(message != null) {
                logger.warn("[getMessage] can't found message({})", id);
            }

            return toIPCMessage(message);

        } finally {
            realm.close();
        }
    }

    public List<GSChatRoom> getChatRooms() throws Exception {
        checkDataBaseState();

        Realm realm = Realm.getInstance(dbConfig);

        try{

            ArrayList<GSChatRoom> gsChatRooms = new ArrayList<>();

            RealmResults<DBChatRoom> chatRooms = realm.where(DBChatRoom.class).findAll();

            for (DBChatRoom dbChatRoom : chatRooms) {
                gsChatRooms.add(new GSChatRoom(dbChatRoom.getSessionID(),MessageType.valueOf(dbChatRoom.getChatRoomType())));
            }

            return gsChatRooms;

        } finally {
            realm.close();
        }
    }

    /**
     * convert DBMessage to IPCMessage
     * @param dbMessage db message
     * @return ipc message
     */
    private IPCMessage toIPCMessage(DBMessage dbMessage) {

        Gson gson = new Gson();

        IPCMessage message = gson.fromJson(dbMessage.getContent(), IPCMessage.class);

        message.setType(MessageType.valueOf(dbMessage.getType()));

        if(dbMessage.isSendFlag()) {
            message.setDirect(GSDirect.To);
        } else {
            message.setDirect(GSDirect.From);
        }

        message.setSource(dbMessage.getSource());

        message.setTarget(dbMessage.getTarget());

        message.setMessageBodyType(dbMessage.getMessageContentType());

        message.setState(GSMessageState.valueOf(dbMessage.getMessageState()));

        return message;
    }

    public List<IPCMessage> getMessages(String chatRoom, int offset, int length, boolean desc) throws Exception {

        checkDataBaseState();

        if (chatRoom == null) {
            return new ArrayList<>();
        }

        Realm realm = Realm.getInstance(dbConfig);

        try{

            RealmResults<DBMessage> messages =
                    realm.where(DBMessage.class).equalTo("sessionID", chatRoom).findAllSorted("localSeqID",false);

            int end = offset + length;

            if(end > messages.size()) {
                end = messages.size();
            }

            ArrayList<IPCMessage> ipcMessages = new ArrayList<>();

            for(int i = offset; i < end; i ++ ){
                ipcMessages.add(toIPCMessage(messages.get(i)));
            }

            return ipcMessages;

        } finally {
            realm.close();
        }
    }

    /**
     * convert from IPCMessage to DBMessage
     */
    private DBMessage toDBMessage(IPCMessage ipcMessage) {

        String content = gson.toJson(ipcMessage);

        DBMessage dbMessage = new DBMessage();

        dbMessage.setSeqID(ipcMessage.getSeqID());

        dbMessage.setSource(ipcMessage.getSource());

        dbMessage.setTarget(ipcMessage.getTarget());

        dbMessage.setId(ipcMessage.getId());

        if(ipcMessage.getDirect() == GSDirect.To) {
            dbMessage.setSessionID(ipcMessage.getTarget());
        } else {

            if(ipcMessage.getType() == MessageType.Multi) {
                dbMessage.setSessionID(ipcMessage.getTarget());
            } else {
                dbMessage.setSessionID(ipcMessage.getSource());
            }
        }



        dbMessage.setMessageContentType(ipcMessage.getContentType().getValue());

        if(ipcMessage.getDirect() == GSDirect.To) {
            dbMessage.setSendFlag(true);
            dbMessage.setReadFlag(true);
        } else {
            dbMessage.setSendFlag(false);
            dbMessage.setReadFlag(false);
        }

        dbMessage.setContent(content);

        dbMessage.setType(ipcMessage.getType().toString());

        dbMessage.setMessageState(ipcMessage.getState().toString());

        return dbMessage;
    }


}
