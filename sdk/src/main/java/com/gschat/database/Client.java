package com.gschat.database;

import com.gschat.ipc.IPCUser;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Client javabean
 */
public class Client extends RealmObject {

    @PrimaryKey
    private String url;

    /**
     * current login user
     */
    private String user;

    /**
     * current token
     */
    private byte[] token;

    public Client(){

    }

    public Client(String url) {
        this.url = url;
    }

    public Client(Client client) {
        this.url = client.getUrl();
        this.user = client.getUser();
        this.token = client.getToken();
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public byte[] getToken() {
        return token;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setToken(byte[] token) {
        this.token = token;
    }
}

