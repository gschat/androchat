package com.gschat.sdk;


public interface GSChatRoomListener {
    void onUpdate(GSChatRoom chatRoom);

    void onRemove(GSChatRoom chatRoom);
}
