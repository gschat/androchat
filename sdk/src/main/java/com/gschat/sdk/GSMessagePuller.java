package com.gschat.sdk;


import java.util.List;

public interface GSMessagePuller {
    void onReceived(List<GSMessage> messages);
}
