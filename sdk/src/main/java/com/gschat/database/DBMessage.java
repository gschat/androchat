package com.gschat.database;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Message bean
 */
public class DBMessage extends RealmObject{
    /**
     * message id
     */
    @PrimaryKey
    private String id;

    /**
     * send or received seq id
     */
    private int seqID;

    /**
     * local message seq lid
     */
    private int localSeqID;

    /**
     * message type
     */
    private String type;

    /**
     * message source
     */
    private String source;

    /**
     * message target
     */
    private String target;

    /**
     * message content
     */
    private String content;

    /**
     * session id belongs to
     */
    @Index
    private String sessionID;

    /**
     * message read flag
     */
    private boolean readFlag;

    /**
     * true indicate this message is a send message
     */
    private boolean sendFlag;

    /**
     * last update Time
     */
    private Date updateTime;

    /**
     *
     */
    private int messageContentType;


    public int getMessageContentType() {
        return messageContentType;
    }

    public void setMessageContentType(int messageContentType) {
        this.messageContentType = messageContentType;
    }

    /**
     * message state
     */
    private String messageState;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public boolean isReadFlag() {
        return readFlag;
    }

    public void setReadFlag(boolean readFlag) {
        this.readFlag = readFlag;
    }

    public boolean isSendFlag() {
        return sendFlag;
    }

    public void setSendFlag(boolean sendFlag) {
        this.sendFlag = sendFlag;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessageState() {
        return messageState;
    }

    public void setMessageState(String messageState) {
        this.messageState = messageState;
    }

    public int getSeqID() {
        return seqID;
    }

    public void setSeqID(int seqID) {
        this.seqID = seqID;
    }

    public int getLocalSeqID() {
        return localSeqID;
    }

    public void setLocalSeqID(int localSeqID) {
        this.localSeqID = localSeqID;
    }
}
