package com.gschat.app.viewmodel;

import android.view.View;
import android.widget.Toast;

import com.gschat.R;
import com.gschat.app.view.CreateChatRoomDialog;
import com.gschat.app.view.GSChatView;
import com.gschat.events.Slot;
import com.gschat.sdk.GSChat;
import com.gschat.sdk.GSChatRoom;
import com.gschat.sdk.GSChatRoomListener;

import org.robobinding.annotation.ItemPresentationModel;
import org.robobinding.annotation.PresentationModel;
import org.robobinding.presentationmodel.HasPresentationModelChangeSupport;
import org.robobinding.presentationmodel.PresentationModelChangeSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@PresentationModel
public class ChatListViewModel implements HasPresentationModelChangeSupport {

    /**
     * logger .
     */
    private final static Logger logger = LoggerFactory.getLogger("ChatListViewModel");



    /**
     * model change support
     */
    private final PresentationModelChangeSupport modelChangeSupport = new PresentationModelChangeSupport(this);

    /**
     * gschat service
     */
    private final GSChat gsChat;
    /**
     * app context
     */
    private final GSChatView gsChatView;

    private Slot slot;

    /**
     * create new chat list view model
     *
     * @param gsChat
     */
    public ChatListViewModel(GSChat gsChat, GSChatView gsChatView) {

        this.gsChat = gsChat;
        this.gsChatView = gsChatView;
    }

    @ItemPresentationModel(value = ChatListItemViewModel.class,factoryMethod="createChatListViewModel")
    public List<GSChatRoom> getSessionList() {
        try {

            return gsChat.getChatRoomList();

        } catch (Exception e) {
            Toast.makeText(
                    gsChatView.getContext(),
                    String.format(gsChatView.getContext().getString(R.string.query_chat_list_error), e),
                    Toast.LENGTH_SHORT).show();

            return null;
        }
    }

    public ChatListItemViewModel createChatListViewModel() {
        return new ChatListItemViewModel(gsChatView);
    }

    public void onAddChatRoom(View view) {

        CreateChatRoomDialog dialog=new CreateChatRoomDialog(gsChatView);

        dialog.setTitle(R.string.chatroom_add);

        dialog.show();
    }

    public void onLogoff(View view) {

        try {
            gsChat.logoff();

        } catch (Exception e) {
            Toast.makeText(
                    gsChatView.getContext(),
                    String.format(gsChatView.getContext().getString(R.string.logoff_error), e),
                    Toast.LENGTH_SHORT).show();

            return;
        }
    }

    public void onPause() {

    }

    public void onResume()  {
        slot = gsChat.addGSChatRoomListener(new GSChatRoomListener() {
            @Override
            public void onUpdate(GSChatRoom chatRoom) {
                ChatListViewModel.this.modelChangeSupport.firePropertyChange("sessionList");
            }

            @Override
            public void onRemove(GSChatRoom chatRoom) {
                ChatListViewModel.this.modelChangeSupport.firePropertyChange("sessionList");
            }
        });
    }

    @Override
    public PresentationModelChangeSupport getPresentationModelChangeSupport() {
        return this.modelChangeSupport;
    }
}
