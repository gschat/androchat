package com.gschat;

import com.gsrpc.Writer;

import com.gsrpc.Reader;

import java.nio.ByteBuffer;



public interface IMServer {

    long Put (Mail mail) throws Exception;

    void Pull (int offset) throws Exception;

}

