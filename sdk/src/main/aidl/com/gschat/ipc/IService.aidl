// IService.aidl
package com.gschat.ipc;

import com.gschat.ipc.IClient;

import com.gschat.ipc.IPCMessage;
import com.gschat.ipc.IPCUser;
import com.gschat.ipc.IPCError;
import com.gschat.ipc.IMessagePuller;
import com.gschat.sdk.GSChatRoom;

interface IService {

    void register(IClient client);

    void login(in IPCUser user);

    void autoLogin();

    void logoff();

    List<GSChatRoom> getChatRooms();

    void sendMessage(in IPCMessage message);

    void getMessages(IMessagePuller puller,String chatRoom,int offset,int length,boolean desc);
}
