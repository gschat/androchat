package com.gschat;

import com.gsrpc.Reader;

import java.nio.ByteBuffer;

import com.gsrpc.Writer;



public interface IMClient {

    void Push (Mail mail) throws Exception;

    void Notify (int SQID) throws Exception;

}

