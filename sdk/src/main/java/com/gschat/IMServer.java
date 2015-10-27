package com.gschat;

import java.nio.ByteBuffer;

import com.gsrpc.Writer;

import com.gsrpc.Reader;



public interface IMServer {

    int prepare () throws Exception;

    long put (Mail mail) throws Exception;

    int sync (int offset, int count) throws Exception;

}

