package com.github.gschat.gsim;

import com.github.gsdocker.gsrpc.Writer;

import com.github.gsdocker.gsrpc.Reader;

import java.nio.ByteBuffer;






public interface UserMQ {

    void PushMessage(com.github.gschat.gsim.Message arg0) throws Exception;

}

