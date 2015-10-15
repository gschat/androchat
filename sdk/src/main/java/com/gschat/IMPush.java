package com.gschat;

import com.gsrpc.Reader;

import java.nio.ByteBuffer;

import com.gsrpc.Writer;



public interface IMPush {

    void Register (byte[] pushToken) throws Exception;

    void Unregister () throws Exception;

}

