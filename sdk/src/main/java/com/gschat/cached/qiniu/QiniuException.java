package com.gschat.cached.qiniu;


public class QiniuException extends Exception {

    private int httpResponseCode;

    public QiniuException(String detailMessage, int httpResponseCode) {
        super(detailMessage);
        this.httpResponseCode = httpResponseCode;
    }
}
