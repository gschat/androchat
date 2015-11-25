package com.gschat;

import com.gsrpc.Writer;

import com.gsrpc.Reader;

import com.gschat.Mail;

import com.gschat.UnexpectSQIDException;

import com.gschat.ResourceNotFoundException;

import com.gschat.ResourceBusyException;

import com.gschat.UserAuthFailedException;

import java.nio.ByteBuffer;

import com.gschat.UserNotFoundException;

import com.gsrpc.Device;

import com.gsrpc.KV;



/*
 * Client generate by gs2java,don't modify it manually
 */
public final class ClientDispatcher implements com.gsrpc.NamedDispatcher {

    private Client service;

    public ClientDispatcher(Client service) {
        this.service = service;
    }

    public String name() {
        return "com.gschat.Client";
    }

    public com.gsrpc.Response dispatch(com.gsrpc.Request call) throws Exception
    {
        switch(call.getMethod()){
        
        case 0: {
				Mail arg0 = new Mail();

				{

					com.gsrpc.BufferReader reader = new com.gsrpc.BufferReader(call.getParams()[0].getContent());

					arg0.unmarshal(reader);

				}


                
                this.service.push(arg0);
                
            }
        
        case 1: {
				int arg0 = 0;

				{

					com.gsrpc.BufferReader reader = new com.gsrpc.BufferReader(call.getParams()[0].getContent());

					arg0 = reader.readUInt32();

				}


                
                this.service.notify(arg0);
                
            }
        
        case 2: {
				Device arg0 = new Device();

				{

					com.gsrpc.BufferReader reader = new com.gsrpc.BufferReader(call.getParams()[0].getContent());

					arg0.unmarshal(reader);

				}

				boolean arg1 = false;

				{

					com.gsrpc.BufferReader reader = new com.gsrpc.BufferReader(call.getParams()[1].getContent());

					arg1 = reader.readBoolean();

				}


                
                this.service.deviceStateChanged(arg0, arg1);
                
            }
        
        }
        return null;
    }
}
