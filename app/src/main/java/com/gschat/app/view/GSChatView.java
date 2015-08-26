package com.gschat.app.view;


import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.gschat.ipc.IPCUser;

import org.robobinding.ViewBinder;

public interface GSChatView {

    Context getContext();

    void showChatRoom(String userName);

    void showLogoff();

    View findViewById(int id);

    ViewBinder createViewBinder();

    void goBackFragment();

    void showChatListView();

    void startActivityForResult(Intent chooser, int i);
}
