package com.gschat.app.view;


import android.content.Context;
import android.content.Intent;
import android.view.View;

public interface ChatRoomView {

    void startActivityForResult(Intent intent, int requestCode);

    Context getContext();

    void showChatListView();

    View findViewById(int id);

    void scrollToBottom();
}
