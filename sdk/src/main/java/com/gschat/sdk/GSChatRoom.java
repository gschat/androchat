package com.gschat.sdk;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.gschat.gsim.MessageType;

public class GSChatRoom implements Parcelable {

    private final String id;

    /**
     * chat room type
     */
    private final MessageType type;

    public GSChatRoom(String id, MessageType type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public MessageType getType() {
        return type;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
    }

    protected GSChatRoom(Parcel in) {
        this.id = in.readString();
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : MessageType.values()[tmpType];
    }

    public static final Parcelable.Creator<GSChatRoom> CREATOR = new Parcelable.Creator<GSChatRoom>() {
        public GSChatRoom createFromParcel(Parcel source) {
            return new GSChatRoom(source);
        }

        public GSChatRoom[] newArray(int size) {
            return new GSChatRoom[size];
        }
    };
}
