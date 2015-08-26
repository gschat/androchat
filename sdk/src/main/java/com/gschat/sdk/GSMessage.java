package com.gschat.sdk;


import com.github.gschat.gsim.Message;
import com.github.gschat.gsim.MessageType;

import java.util.UUID;

/**
 * gschat message object
 */
public final class GSMessage {

    private String id;

    private String source;

    private String target;

    private MessageType type;

    private GSDirect direct;

    private GSMessageState state;

    private GSError errorCode;

    private final GSMessageBody body;

    /**
     * create new message with message body
     * @param body
     */
    public GSMessage(GSMessageBody body) {
        this.body = body;
        this.id = UUID.randomUUID().toString();
        this.state = GSMessageState.Creating;
    }

    public String getId() {
        return id;
    }

    public GSMessage setId(String id) {
        this.id = id;

        return this;
    }

    public String getSource() {
        return source;
    }

    public GSMessage setSource(String source) {
        this.source = source;
        return this;
    }

    public String getTarget() {
        return target;
    }

    public GSMessage setTarget(String target) {
        this.target = target;
        return this;
    }

    public MessageType getType() {
        return type;
    }

    public GSMessage setType(MessageType type) {
        this.type = type;
        return this;
    }

    public GSDirect getDirect() {
        return direct;
    }

    public GSMessage setDirect(GSDirect direct) {
        this.direct = direct;
        return this;
    }

    /**
     * get message body
     * @param <T> message body type
     * @return message body
     */
    public <T extends GSMessageBody> T getBody(Class<T> clazz) {
        return clazz.cast(body);
    }

    /**
     * get body
     * @return
     */
    public GSMessageBody getBody() {
        return this.body;
    }

    /**
     * get message body type
     * @return GSContentType enum
     */
    public GSContentType getBodyType() {
        return this.body.getType();
    }


    public GSMessageState getState() {
        return state;
    }

    public GSMessage setState(GSMessageState state) {
        this.state = state;
        return this;
    }

    public GSError getErrorCode() {
        return errorCode;
    }

    public GSMessage setErrorCode(GSError errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String key() {
        return this.getId() + this.direct;
    }
}
