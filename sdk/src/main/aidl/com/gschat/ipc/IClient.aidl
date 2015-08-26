package com.gschat.ipc;

import com.gschat.ipc.IPCMessageEvent;
import com.gschat.ipc.IPCChatRoomEvent;
import com.gschat.ipc.IPCNetEvent;
import com.gschat.ipc.IPCUserEvent;
import com.gschat.ipc.IPCError;

interface IClient {
    void onMessageEvent(in IPCMessageEvent event);
    void onChatRoomEvent(in IPCChatRoomEvent event);
    void onNetEvent(in IPCNetEvent event);
    void onUserEvent(in IPCUserEvent event);
}