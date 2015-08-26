package com.gschat.database;


import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * sync file bean object
 */
public class DBDownloadSyncFile extends RealmObject{

    /**
     * local file url
     */
    @Index
    private String localURL;

    /**
     * remote file url
     */
    @PrimaryKey
    private String remoteURL;


    public String getLocalURL() {
        return localURL;
    }

    public void setLocalURL(String localURL) {
        this.localURL = localURL;
    }

    public String getRemoteURL() {
        return remoteURL;
    }

    public void setRemoteURL(String remoteURL) {
        this.remoteURL = remoteURL;
    }
}
