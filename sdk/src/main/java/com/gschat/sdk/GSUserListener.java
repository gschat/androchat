package com.gschat.sdk;


public interface GSUserListener {
    void onStateChanged(String userName, GSUserState state, GSError errorCode);
}
