package com.gschat.cached;


import java.net.URL;

public interface GSCloudUploader {
    void onSuccess(int size, URL original, URL thumbnail);
}
