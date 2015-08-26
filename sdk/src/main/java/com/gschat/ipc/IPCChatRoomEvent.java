package com.gschat.ipc;

import android.os.Parcel;
import android.os.Parcelable;

import com.gschat.sdk.GSChatRoom;

/**
 * chat room event
 */
public final class IPCChatRoomEvent implements Parcelable {

    public enum Action {
        Update,Remove
    }

    /**
     * chat room event action
     */
    private final Action action;

    /**
     * chat room's id
     */
    private final GSChatRoom chatRoom;


    public IPCChatRoomEvent(Action action, GSChatRoom chatRoom) {
        this.action = action;
        this.chatRoom = chatRoom;
    }

    public Action getAction() {
        return action;
    }

    public GSChatRoom getChatRoom() {
        return chatRoom;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.action == null ? -1 : this.action.ordinal());
        dest.writeParcelable(this.chatRoom, 0);
    }

    protected IPCChatRoomEvent(Parcel in) {
        int tmpAction = in.readInt();
        this.action = tmpAction == -1 ? null : Action.values()[tmpAction];
        this.chatRoom = in.readParcelable(GSChatRoom.class.getClassLoader());
    }

    public static final Creator<IPCChatRoomEvent> CREATOR = new Creator<IPCChatRoomEvent>() {
        public IPCChatRoomEvent createFromParcel(Parcel source) {
            return new IPCChatRoomEvent(source);
        }

        public IPCChatRoomEvent[] newArray(int size) {
            return new IPCChatRoomEvent[size];
        }
    };
}
