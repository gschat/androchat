package com.gschat.sdk;


import com.google.gson.Gson;

public class GSText implements GSMessageBody {

    /**
     * message body type
     */
    private final int messageBodyType = GSContentType.Text.getValue();

    /**
     * text text
     */
    private final String text;

    /**
     * create new GSText body
     * @param text
     */
    public GSText(String text) {
        this.text = text;
    }

    @Override
    public GSContentType getType() {
        return GSContentType.Text;
    }

    @Override
    public String toJson() {

        Gson gson = new Gson();

        return gson.toJson(this);
    }

    /**
     * get GSText text
     * @return
     */
    public String getText() {
        return text;
    }

    public static GSMessageBody fromJson(String content) {

        Gson gson = new Gson();

        return gson.fromJson(content,GSText.class);
    }

    public int getMessageBodyType() {
        return messageBodyType;
    }
}
