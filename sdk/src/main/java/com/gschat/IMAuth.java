package com.gschat;

import com.gsrpc.Writer;

import com.gsrpc.Reader;

import java.nio.ByteBuffer;



public interface IMAuth {

    Property[] login (String username, Property[] properties) throws Exception;

    void logoff (Property[] properties) throws Exception;

}

