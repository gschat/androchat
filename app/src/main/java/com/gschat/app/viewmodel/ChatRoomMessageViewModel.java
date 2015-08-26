package com.gschat.app.viewmodel;


import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.gschat.R;
import com.gschat.app.widget.MessageView;
import com.gschat.cached.GSCacheImageLoader;
import com.gschat.cached.GSCachedImage;
import com.gschat.cached.GSCloud;
import com.gschat.cached.GSCloudCached;
import com.gschat.cached.GSLocalCached;
import com.gschat.events.EventListener;
import com.gschat.events.Slot;
import com.gschat.sdk.GSDirect;
import com.gschat.sdk.GSImage;
import com.gschat.sdk.GSMessage;
import com.gschat.sdk.GSText;

import org.robobinding.annotation.PresentationModel;
import org.robobinding.itempresentationmodel.ItemContext;
import org.robobinding.itempresentationmodel.ItemPresentationModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PresentationModel
public class ChatRoomMessageViewModel implements ItemPresentationModel<GSMessage> {

    private final static Logger logger = LoggerFactory.getLogger("ChatRoomMessageViewModel");


    private final ChatRoomViewModel chatRoomViewModel;

    private MessageView messageView;


    /**
     * bound message
     */
    private GSMessage message;

    private Slot localCachedListener;

    private Slot memoryCachedListener;

    private Slot cloudCachedListener;

    public ChatRoomMessageViewModel(ChatRoomViewModel chatRoomViewModel) {

        this.chatRoomViewModel = chatRoomViewModel;
    }

    @Override
    public void updateData(GSMessage gsMessage, ItemContext itemContext) {

        if(localCachedListener != null) {
            localCachedListener.disconnect();
            localCachedListener = null;
        }

        if(memoryCachedListener != null) {
            memoryCachedListener.disconnect();
            memoryCachedListener = null;
        }

        if(cloudCachedListener != null) {
            cloudCachedListener.disconnect();
            cloudCachedListener = null;
        }

        this.message = gsMessage;

        this.messageView = (MessageView) itemContext.getItemView();

        switch (gsMessage.getBodyType()) {
            case Text:

                updateText();

                break;

            case Image:

                updateImage(itemContext);

                break;
            case Video:
                break;
            case Location:
                break;
            case Voice:
                break;
            case File:
                break;
            case Command:
                break;
        }

    }

    private void updateText() {

        this.messageView.setText(
                message.getBody(GSText.class).getText(),
                message.getDirect()
        );
    }


    private void loadImage(final GSCachedImage thumbnail,final GSCloudCached cloudCached) {

        if(message.getDirect() == GSDirect.To) {

            if(!cloudCached.isCached()) {
                cloudCachedListener = cloudCached.addListener(new EventListener<GSCloudCached>() {
                    @Override
                    public void call(GSCloudCached arg) {
                        messageView.setProcess(arg.getProcess());
                    }
                });
            }
        }


        Bitmap bitmap = thumbnail.createBitmap(256, 256);

        if (bitmap == null) {

            if (memoryCachedListener != null) {
                return;
            }

            memoryCachedListener = thumbnail.addListener(new EventListener<GSCachedImage>() {
                @Override
                public void call(GSCachedImage arg) {
                    Bitmap bitmap = thumbnail.createBitmap(256, 256);

                    MessageView messageView = ChatRoomMessageViewModel.this.messageView;

                    if(message.getDirect() == GSDirect.To) {
                        messageView.setImage(bitmap, message.getDirect(), cloudCached.getProcess());
                    } else {
                        messageView.setImage(bitmap, message.getDirect(), 100);
                    }
                }
            });
        } else {
            if(message.getDirect() == GSDirect.To) {
                messageView.setImage(bitmap, message.getDirect(), cloudCached.getProcess());
            } else {
                messageView.setImage(bitmap, message.getDirect(), 100);
            }
        }
    }

    private void updateImage(ItemContext itemContext)  {

        try{
            GSImage image = message.getBody(GSImage.class);

            final GSCachedImage thumbnail  = image.thumbnail();

            final GSLocalCached thumbnailLocalCached = thumbnail.getLocalCached();

            final GSCloudCached imageCloudCached = image.bitmap().getCloudCached();

            if(thumbnailLocalCached.isCached()) {
                loadImage(thumbnail,imageCloudCached);
                return;
            }

            localCachedListener = thumbnailLocalCached.addListener(new EventListener<GSLocalCached>() {
                @Override
                public void call(GSLocalCached localCached) {

                    if (thumbnailLocalCached.isCached()) {
                        loadImage(thumbnail,imageCloudCached);
                        return;
                    }

                    thumbnailLocalCached.download();

                    ChatRoomMessageViewModel.this.messageView.setImage(
                            null, message.getDirect(),
                            localCached.getProcess()
                    );
                }
            });

        }catch (Exception e) {

            logger.error("get message error {}", e);

            Context context = itemContext.getItemView().getContext();

            Toast.makeText(
                    context,
                    String.format(context.getString(R.string.error_send_message), e),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
