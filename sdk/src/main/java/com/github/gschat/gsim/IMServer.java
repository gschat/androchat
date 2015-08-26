package com.github.gschat.gsim;

import com.github.gsdocker.gsrpc.Writer;

import com.github.gsdocker.gsrpc.Reader;

import java.nio.ByteBuffer;






public interface IMServer {

    int Heartbeat(int arg0) throws Exception;

    void Poll(byte[] arg0, int arg1) throws Exception;

    boolean ResendMessage(int arg0) throws Exception;

    long SendMessage(com.github.gschat.gsim.Message arg0) throws Exception;

}

