package com.gschat;

import com.gsrpc.Writer;

import com.gsrpc.Reader;

import java.nio.ByteBuffer;



public interface IMPush {

    void Register (byte[] pushToken) throws Exception;

    void Unregister () throws Exception;

}

