package com.gschat;

import com.gsrpc.Reader;

import java.nio.ByteBuffer;

import com.gsrpc.Writer;



public interface IMPush {

    void register (byte[] pushToken) throws Exception;

    void unregister () throws Exception;

}

