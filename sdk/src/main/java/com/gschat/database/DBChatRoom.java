package com.gschat.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by liyang on 15/7/22.
 */
public class DBChatRoom extends RealmObject{
    @PrimaryKey
    private String sessionID;

    /**
     * chat room type
     */
    private String chatRoomType;

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getChatRoomType() {
        return chatRoomType;
    }

    public void setChatRoomType(String chatRoomType) {
        this.chatRoomType = chatRoomType;
    }
}
