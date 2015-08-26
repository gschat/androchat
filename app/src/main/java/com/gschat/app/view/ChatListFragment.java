package com.gschat.app.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gschat.R;
import com.gschat.app.App;
import com.gschat.app.viewmodel.ChatListViewModel;
import com.gschat.sdk.GSChat;

import org.robobinding.ViewBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatListFragment extends Fragment {

    private final static Logger logger = LoggerFactory.getLogger("ChatListFragment");

    private ChatListViewModel chatListViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        logger.debug("ChatListFragment onCreateView");

        App app = ((App) getActivity().getApplication());

        ViewBinder viewBinder = app.createViewBinder(getActivity());

        chatListViewModel = new ChatListViewModel(app.getGsChat(),(GSChatView)getActivity());

        if(container == null) {
            return viewBinder.inflateAndBind(R.layout.fragment_chat_list, chatListViewModel);
        }

        return viewBinder.inflateAndBindWithoutAttachingToRoot(R.layout.fragment_chat_list, chatListViewModel, container);
    }

    @Override
    public void onResume() {
        super.onResume();

        logger.debug("ChatListFragment onResume");

        chatListViewModel.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

        chatListViewModel.onPause();
    }
}
