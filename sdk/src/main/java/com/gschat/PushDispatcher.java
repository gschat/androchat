package com.gschat;

import java.nio.ByteBuffer;

import com.gschat.UserNotFoundException;

import com.gsrpc.Device;

import com.gsrpc.KV;

import com.gsrpc.Writer;

import com.gsrpc.Reader;

import com.gschat.Mail;

import com.gschat.UnexpectSQIDException;

import com.gschat.ResourceNotFoundException;

import com.gschat.ResourceBusyException;

import com.gschat.UserAuthFailedException;



/*
 * Push generate by gs2java,don't modify it manually
 */
public final class PushDispatcher implements com.gsrpc.NamedDispatcher {

    private Push service;

    public PushDispatcher(Push service) {
        this.service = service;
    }

    public String name() {
        return "com.gschat.Push";
    }

    public com.gsrpc.Response dispatch(com.gsrpc.Request call) throws Exception
    {
        switch(call.getMethod()){
        
        case 0: {
				byte[] arg0 = new byte[0];

				{

					com.gsrpc.BufferReader reader = new com.gsrpc.BufferReader(call.getParams()[0].getContent());

					arg0 = reader.readBytes();

				}


                
                
                    this.service.register(arg0);

                    com.gsrpc.Response callReturn = new com.gsrpc.Response();
                    callReturn.setID(call.getID());
                    callReturn.setException((byte)-1);

                    

                    return callReturn;

                
                
            }
        
        case 1: {

                
                
                    this.service.unregister();

                    com.gsrpc.Response callReturn = new com.gsrpc.Response();
                    callReturn.setID(call.getID());
                    callReturn.setException((byte)-1);

                    

                    return callReturn;

                
                
            }
        
        }
        return null;
    }
}
