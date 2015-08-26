package com.gschat.cached;


import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class GSCachedMetaData extends RealmObject{

    /**
     * local file path
     */
    @Index
    private String localPath;

    /**
     * remote URL
     */
    @PrimaryKey
    private String remoteURL;

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getRemoteURL() {
        return remoteURL;
    }

    public void setRemoteURL(String remoteURL) {
        this.remoteURL = remoteURL;
    }
}
