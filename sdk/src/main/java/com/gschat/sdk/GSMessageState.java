package com.gschat.sdk;


public enum GSMessageState {
    Creating,
    LocalApply,
    Sending,
    SendFailed,
    ServerApply,
    RemoteApply,
    RemoteReject,
    SyncProcessing,
    SyncFailed,
    SyncSuccess;
}
