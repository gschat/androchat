package com.gschat.cached;

import android.os.Handler;
import android.util.LruCache;

import com.gschat.events.Event;
import com.gschat.events.EventListener;
import com.gschat.events.Slot;
import com.gschat.sdk.GSConfig;
import com.qiniu.android.utils.UrlSafeBase64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.concurrent.Executor;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * gschat file cached system
 */
public final class GSCachedService {

    private static final Logger logger = LoggerFactory.getLogger("GSCachedService");

    /**
     * memory cached
     */
    private final LruCache<String, GSCached> memoryCached;

    /**
     * ui thread handler
     */
    private final Handler handler;

    /**
     * local caching process event
     */
    private final Event<GSCached> localCachingEvent;

    /**
     * cloud caching process event
     */
    private final Event<GSCached> cloudCachingEvent;

    /**
     * memory caching  process event
     */
    private final Event<GSCached> memoryCachingEvent;

    /**
     * gschat client id
     */
    private final String uri;
    /**
     * cloud cache api
     */
    private final GSCloud cloud;

    /**
     * async executor
     */
    private final Executor executor;

    /**
     * local cached file root path
     */
    private final String downloadDir;

    /**
     * metadata database config
     */
    private final RealmConfiguration dbConfig;

    /**
     * create new cached service,must be create in main ui thread
     */
    public GSCachedService(String uri, GSCloud cloud, Executor executor) {
        this.uri = uri;

        cloud.setExecutor(executor);

        this.cloud = cloud;

        this.executor = executor;

        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        int cacheSize = maxMemory / 8;

        memoryCached = new LruCache<String, GSCached>(cacheSize) {
            @Override
            protected int sizeOf(String key, GSCached memory) {
                return memory.sizeOf() / 1024;
            }
        };

        this.handler = new Handler();

        localCachingEvent = new Event<>(new Executor() {
            @Override
            public void execute(Runnable command) {
                handler.post(command);
            }
        });

        cloudCachingEvent = new Event<>(new Executor() {
            @Override
            public void execute(Runnable command) {
                handler.post(command);
            }
        });

        memoryCachingEvent = new Event<>(new Executor() {
            @Override
            public void execute(Runnable command) {
                handler.post(command);
            }
        });

        File targetDir = new File(String.format("%s/%s", GSConfig.StoreRootPath, uri));

        if (!targetDir.mkdirs()) {
            logger.debug("target dir already exists :{}", targetDir);
        }

        dbConfig = new RealmConfiguration
                .Builder(targetDir)
                .name("cached.realm")
                .setModules(new GSCachedModule())
                .build();

        downloadDir = GSConfig.StoreRootPath + "/download/";

        File dir = new File(downloadDir);

        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    Executor getExecutor() {
        return executor;
    }

    /**
     * raise an local caching process event
     *
     * @param cached source cached object
     */
    void onLocalCachingProcess(final GSCached cached) {

        this.localCachingEvent.raise(cached);
    }

    /**
     * raise an cloud caching process event
     *
     * @param cached source cached object
     */
    void onCloudCachingProcess(final GSCached cached) {
        this.cloudCachingEvent.raise(cached);
    }

    /**
     * raise an memory caching process event
     *
     * @param cached source cached object
     */
    void onMemoryCachingProcess(GSCached cached) {
        this.memoryCachingEvent.raise(cached);
    }

    /**
     * add local caching event listener
     *
     * @param listener new listener
     * @return event slot
     */
    public Slot addLocalCachingListener(EventListener<GSCached> listener) {
        return this.localCachingEvent.connect(listener);
    }

    /**
     * add cloud caching event listener
     *
     * @param listener new listener
     * @return event slot
     */
    public Slot addCloudCachingListener(EventListener<GSCached> listener) {
        return this.cloudCachingEvent.connect(listener);
    }

    /**
     * add memory caching event listener
     *
     * @param listener new listener
     * @return event slot
     */
    public Slot addMemoryCachingListener(EventListener<GSCached> listener) {
        return this.memoryCachingEvent.connect(listener);
    }

    /**
     * get or create cached image from local file path
     *
     * @param file local file path
     * @return local file path
     */
    public GSCachedImage getOrCreateCachedImage(File file) throws FileNotFoundException {

        GSCachedImage cached = (GSCachedImage) this.memoryCached.get(file.toString());

        if (cached == null) {

            cached = new GSCachedImage(this);

            cached.getLocalCached().attachLocalFile(file);

            this.memoryCached.put(file.toString(), cached);
        }

        return cached;
    }

    /**
     * get or create cached image from remote cached url
     */
    public GSCachedImage getOrCreateCachedImage(URL url) {

        GSCachedImage cached = (GSCachedImage) this.memoryCached.get(url.toString());

        if (cached == null) {

            cached = new GSCachedImage(this);

            cached.getCloudCached().attachRemoteURL(url);

            Realm realm = Realm.getInstance(dbConfig);

            try {
                GSCachedMetaData metaData = realm.where(GSCachedMetaData.class)
                        .equalTo("remoteURL",url.toString())
                        .findFirst();

                if(metaData != null && !"".equals(metaData.getLocalPath())){
                    File file = new File(metaData.getLocalPath());

                    if(file.exists()) {
                        cached.getLocalCached().attachLocalFile(file);
                    }
                }

            } finally {
                realm.close();
            }

            this.memoryCached.put(url.toString(), cached);
        }

        return cached;
    }


    /**
     * get cloud object
     */
    GSCloud getCloud() {
        return cloud;
    }

    /**
     * generate download path by remote url
     */
    public File downloadPath(URL url) {
        return new File(downloadDir + UrlSafeBase64.encodeToString(url.toString()));
    }

    public RealmConfiguration dbConfig() {
        return dbConfig;
    }

    public Handler getHandler() {
        return handler;
    }
}
