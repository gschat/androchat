package com.github.gschat.gsim;

import com.github.gsdocker.gsrpc.Writer;

import com.github.gsdocker.gsrpc.Reader;

import java.nio.ByteBuffer;






public interface IMClient {

    void Heartbeat(int arg0) throws Exception;

    void KickOff() throws Exception;

    void RecvMessage(com.github.gschat.gsim.Message arg0) throws Exception;

    void RecvPushMessage(com.github.gschat.gsim.Message arg0) throws Exception;

}

