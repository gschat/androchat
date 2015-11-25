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



public interface Client {
    String NAME = "com.gschat.Client";

    void push (Mail mail) throws Exception;

    void notify (int SQID) throws Exception;

    void deviceStateChanged (Device device, boolean online) throws Exception;

}

