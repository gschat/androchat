package com.gschat.app.widget;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gschat.R;
import com.gschat.sdk.GSContentType;
import com.gschat.sdk.GSDirect;
import com.gschat.sdk.GSMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mehdi.sakout.fancybuttons.FancyButton;

public class MessageView extends LinearLayout{

    private final static Logger logger = LoggerFactory.getLogger("widget.MSG");

    private GSDirect direct;

    /**
     * message type
     */
    private GSMessage message;

    /**
     * child view
     */
    private View view;

    /**
     * side layout
     */
    private View sideLayout;

    /**
     * message content type
     */
    private GSContentType contentType;
    private int process;


    /**
     * crete new chat message widget
     * @param context
     * @param attrs
     */
    public MessageView(Context context, AttributeSet attrs) {

        super(context, attrs);
    }

    private void createTextView() {

        view = LayoutInflater.from(this.getContext()).inflate(R.layout.chat_text_item, this, true);

        sideLayout = view.findViewById(R.id.side_layout);

    }

    private void createImageView() {
        view = LayoutInflater.from(this.getContext()).inflate(R.layout.chat_image_item, this, true);

        sideLayout = view.findViewById(R.id.side_layout);
    }


    public void setDirect(GSDirect direct) {

        RelativeLayout.LayoutParams layoutParams = ((RelativeLayout.LayoutParams) sideLayout.getLayoutParams());

        if(direct == GSDirect.To) {

            logger.debug("set align to right");

            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

            sideLayout.setLayoutParams(layoutParams);

        } else {

            logger.debug("set align to left");

            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            sideLayout.setLayoutParams(layoutParams);
        }
    }

    public void setText(String text,GSDirect direct) {

        if(this.contentType != GSContentType.Text) {

            this.removeAllViews();

            this.contentType = GSContentType.Text;

            createTextView();
        }

        FancyButton button = (FancyButton)view.findViewById(R.id.text_item);

        button.setText(text);

        if(direct == GSDirect.To) {
            button.setIconResource(getContext().getString(R.string.icon_send));
        } else {
            button.setIconResource(getContext().getString(R.string.icon_recv));
        }

        setDirect(direct);
    }

    public void setImage(Bitmap bitmap, GSDirect direct, int process) {

        if(this.contentType != GSContentType.Image) {

            this.removeAllViews();

            this.contentType = GSContentType.Image;

            createImageView();
        }

        setDirect(direct);

        FancyButton button = (FancyButton)view.findViewById(R.id.sync_flag);

        if(process < 100) {

            button.setVisibility(View.VISIBLE);

            if(direct == GSDirect.To) {
                button.setIconResource(getContext().getString(R.string.icon_upload));
            } else {
                button.setIconResource(getContext().getString(R.string.icon_download));
            }

            button.setText(String.format("%d%%", process));
        } else {
            button.setVisibility(View.GONE);
        }

        ImageView imageView = (ImageView) view.findViewById(R.id.image_item);

        imageView.setImageBitmap(bitmap);
    }

    public void setProcess(int process) {

        if(view == null) {
            return;
        }

        this.process = process;

        FancyButton button = (FancyButton)view.findViewById(R.id.sync_flag);

        if(button != null) {
            if(process < 100) {

                button.setVisibility(View.VISIBLE);

                if(direct == GSDirect.To) {
                    button.setIconResource(getContext().getString(R.string.icon_upload));
                } else {
                    button.setIconResource(getContext().getString(R.string.icon_download));
                }

                button.setText(String.format("%d%%", process));
            } else {
                button.setVisibility(View.GONE);
            }
        }
    }
}
