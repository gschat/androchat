package com.github.gsdocker.gsrpc;


/**
 * Created by liyang on 15/5/25.
 */
public interface CompleteHandler {
    void Complete(Exception e, Object ... args);
}
