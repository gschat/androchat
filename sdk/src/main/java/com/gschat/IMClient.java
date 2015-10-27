package com.gschat;

import java.nio.ByteBuffer;

import com.gsrpc.Writer;

import com.gsrpc.Reader;



public interface IMClient {

    void push (Mail mail) throws Exception;

    void notify (int SQID) throws Exception;

}

