package com.gschat.cached;


import android.os.Handler;

import com.gschat.events.Event;
import com.gschat.events.EventListener;
import com.gschat.events.Slot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.concurrent.Executor;

public final class GSCloudCached {

    private final static Logger logger = LoggerFactory.getLogger("GSCloudCached");


    /**
     * parent cached object
     */
    private final GSCached cached;

    /**
     * local cached process value
     */
    private int process = 0;

    /**
     * local cached file path
     */
    private File file;

    /**
     * last error
     */
    private Exception error;

    /**
     * gs cached object state
     */
    private GSCacheState state = GSCacheState.UnCached;


    private final Event<GSCloudCached> event;

    /**
     * remote url
     */
    private URL remoteURL;

    public GSCloudCached(GSCached cached) {

        this.cached = cached;

        final Handler handler = cached.getHandler();

        event = new Event<>(new Executor() {
            @Override
            public void execute(Runnable command) {
                handler.post(command);
            }
        });
    }

    /**
     * attache remote cached object
     * @param remoteURL
     */
    void attachRemoteCached(URL remoteURL) {

        this.remoteURL = remoteURL;

        this.process = 100;

        this.event.raise(this);
    }

    /**
     * add new cloud cached event
     * @param listener listener
     * @return listener slot
     */
    public Slot addListener(final EventListener<GSCloudCached> listener) {

        cached.getHandler().post(new Runnable() {
            @Override
            public void run() {
                listener.call(GSCloudCached.this);
            }
        });

        return this.event.connect(listener);
    }

    /**
     * notify begin write cloud cache object
     */
    public void onBeginWriteCloud() {

        this.process = 0;

        this.state = GSCacheState.Caching;

        event.raise(this);
    }

    /**
     * notify write cloud cache object process event
     * @param process current write process value
     */
    public void onWriteCloud(int process) {

        this.process = process;

        event.raise(this);
    }

    /**
     * notify begin write cloud cache object
     */
    public void onEndWriteCloud(URL url,Exception e) {

        if(e == null) {
            this.process = 100;
            this.remoteURL = url;

            cached.saveMetadata();

            this.state = GSCacheState.Cached;

        } else {
            this.process = -1;
            this.error = e;
            this.state = GSCacheState.CacheFault;
        }

        event.raise(this);
    }

    public final boolean upload(GSCloudUploader cloudUploader) {

        cached.upload(this,cloudUploader);

        return true;
    }

    public URL getRemoteURL() {
        return remoteURL;
    }

    public int getProcess() {
        return process;
    }

    public void attachRemoteURL(URL url) {
        this.remoteURL = url;
        this.process = 100;
        this.state = GSCacheState.Cached;

        this.event.raise(this);
    }

    public boolean isCached() {
        return this.state == GSCacheState.Cached;
    }
}
