package com.gschat.app.viewmodel;


import com.gschat.app.view.GSChatView;
import com.gschat.ipc.IPCUser;
import com.gschat.sdk.GSChatRoom;

import org.robobinding.itempresentationmodel.ItemContext;
import org.robobinding.itempresentationmodel.ItemPresentationModel;

public class ChatListItemViewModel implements ItemPresentationModel<GSChatRoom>{

    private final GSChatView view;

    private GSChatRoom chatRoom;

    public ChatListItemViewModel(GSChatView view) {

        this.view = view;
    }

    @Override
    public void updateData(GSChatRoom chatRoom, ItemContext itemContext) {

        this.chatRoom = chatRoom;
    }

    public String getSessionID() {
        return chatRoom.getId();
    }

    public void openChatRoom() {
        view.showChatRoom(chatRoom.getId());
    }
}
