package com.gschat.cached;


import java.io.File;
import java.net.URL;
import java.util.concurrent.Executor;

public interface GSCloud {

    /**
     * upload image
     * @param file local cached object host file path
     * @param cached
     */
    void upload(boolean image,File file, GSCloudCached cached, GSCloudUploader uploader);


    /**
     * download image
     * @param url cloud cached object url
     * @param cached object
     */
    void download(URL url,File localFile,GSLocalCached cached);


    /**
     * set async method executor
     * @param executor
     */
    void setExecutor(Executor executor);
}
