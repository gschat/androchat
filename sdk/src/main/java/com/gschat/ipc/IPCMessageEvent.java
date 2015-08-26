package com.gschat.ipc;

import android.os.Parcel;
import android.os.Parcelable;

import com.gschat.sdk.GSError;
import com.gschat.sdk.GSMessageState;

/**
 * message event
 */
public final class IPCMessageEvent implements Parcelable {

    private final IPCMessage message;

    private final GSMessageState action;

    private final GSError errorCode;

    public IPCMessageEvent(IPCMessage message, GSMessageState action,GSError errorCode) {
        this.message = message;
        this.action = action;
        this.errorCode = errorCode;
    }



    public IPCMessage getMessage() {
        return message;
    }

    /**
     *  get event action
     * @return
     */
    public GSMessageState getAction() {
        return action;
    }

    /**
     * get source message id
     * @return message id
     */
    public String getID() {
        return message.getId();
    }

    /**
     * get error code
     * @return
     */
    public GSError getErrorCode() {
        return errorCode;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.message, 0);
        dest.writeInt(this.action == null ? -1 : this.action.ordinal());
        dest.writeInt(this.errorCode == null ? -1 : this.errorCode.ordinal());
    }

    protected IPCMessageEvent(Parcel in) {
        this.message = in.readParcelable(IPCMessage.class.getClassLoader());
        int tmpAction = in.readInt();
        this.action = tmpAction == -1 ? null : GSMessageState.values()[tmpAction];
        int tmpErrorCode = in.readInt();
        this.errorCode = tmpErrorCode == -1 ? null : GSError.values()[tmpErrorCode];
    }

    public static final Creator<IPCMessageEvent> CREATOR = new Creator<IPCMessageEvent>() {
        public IPCMessageEvent createFromParcel(Parcel source) {
            return new IPCMessageEvent(source);
        }

        public IPCMessageEvent[] newArray(int size) {
            return new IPCMessageEvent[size];
        }
    };
}
