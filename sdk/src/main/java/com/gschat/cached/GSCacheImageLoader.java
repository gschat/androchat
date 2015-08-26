package com.gschat.cached;


import android.graphics.Bitmap;

public abstract class GSCacheImageLoader {

    private boolean canceled = false;

    protected abstract void onComplete(Exception error,Bitmap bitmap);

    public final void cancel() {
        this.canceled = true;
    }

    protected final void complete(Exception error,Bitmap bitmap) {
        if(!this.canceled) {
            onComplete(error,bitmap);
        }
    }
}
