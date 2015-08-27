package com.gschat.app.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Toast;

import com.github.gschat.gsim.MessageType;
import com.gschat.R;
import com.gschat.app.view.ChatRoomView;
import com.gschat.cached.GSCachedImage;
import com.gschat.cached.GSCloudUploader;
import com.gschat.events.Slot;
import com.gschat.sdk.GSChat;
import com.gschat.sdk.GSDirect;
import com.gschat.sdk.GSImage;
import com.gschat.sdk.GSMessage;
import com.gschat.sdk.GSMessageListener;
import com.gschat.sdk.GSMessagePuller;
import com.gschat.sdk.GSText;

import org.robobinding.annotation.ItemPresentationModel;
import org.robobinding.annotation.PresentationModel;
import org.robobinding.presentationmodel.HasPresentationModelChangeSupport;
import org.robobinding.presentationmodel.PresentationModelChangeSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

@PresentationModel
public class ChatRoomViewModel implements HasPresentationModelChangeSupport {

    public static final int SEND_IMG = 1;

    /**
     * logger .
     */
    private final static Logger logger = LoggerFactory.getLogger("loginViewModel");

    /**
     * model change support
     */
    private final PresentationModelChangeSupport modelChangeSupport = new PresentationModelChangeSupport(this);

    /**
     * ChatRoom name
     */
    private final String name;


    /**
     * the message cached size
     */
    private final int cachedSize;


    /**
     * gschat service
     */
    private GSChat gsChat;

    /**
     * ChatRoom view interface
     */
    private ChatRoomView view;


    /**
     * message stack
     */
    private LinkedList<GSMessage> messages = new LinkedList<>();

    /**
     * message changed event listener slot
     */
    private Slot messageListenerSlot;

    /**
     * input text cache
     */
    private String textMessage;

    /**
     * send message flag
     */
    private boolean sendMessageFlag;


    /*
     * create ChatRoom ViewModel
     * @param name
     */
    public ChatRoomViewModel(GSChat gsChat, ChatRoomView view,String name,int cachedSize) {
        this.gsChat = gsChat;
        this.view = view;

        this.name = name;
        this.cachedSize = cachedSize;
    }

    @Override
    public PresentationModelChangeSupport getPresentationModelChangeSupport() {
        return modelChangeSupport;
    }

    @ItemPresentationModel(value = ChatRoomMessageViewModel.class, factoryMethod = "createChatRoomMessageViewModel")
    public List<GSMessage> getMessages() {
        logger.debug("get messages");
        return messages;
    }

    public ChatRoomMessageViewModel createChatRoomMessageViewModel() {
        return new ChatRoomMessageViewModel(this);
    }

    /**
     * start chatroom ViewModel
     *
     */
    public void create() {

        try {
            gsChat.pullMessages(new GSMessagePuller() {
                @Override
                public void onReceived(List<GSMessage> messages) {

                    for (GSMessage message : messages) {
                        addMessage(message);
                    }

                    onCreate();

                    ChatRoomViewModel.this.modelChangeSupport.firePropertyChange("messages");

                    view.scrollToBottom();

                }
            }, name, 0, 20, true);

        } catch (Exception e) {

            logger.error("pull message error .",e);
        }
    }

    /**
     * stop ChatRoom ViewModel
     */
    public void stop() {
        // TODO: add all clear codes here

        if(messageListenerSlot != null) {
            messageListenerSlot.disconnect();
        }

    }


    private void onCreate() {

        messageListenerSlot = gsChat.addMessageListener(new GSMessageListener() {
            @Override
            public void onStateChanged(GSMessage message) {
                boolean newMessage = ChatRoomViewModel.this.addMessage(message);

                ChatRoomViewModel.this.modelChangeSupport.firePropertyChange("messages");

                if(newMessage){
                    view.scrollToBottom();
                }
            }
        },getUserName());
    }
    /**
     * add message into message stack
     * @param message adding message
     */
    private boolean addMessage(GSMessage message) {

        for (int i = 0; i < messages.size(); i++) {

            GSMessage current = messages.get(i);

            if(current.getId().equals(message.getId())) {

                current.setErrorCode(message.getErrorCode());

                current.setState(message.getState());

                return false;
            }
        }

        messages.add(message);

        if (messages.size() > cachedSize) {
            messages.pop();
        }

        return true;
    }


    /**
     * set send message content
     */
    public void setTextMessage(String message) {
        this.textMessage = message;

        boolean refresh = false;

        if (message.length() > 0) {

            if (!sendMessageFlag) {
                refresh = true;
            }

            sendMessageFlag = true;
        } else {

            if (sendMessageFlag) {
                refresh = true;
            }

            sendMessageFlag = false;
        }

        if (refresh) {
            modelChangeSupport.firePropertyChange("buttonText");
            modelChangeSupport.firePropertyChange("buttonIconResource");
        }

    }

    /**
     * get send message content
     *
     */
    public String getTextMessage() {
        return this.textMessage;
    }

    /**
     * get user name
     */
    public String getUserName() {
        return this.name;
    }

    /**
     * process go back action
     */
    public void goBack() {
        this.view.showChatListView();
    }

    /**
     * process send action
     */
    public void onSend(View view) {
        if(sendMessageFlag){
            sendMessage();
        } else {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/jpg");
            this.view.startActivityForResult(intent, SEND_IMG);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        logger.debug("onActivityResult ");

        if(requestCode != SEND_IMG) {
            return;
        }

        if(intent == null) {
            return;
        }

        Uri data = intent.getData();

        File file;


        if("content".equals(data.getScheme())){
            file = uriToFile(data);
        } else {
            file = new File(data.getPath());
        }

        if (file == null) {

            Toast.makeText(
                    view.getContext(),
                    String.format(view.getContext().getString(R.string.error_send_message),"can't load image:" + data),
                    Toast.LENGTH_SHORT).show();

            return;
        }

        final GSImage image = new GSImage(gsChat,file);

        final GSMessage msg = new GSMessage(image)
                .setType(MessageType.Single)
                .setTarget(name)
                .setDirect(GSDirect.To);


        addMessage(msg);

        modelChangeSupport.firePropertyChange("messages");

        try {
            GSCachedImage cachedImage = image.bitmap();

            cachedImage.getCloudCached().upload(new GSCloudUploader() {
                @Override
                public void onSuccess(int size, URL original, URL thumbnail) {

                    image.getMetaData().setFileLength(size);
                    image.getMetaData().setUrl(original.toString());
                    image.getMetaData().setThumbnailUrl(thumbnail.toString());

                    try {
                        gsChat.sendMessage(msg);

                        logger.debug("send message {}",msg);

                    } catch (Exception e) {
                        logger.error("send message error {}", e);

                        Toast.makeText(
                                view.getContext(),
                                String.format(view.getContext().getString(R.string.error_send_message), e),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception e) {

            logger.error("send message error {}", e);

            Toast.makeText(
                    view.getContext(),
                    String.format(view.getContext().getString(R.string.error_send_message), e),
                    Toast.LENGTH_SHORT).show();
        }

        logger.debug("onActivityResult -- success");

    }



    private File uriToFile(Uri data) {

        String[] projection = { MediaStore.Images.Media.DATA };

        Cursor cursor = view.getContext().getContentResolver().query(data, projection, null, null, null);

        try {

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(projection[0]);

            String img_path = cursor.getString(columnIndex);

            if (img_path == null) {
                return null;
            }

            return new File(img_path);

        } finally {
            cursor.close();
        }
    }

    public void onEditFocus() {
        //view.scrollToBottom();
    }

    private void sendMessage() {
        GSMessage msg = new GSMessage(new GSText(this.textMessage))
                .setType(MessageType.Single)
                .setTarget(this.name)
                .setDirect(GSDirect.To);

        try {

            gsChat.sendMessage(msg);

            setTextMessage("");

            addMessage(msg);

            modelChangeSupport.firePropertyChange("messages");

            view.scrollToBottom();

        } catch (Exception e) {

            logger.error("send message error {}", e);

            Toast.makeText(
                    view.getContext(),
                    String.format(view.getContext().getString(R.string.error_send_message), e),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * sync the button icon resource
     */
    public String getButtonIconResource() {

        Context context = view.getContext();

        if (sendMessageFlag) {
            return context.getString(R.string.icon_share);
        } else {
            return context.getString(R.string.icon_plus_circle);
        }
    }

    /**
     * get button text
     */
    public String getButtonText() {

        Context context = view.getContext();

        if (sendMessageFlag) {
            return context.getString(R.string.send_message);
        } else {
            return "";
        }
    }
}
