package com.gschat.cached;


import android.os.Handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.concurrent.Executor;

import io.realm.Realm;

public abstract class GSCached {

    private final static Logger logger = LoggerFactory.getLogger("GSCached");
    /**
     * cache service
     */
    private final GSCachedService service;

    private final GSLocalCached localCached;

    private final GSCloudCached cloudCached;

    /**
     * create new cached object
     *
     */
    protected GSCached(GSCachedService service) {
        this.service = service;

        this.localCached = new GSLocalCached(this);

        this.cloudCached = new GSCloudCached(this);
    }

    /**
     * get cached memory size
     *
     * @return cached memory object size
     */
    abstract int sizeOf();


    void saveMetadata() {

        Realm realm = Realm.getInstance(service.dbConfig());

        try {

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    GSCachedMetaData metaData = new GSCachedMetaData();

                    metaData.setLocalPath(localCached.getLocalFile().toString());

                    metaData.setRemoteURL(cloudCached.getRemoteURL().toString());

                    realm.copyToRealmOrUpdate(metaData);
                }
            });

        } finally {
            realm.close();
        }
    }

    public GSLocalCached getLocalCached() {
        return localCached;
    }

    public GSCloudCached getCloudCached() {
        return cloudCached;
    }

    /**
     * get async executor
     */
    protected Executor getExecutor() {
        return this.service.getExecutor();
    }

    protected Handler getHandler() {
        return this.service.getHandler();
    }


    protected boolean isImage() {
        return false;
    }

    protected void download(GSLocalCached gsLocalCached) {

        URL remoteURL = cloudCached.getRemoteURL();

        service.getCloud().download(remoteURL, service.downloadPath(remoteURL), gsLocalCached);
    }

    protected void upload(GSCloudCached gsCloudCached,GSCloudUploader cloudUploader) {

        service.getCloud().upload(isImage(), localCached.getLocalFile(), gsCloudCached, cloudUploader);
    }
}
