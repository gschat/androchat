package com.gschat;

import com.gsrpc.Writer;

import com.gsrpc.Reader;

import java.nio.ByteBuffer;



public interface IMPush {

    void register (byte[] pushToken) throws Exception;

    void unregister () throws Exception;

}

