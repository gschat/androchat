package com.gschat.ipc;


import android.os.Parcel;
import android.os.Parcelable;

import com.github.gsdocker.gsrpc.State;
import com.gschat.sdk.GSError;

public final class IPCNetEvent implements Parcelable {
    /**
     * net event
     */
    private final State state;

    private final GSError error;

    public IPCNetEvent(State state, GSError error) {
        this.state = state;
        this.error = error;
    }

    public GSError getError() {
        return error;
    }


    /**
     * get current net state
     * @return
     */
    public State getState() {
        return state;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.state == null ? -1 : this.state.ordinal());
        dest.writeInt(this.error == null ? -1 : this.error.ordinal());
    }

    protected IPCNetEvent(Parcel in) {
        int tmpState = in.readInt();
        this.state = tmpState == -1 ? null : State.values()[tmpState];
        int tmpError = in.readInt();
        this.error = tmpError == -1 ? null : GSError.values()[tmpError];
    }

    public static final Creator<IPCNetEvent> CREATOR = new Creator<IPCNetEvent>() {
        public IPCNetEvent createFromParcel(Parcel source) {
            return new IPCNetEvent(source);
        }

        public IPCNetEvent[] newArray(int size) {
            return new IPCNetEvent[size];
        }
    };
}
