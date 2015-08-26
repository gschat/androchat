package com.gschat.ipc;

import com.gschat.ipc.IPCMessage;

interface IMessagePuller {
    void onSuccess(in List<IPCMessage> messages);
}