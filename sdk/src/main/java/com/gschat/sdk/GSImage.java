package com.gschat.sdk;


import com.google.gson.Gson;
import com.gschat.cached.GSCachedImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;

public class GSImage implements GSMessageBody {

    private final static Logger logger = LoggerFactory.getLogger("GSImage");

    public final class GSImageMetaData {
        /**
         * message body type
         */
        private final int messageBodyType = GSContentType.Image.getValue();

        private String thumbnailUrl;

        private String url;

        private int fileLength;

        public int getMessageBodyType() {
            return messageBodyType;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public void setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getFileLength() {
            return fileLength;
        }

        public void setFileLength(int fileLength) {
            this.fileLength = fileLength;
        }
    }

    private final GSImageMetaData metaData;


    private final GSChat gsChat;


    private File localFile;

    /**
     * create GSImage by local file path
     */
    public GSImage(GSChat gsChat, File localFile) {

        this.gsChat = gsChat;

        metaData = new GSImageMetaData();

        this.localFile = localFile;
    }

    public GSImageMetaData getMetaData() {
        return metaData;
    }

    /**
     * create GSImage by image metadata
     */
    protected GSImage(GSChat gsChat, GSImageMetaData metaData) {

        this.metaData = metaData;

        this.gsChat = gsChat;
    }


    public GSCachedImage thumbnail() throws Exception {
        // load local message
        if (localFile != null) {

            return gsChat.getCachedService().getOrCreateCachedImage(this.localFile);

        } else {
            return gsChat.getCachedService().getOrCreateCachedImage(new URL(metaData.getThumbnailUrl()));
        }
    }

    public GSCachedImage bitmap() throws Exception {

        if (this.localFile != null) {
            return gsChat.getCachedService().getOrCreateCachedImage(this.localFile);
        } else {
            return gsChat.getCachedService().getOrCreateCachedImage(new URL(metaData.getUrl()));
        }
    }

    @Override
    public GSContentType getType() {
        return GSContentType.Image;
    }

    @Override
    public String toJson() {

        Gson gson = new Gson();

        return gson.toJson(this.metaData);
    }

    public static GSMessageBody fromJson(GSChat gsChat, String content) {

        Gson gson = new Gson();

        GSImageMetaData metaData = gson.fromJson(content, GSImageMetaData.class);

        return new GSImage(gsChat, metaData);
    }
}
