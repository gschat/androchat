package com.github.gsdocker.gsrpc;

/**
 * Created by liyang on 15/5/25.
 */
public interface Dispatcher {
    com.github.gsdocker.gsrpc.Return Dispatch(com.github.gsdocker.gsrpc.Call call) throws Exception;
}
