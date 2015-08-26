package com.gschat.cached;

import android.os.Handler;

import com.gschat.events.Event;
import com.gschat.events.EventListener;
import com.gschat.events.Slot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Executor;

/**
 * local cached object
 */
public final class GSLocalCached {

    private final static Logger logger = LoggerFactory.getLogger("GSLocalCached");


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


    private final Event<GSLocalCached> event;
    /**
     * file output stream
     */
    private OutputStream outputStream;


    /**
     * create new local cache object
     * @param cached parent cached object
     */
    GSLocalCached(GSCached cached) {

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
     * attach external file as local cached file object
     * @param file
     */
    public void attachLocalFile(File file){
        this.file = file;
        this.process = 100;

        this.state = GSCacheState.Cached;

        this.event.raise(this);
    }

    /**
     * reset local cached object state
     */
    public void reset() {
        file = null;
        process = 0;
        error = null;
        state = GSCacheState.UnCached;
    }

    /**
     * get last error
     */
    public Exception getError() {
        return error;
    }

    public GSCacheState getState() {
        return state;
    }


    /**
     * close local cache write stream;
     *
     * @throws IOException
     */
    private void closeLocalCacheStream() throws IOException {

        if (outputStream != null) {
            outputStream.close();
            outputStream = null;
        }
    }

    /**
     * start write local cache object
     *
     * @param file local cache object host file
     * @throws IOException if open target file failed,throw this exception
     */
    public void beginWriteLocal(File file) throws IOException {

        closeLocalCacheStream();

        try {

            //logger.debug("begin local caching process\n\tremote :{}\n\tlocal :{}",this.remoteURL,file);

            outputStream = new FileOutputStream(file, false);

            this.process = 0;

            this.file = file;

            this.state = GSCacheState.Caching;

            this.event.raise(this);

        } catch (IOException e) {
            this.error = e;
            this.process = -1;
            throw e;
        }
    }



    /**
     * write local cached object data
     */
    public void writeLocal(byte[] buffer,int offset, int length,int process) throws IOException {

        assert outputStream != null : "call beginWriteLocal first";

        outputStream.write(buffer, offset, length);

        outputStream.flush();

        this.process = process;

        this.event.raise(this);
    }

    /**
     * finish write local cache object
     *
     * @param e not null if write local cache object failed
     */
    public void endWriteLocal(Exception e) {
        try {

            closeLocalCacheStream();

            if (e != null) {
                this.process = -1;

                this.file = null;

                this.error = e;

                this.state = GSCacheState.CacheFault;

            } else {

                this.process = 100;

                cached.saveMetadata();

                this.state = GSCacheState.Cached;
            }

        } catch (IOException ioe) {

            logger.error("finish local cache error",e);

            this.process = -1;

            this.error = e;

            this.file = null;

            this.state = GSCacheState.CacheFault;
        }

        this.event.raise(this);
    }

    public Slot addListener(final EventListener<GSLocalCached> eventListener) {

        cached.getHandler().post(new Runnable() {
            @Override
            public void run() {
                eventListener.call(GSLocalCached.this);
            }
        });

        return this.event.connect(eventListener);
    }

    /**
     * start a new download process
     */
    public boolean download() {

        if(this.state == GSCacheState.CacheFault || this.state == GSCacheState.UnCached) {

            this.state = GSCacheState.Caching;

            cached.download(this);

            return true;
        }

        return false;
    }

    public File getLocalFile() {
        return file;
    }

    public int getProcess() {
        return process;
    }

    public boolean isCached() {
        return this.state == GSCacheState.Cached;
    }
}
