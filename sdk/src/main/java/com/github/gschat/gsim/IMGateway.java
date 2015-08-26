package com.github.gschat.gsim;

import com.github.gsdocker.gsrpc.Writer;

import com.github.gsdocker.gsrpc.Reader;

import java.nio.ByteBuffer;






public interface IMGateway {

    void FastLogin(byte[] arg0) throws Exception;

    byte[] Login(String arg0) throws Exception;

    void Logoff(byte[] arg0) throws Exception;

}

