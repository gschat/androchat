package com.gschat.cached;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.gschat.events.Event;
import com.gschat.events.EventListener;
import com.gschat.events.Slot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.concurrent.Executor;

/**
 * cached image object
 */
public final class GSCachedImage extends GSCached {

    private final static Logger logger = LoggerFactory.getLogger("GSCachedImage");

    private final HashMap<String, Bitmap> cached = new HashMap<>();

    private final Event<GSCachedImage> event;

    /**
     * create new cached object
     *
     * @param service
     */
    public GSCachedImage(final GSCachedService service) {
        super(service);

        event = new Event<>(new Executor() {
            @Override
            public void execute(Runnable command) {
                service.getHandler().post(command);
            }
        });
    }

    public Slot addListener(final EventListener<GSCachedImage> listener) {

        getHandler().post(new Runnable() {
            @Override
            public void run() {
                listener.call(GSCachedImage.this);
            }
        });

        return event.connect(listener);
    }

    public Bitmap createBitmap(final int maxWidth, final int maxHeight) {

        synchronized (cached) {

            final String key = String.format("%d:%d", maxWidth, maxHeight);

            if (!cached.containsKey(key)) {

                logger.debug("create Bitmap");

                cached.put(key, null);

                this.getExecutor().execute(new Runnable() {

                    @Override
                    public void run() {

                        final Bitmap bitmap = GSCachedImage.this.load(maxWidth, maxHeight);

                        if (bitmap != null) {

                            logger.debug("create Bitmap -- success");

                            logger.debug("put Bitmap into cache");

                            synchronized (cached) {

                                cached.put(key,bitmap);
                            }

                            logger.debug("put Bitmap into cache -- success");

                        } else {
                            logger.debug("create Bitmap -- faield");
                        }

                        event.raise(GSCachedImage.this);
                    }
                });
            }

            return cached.get(key);
        }

    }

    private Bitmap load(int maxWidth, int maxHeight) {
        // get raw size
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;

        String filepath = this.getLocalCached().getLocalFile().toString();

        BitmapFactory.decodeFile(filepath, options);

        int scale = 1;

        if (maxHeight != -1 && maxWidth != -1) {
            // scale original image
            if (maxWidth < options.outWidth || maxHeight < options.outHeight) {
                if (options.outWidth > options.outHeight) {
                    scale = Math.round((float) options.outHeight / (float) maxHeight);
                } else {
                    scale = Math.round((float) options.outWidth / (float) maxWidth);
                }
            }
        }


        options.inSampleSize = scale;

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filepath, options);
    }

    @Override
    int sizeOf() {

        int size = 0;

        for (Bitmap bitmap : cached.values()) {
            size += bitmap.getByteCount();
        }

        return size;
    }

    @Override
    protected boolean isImage() {
        return true;
    }
}
