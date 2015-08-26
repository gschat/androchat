package com.gschat.ipc;

import android.os.Parcel;
import android.os.Parcelable;

import com.gschat.sdk.GSError;


public final class IPCError implements Parcelable {

    /**
     * success error GSError
     */
    public static final IPCError SUCCESS = new IPCError(GSError.SUCCESS);

    /**
     * GSError indicate lost com.gschat.core.CoreService
     */
    public static final IPCError CORE_SERVICE_LOST = new IPCError(GSError.CORE_SERVICE_LOST);

    /**
     * other user already login
     */
    public static final IPCError OTHER_USER_LOGIN = new IPCError(GSError.OTHER_USER_LOGIN);

    /**
     * unknown error
     */
    public static final IPCError UNKNOWN_ERROR = new IPCError(GSError.UNKNOWN_ERROR);

    /**
     * kick off error
     */
    public static final IPCError KICKOFF = new IPCError(GSError.KICKOFF);

    /**
     * kick off error
     */
    public static final IPCError LOGIN_FIRST = new IPCError(GSError.LOGIN_FIRST);
    /**
     * user name error
     */
    public static final IPCError USER_NAME_ERROR = new IPCError(GSError.USER_NAME_ERROR);

    /**
     * user name error
     */
    public static final IPCError CHANNEL_BROKEN = new IPCError(GSError.CHANNEL_BROKEN);

    /**
     * timeout error
     */
    public static final IPCError TIMEOUT = new IPCError(GSError.TIMEOUT);

    private final GSError code;

    public IPCError(GSError code) {
        this.code = code;
    }

    public GSError getCode() {
        return code;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code == null ? -1 : this.code.ordinal());
    }

    protected IPCError(Parcel in) {
        int tmpCode = in.readInt();
        this.code = tmpCode == -1 ? null : GSError.values()[tmpCode];
    }

    public static final Creator<IPCError> CREATOR = new Creator<IPCError>() {
        public IPCError createFromParcel(Parcel source) {
            return new IPCError(source);
        }

        public IPCError[] newArray(int size) {
            return new IPCError[size];
        }
    };
}
