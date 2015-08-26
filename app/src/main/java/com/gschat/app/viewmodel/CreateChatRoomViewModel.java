package com.gschat.app.viewmodel;


import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import com.gschat.R;
import com.gschat.app.view.CreateChatRoomDialog;
import com.gschat.app.view.GSChatView;
import com.gschat.ipc.IPCUser;

import org.robobinding.annotation.PresentationModel;

@PresentationModel
public class CreateChatRoomViewModel {

    private String userID = "";

    private final CreateChatRoomDialog dialog;
    private final GSChatView gsChatView;

    public CreateChatRoomViewModel(CreateChatRoomDialog dialog,GSChatView gsChatView) {
        this.dialog = dialog;
        this.gsChatView = gsChatView;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void ok() {

        InputMethodManager imm = (InputMethodManager)
                this.dialog.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(this.dialog.findViewById(R.id.edit_create_chat_room).getWindowToken(), 0);

        this.dialog.dismiss();

        if(userID.equals("")) {
            return;
        }

        this.gsChatView.showChatRoom(userID);
    }
}
