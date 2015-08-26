package com.gschat.ipc;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.gschat.gsim.MessageType;
import com.gschat.sdk.GSContentType;
import com.gschat.sdk.GSDirect;
import com.gschat.sdk.GSMessageState;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * message object
 */
public final class IPCMessage implements Parcelable {


    /**
     * sequence id
     */
    private transient int seqID;

    /**
     * message type
     */
    private transient MessageType type;

    /**
     * message source id
     */

    private transient String source;

    /**
     * message target
     */
    private transient String target;

    /**
     * message direct
     */
    private transient GSDirect direct;

    /**
     * message state
     */
    private transient GSMessageState state;


    /**
     * get content type
     */
    private transient int messageBodyType;

    /**
     * message id
     */
    private final String id;

    /**
     * message body list
     */
    private final ArrayList<String> bodies;

    /**
     * message extra data map
     */
    private final HashMap<String, String> extra;


    /**
     * create new message
     * @param type message type
     * @param source source user id
     * @param target target user id
     */
    public IPCMessage(String id,MessageType type, String source, String target) {
        this.type = type;
        this.source = source;
        this.target = target;
        this.id = id;

        this.extra =  new HashMap<>();

        this.bodies = new ArrayList<>(1);
    }


    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public GSDirect getDirect() {
        return direct;
    }

    public void setDirect(GSDirect direct) {
        this.direct = direct;
    }

    public GSContentType getContentType() {
        return GSContentType.create(messageBodyType);
    }

    public String getId() {
        return id;
    }

    public GSMessageState getState() {
        return state;
    }

    public void setState(GSMessageState state) {
        this.state = state;
    }

    public int getSeqID() {
        return seqID;
    }

    public void setSeqID(int seqID) {
        this.seqID = seqID;
    }

    public void setContent(String content,GSContentType contentType) {
        this.messageBodyType = contentType.getValue();
        this.bodies.add(0, content);
    }

    public void setMessageBodyType(int contentType) {
        this.messageBodyType = contentType;
    }

    public void setContent(String content,int contentType) {
        this.messageBodyType = contentType;
        this.bodies.add(0, content);
    }

    public String getContent() {
        return this.bodies.get(0);
    }


    @Override
    public String toString() {
        return "IPCMessage{" +
                "type=" + type +
                ", source='" + source + '\'' +
                ", target='" + target + '\'' +
                ", direct=" + direct +
                ", contentType=" + messageBodyType +
                ", id='" + id + '\'' +
                ", body=" + getContent() +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.seqID);
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeString(this.source);
        dest.writeString(this.target);
        dest.writeInt(this.direct == null ? -1 : this.direct.ordinal());
        dest.writeInt(this.state == null ? -1 : this.state.ordinal());
        dest.writeInt(this.messageBodyType);
        dest.writeString(this.id);
        dest.writeStringList(this.bodies);
        dest.writeSerializable(this.extra);
    }

    protected IPCMessage(Parcel in) {
        this.seqID = in.readInt();
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : MessageType.values()[tmpType];
        this.source = in.readString();
        this.target = in.readString();
        int tmpDirect = in.readInt();
        this.direct = tmpDirect == -1 ? null : GSDirect.values()[tmpDirect];
        int tmpState = in.readInt();
        this.state = tmpState == -1 ? null : GSMessageState.values()[tmpState];
        this.messageBodyType = in.readInt();
        this.id = in.readString();
        this.bodies = in.createStringArrayList();
        this.extra = (HashMap<String, String>) in.readSerializable();
    }

    public static final Creator<IPCMessage> CREATOR = new Creator<IPCMessage>() {
        public IPCMessage createFromParcel(Parcel source) {
            return new IPCMessage(source);
        }

        public IPCMessage[] newArray(int size) {
            return new IPCMessage[size];
        }
    };
}
