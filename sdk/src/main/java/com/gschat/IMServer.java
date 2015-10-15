package com.gschat;

import com.gsrpc.Reader;

import java.nio.ByteBuffer;

import com.gsrpc.Writer;



public interface IMServer {

    long Put (Mail mail) throws Exception;

    void Pull (int offset) throws Exception;

}

