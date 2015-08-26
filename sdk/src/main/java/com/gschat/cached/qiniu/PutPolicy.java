package com.gschat.cached.qiniu;


import java.util.HashMap;

public final class PutPolicy {

    /**
     * upload target space
     */
    private String scope;

    /**
     * resource deadline
     */
    private long deadline;


    /**
     * resource user id
     */
    private String endUser;


    private String returnBody;


    private String saveKey = "$(etag)";


    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public String getEndUser() {
        return endUser;
    }

    public void setEndUser(String endUser) {
        this.endUser = endUser;
    }

    public String getReturnBody() {
        return returnBody;
    }

    public void setReturnBody(String returnBody) {
        this.returnBody = returnBody;
    }

    public String getSaveKey() {
        return saveKey;
    }

    public void setSaveKey(String saveKey) {
        this.saveKey = saveKey;
    }
}
