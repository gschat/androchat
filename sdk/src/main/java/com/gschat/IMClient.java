package com.gschat;

import com.gsrpc.Writer;

import com.gsrpc.Reader;

import java.nio.ByteBuffer;



public interface IMClient {

    void Push (Mail mail) throws Exception;

    void Notify (int SQID) throws Exception;

}
