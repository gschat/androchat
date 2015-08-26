package com.gschat.app;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.gschat.R;
import com.gschat.app.view.ChatListFragment;
import com.gschat.app.view.ChatRoomFragment;
import com.gschat.app.view.GSChatView;
import com.gschat.app.view.LoginFragment;
import com.gschat.app.view.WelcomeFragment;
import com.gschat.sdk.GSChat;
import com.gschat.sdk.GSChatListener;
import com.gschat.sdk.GSError;
import com.gschat.sdk.GSUserListener;
import com.gschat.sdk.GSUserState;

import com.gschat.events.Slot;
import org.robobinding.ViewBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MainActivity extends FragmentActivity implements GSChatView {

    private final static Logger logger = LoggerFactory.getLogger("LoginActivity");

    private final WelcomeFragment welcomeFragment = new WelcomeFragment();

    private final LoginFragment loginFragment = new LoginFragment();

    private final ChatListFragment chatListFragment = new ChatListFragment();

    private final ChatRoomFragment chatRoomFragment = new ChatRoomFragment();

    private Slot serviceStateEventSlot;

    private Slot userStateEventSlot;

    private String chatRoom;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("chatRoom", chatRoom);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            chatRoom = savedInstanceState.getString("chatRoom");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.main_activity, welcomeFragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        logger.debug("main activity onResume");

        final GSChat gsChat = ((App) getApplication()).getGsChat();

        serviceStateEventSlot = gsChat.addGSChatListener(new GSChatListener() {
            @Override
            public void onStateChanged(GSChat.State state) {

                logger.debug("GSChat state {}",state);

                FragmentManager fragmentManager = getSupportFragmentManager();

                if (GSChat.State.Connected == state) {

                    userStateEventSlot = gsChat.addUserListener(new GSUserListener() {
                        @Override
                        public void onStateChanged(String userName, GSUserState state, GSError errorCode) {

                            logger.debug("user state {}",state);

                            FragmentManager fragmentManager = getSupportFragmentManager();

                            if (GSUserState.Login == state) {

                                if(chatRoom == null) {
                                    fragmentManager.beginTransaction().replace(R.id.main_activity, chatListFragment).commit();
                                } else {

                                    chatRoomFragment.setUserName(userName);

                                    fragmentManager.beginTransaction().replace(R.id.main_activity, chatRoomFragment).commit();
                                }

                            } else {

                                chatRoom = null;

                                fragmentManager.beginTransaction().replace(R.id.main_activity, loginFragment).commit();
                            }
                        }
                    });

                } else {

                    fragmentManager.beginTransaction().replace(R.id.main_activity, welcomeFragment).commit();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        logger.debug("main activity onPause");

        serviceStateEventSlot.disconnect();

        serviceStateEventSlot = null;

        if (userStateEventSlot != null) {
            userStateEventSlot.disconnect();

            userStateEventSlot = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showChatRoom(String userName) {

        this.chatRoom = userName;

        chatRoomFragment.setUserName(userName);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager
                .beginTransaction()
                .replace(R.id.main_activity, chatRoomFragment)
                .commit();
    }

    @Override
    public void showLogoff() {

        this.chatRoom = null;

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager
                .beginTransaction()
                .replace(R.id.main_activity, loginFragment)
                .commit();
    }

    @Override
    public ViewBinder createViewBinder() {
        return ((App) getApplication()).createViewBinder(this);
    }

    @Override
    public void goBackFragment() {

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.popBackStack();
    }

    @Override
    public void showChatListView() {

        chatRoom = null;

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager
                .beginTransaction()
                .replace(R.id.main_activity, chatListFragment)
                .commit();
    }
}
