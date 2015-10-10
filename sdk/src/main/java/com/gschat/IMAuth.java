package com.gschat;

import com.gsrpc.Writer;

import com.gsrpc.Reader;

import java.nio.ByteBuffer;



public interface IMAuth {

    Property[] Login (String username, Property[] properties) throws Exception;

    void Logoff (Property[] properties) throws Exception;

}

