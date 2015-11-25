package com.gschat;

import com.gsrpc.Device;

import com.gsrpc.KV;

import java.nio.ByteBuffer;

import com.gschat.UserNotFoundException;

import com.gschat.Mail;

import com.gschat.UnexpectSQIDException;

import com.gschat.ResourceNotFoundException;

import com.gschat.ResourceBusyException;

import com.gschat.UserAuthFailedException;

import com.gsrpc.Writer;

import com.gsrpc.Reader;



public interface MailHub {
    String NAME = "com.gschat.MailHub";

    int putSync () throws Exception;

    long put (Mail mail) throws Exception;

    Sync sync (int offset, int count) throws Exception;

    void fin (int offset) throws Exception;

    Mail[] fetch (int[] mailIDs) throws Exception;

}

