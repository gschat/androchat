package com.gschat.app.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gschat.R;
import com.gschat.app.App;
import com.gschat.app.viewmodel.ChatRoomViewModel;

import org.robobinding.ViewBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatRoomFragment extends Fragment implements ChatRoomView {

    private final static Logger logger = LoggerFactory.getLogger("ChatListFragment");

    private String userName;

    /**
     * ChatRoom ViewModel reference
     */
    private ChatRoomViewModel chatRoomViewModel;

    /**
     * view
     */
    private View view;

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState != null) {
            userName = savedInstanceState.getString("username");
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("username",userName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        logger.debug("ChatListFragment onCreateView");

        App app = ((App) getActivity().getApplication());

        ViewBinder viewBinder = app.createViewBinder(getActivity());

        chatRoomViewModel = new ChatRoomViewModel(app.getGsChat(),this,userName,20);

        if(container == null) {
            view = viewBinder.inflateAndBind(R.layout.fragment_chat_room, chatRoomViewModel);
        } else {
            view = viewBinder.inflateAndBindWithoutAttachingToRoot(R.layout.fragment_chat_room, chatRoomViewModel, container);
        }

        chatRoomViewModel.create();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        chatRoomViewModel.stop();
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        chatRoomViewModel.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Context getContext() {
        return this.getActivity().getApplicationContext();
    }

    @Override
    public void showChatListView() {
        ((GSChatView)this.getActivity()).showChatListView();
    }

    @Override
    public View findViewById(int id) {
        return view.findViewById(id);
    }

    @Override
    public void scrollToBottom() {
        ListView listView = (ListView) view.findViewById(R.id.chatList);
        listView.setSelection(listView.getBottom());
    }
}

