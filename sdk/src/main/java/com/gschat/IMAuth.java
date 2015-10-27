package com.gschat;

import java.nio.ByteBuffer;

import com.gsrpc.Writer;

import com.gsrpc.Reader;



public interface IMAuth {

    Property[] login (String username, Property[] properties) throws Exception;

    void logoff (Property[] properties) throws Exception;

}

