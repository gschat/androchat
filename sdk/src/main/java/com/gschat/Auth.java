package com.gschat;

import com.gsrpc.Reader;

import com.gschat.Mail;

import com.gschat.UnexpectSQIDException;

import com.gschat.ResourceNotFoundException;

import com.gschat.ResourceBusyException;

import com.gschat.UserAuthFailedException;

import com.gsrpc.Writer;

import com.gschat.UserNotFoundException;

import com.gsrpc.Device;

import com.gsrpc.KV;

import java.nio.ByteBuffer;



public interface Auth {
    String NAME = "com.gschat.Auth";

    KV[] login (String username, KV[] properties) throws Exception;

    void logoff (KV[] properties) throws Exception;

}

