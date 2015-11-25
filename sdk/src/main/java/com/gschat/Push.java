package com.gschat;

import com.gschat.UnexpectSQIDException;

import com.gschat.ResourceNotFoundException;

import com.gschat.ResourceBusyException;

import com.gschat.UserAuthFailedException;

import com.gsrpc.Writer;

import com.gsrpc.Reader;

import com.gschat.Mail;

import com.gsrpc.KV;

import java.nio.ByteBuffer;

import com.gschat.UserNotFoundException;

import com.gsrpc.Device;



public interface Push {
    String NAME = "com.gschat.Push";

    void register (byte[] pushToken) throws Exception;

    void unregister () throws Exception;

}

