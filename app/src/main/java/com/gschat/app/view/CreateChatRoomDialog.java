package com.gschat.app.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.gschat.R;
import com.gschat.app.viewmodel.CreateChatRoomViewModel;

import org.robobinding.ViewBinder;

public final class CreateChatRoomDialog extends Dialog{

    private final GSChatView gsChatView;

    private final CreateChatRoomViewModel viewModel;

    public CreateChatRoomDialog(GSChatView gsChatView){

        super(gsChatView.getContext(),R.style.AppDialog);
        this.gsChatView = gsChatView;

        this.viewModel = new CreateChatRoomViewModel(this,gsChatView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewBinder viewBinder = gsChatView.createViewBinder();

        this.setContentView(viewBinder.inflateAndBind(R.layout.dialog_create_chat_room,viewModel));
    }
}