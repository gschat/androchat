package com.github.gsdocker.gsrpc;

/**
 * Created by liyang on 15/5/25.
 */
public interface Net {
    void send(com.github.gsdocker.gsrpc.Call call,Callback callback) throws Exception;
}
