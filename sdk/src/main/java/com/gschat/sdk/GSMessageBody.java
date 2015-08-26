package com.gschat.sdk;


public interface GSMessageBody {
    /**
     * get message body type
     * @return GSContentType
     */
    GSContentType getType();

    /**
     * create json string from message body
     * @return
     */
    String toJson();
}
