package com.gschat.ipc;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public final class IPCUser implements Parcelable {

    /**
     *  User name
     */
    private final String userName;

    private final HashMap<String,String> properties;

    /**
     * create new IPCUser
     * @param userName
     */
    public IPCUser(String userName) {
        this.userName = userName;
        this.properties = new HashMap<>();
    }

    public String getUserName() {
        return userName;
    }

    public HashMap<String, String> getProperties() {
        return properties;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userName);
        dest.writeSerializable(this.properties);
    }

    protected IPCUser(Parcel in) {
        this.userName = in.readString();
        this.properties = (HashMap<String, String>) in.readSerializable();
    }

    public static final Parcelable.Creator<IPCUser> CREATOR = new Parcelable.Creator<IPCUser>() {
        public IPCUser createFromParcel(Parcel source) {
            return new IPCUser(source);
        }

        public IPCUser[] newArray(int size) {
            return new IPCUser[size];
        }
    };
}
