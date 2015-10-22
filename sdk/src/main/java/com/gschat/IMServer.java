package com.gschat;

import com.gsrpc.Writer;

import com.gsrpc.Reader;

import java.nio.ByteBuffer;



public interface IMServer {

    int prepare () throws Exception;

    long put (Mail mail) throws Exception;

    void pull (int offset) throws Exception;

}

