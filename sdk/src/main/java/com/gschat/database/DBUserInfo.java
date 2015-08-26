package com.gschat.database;

import io.realm.RealmObject;


public class DBUserInfo extends RealmObject {

    private int sendSeqID;

    private int receivedSeqID;

    private int localSeqID;

    public int getSendSeqID() {
        return sendSeqID;
    }

    public void setSendSeqID(int sendSeqID) {
        this.sendSeqID = sendSeqID;
    }

    public int getReceivedSeqID() {
        return receivedSeqID;
    }

    public void setReceivedSeqID(int receivedSeqID) {
        this.receivedSeqID = receivedSeqID;
    }

    public int getLocalSeqID() {
        return localSeqID;
    }

    public void setLocalSeqID(int localSeqID) {
        this.localSeqID = localSeqID;
    }
}
