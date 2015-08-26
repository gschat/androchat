package com.gschat.ipc;


import android.os.Parcel;
import android.os.Parcelable;

import com.gschat.sdk.GSError;
import com.gschat.sdk.GSUserState;

public final class IPCUserEvent implements Parcelable {

    /**
     * action
     */
    private final GSUserState action;

    /**
     * user name
     */
    private final String userName;


    private final GSError errorCode;

    public IPCUserEvent(GSUserState action, String userName, GSError errorCode) {
        this.action = action;
        this.userName = userName;
        this.errorCode = errorCode;
    }

    public GSUserState getAction() {
        return action;
    }

    public String getUserName() {
        return userName;
    }

    public GSError getErrorCode() {
        return errorCode;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.action == null ? -1 : this.action.ordinal());
        dest.writeString(this.userName);
        dest.writeInt(this.errorCode == null ? -1 : this.errorCode.ordinal());
    }

    protected IPCUserEvent(Parcel in) {
        int tmpAction = in.readInt();
        this.action = tmpAction == -1 ? null : GSUserState.values()[tmpAction];
        this.userName = in.readString();
        int tmpErrorCode = in.readInt();
        this.errorCode = tmpErrorCode == -1 ? null : GSError.values()[tmpErrorCode];
    }

    public static final Creator<IPCUserEvent> CREATOR = new Creator<IPCUserEvent>() {
        public IPCUserEvent createFromParcel(Parcel source) {
            return new IPCUserEvent(source);
        }

        public IPCUserEvent[] newArray(int size) {
            return new IPCUserEvent[size];
        }
    };
}
