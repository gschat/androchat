package com.gschat;

import com.gsrpc.Writer;

import com.gsrpc.Reader;

import java.nio.ByteBuffer;



public interface IMClient {

    void push (Mail mail) throws Exception;

    void notify (int SQID) throws Exception;

}

