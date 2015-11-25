package com.gschat;

import com.gschat.UserAuthFailedException;

import com.gsrpc.Writer;

import com.gsrpc.Reader;

import com.gschat.Mail;

import com.gschat.UnexpectSQIDException;

import com.gschat.ResourceNotFoundException;

import com.gschat.ResourceBusyException;

import java.nio.ByteBuffer;

import com.gschat.UserNotFoundException;

import com.gsrpc.Device;

import com.gsrpc.KV;


/*
 * Client generate by gs2java,don't modify it manually
 */
public final class ClientRPC {

    /**
     * gsrpc net interface
     */
    private com.gsrpc.Channel net;

    /**
     * remote service id
     */
    private short serviceID;

    public ClientRPC(com.gsrpc.Channel net, short serviceID){
        this.net = net;
        this.serviceID = serviceID;
    }

    
    public void push(Mail arg0) throws Exception {

        com.gsrpc.Request request = new com.gsrpc.Request();

        request.setService(this.serviceID);

        request.setMethod((short)0);

        
        com.gsrpc.Param[] params = new com.gsrpc.Param[1];
		{

			com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

			arg0.marshal(writer);

			com.gsrpc.Param param = new com.gsrpc.Param();

			param.setContent(writer.getContent());

			params[0] = (param);

		}


        request.setParams(params);
        

        
        this.net.post(request);
        
    }
    
    public void notify(int arg0) throws Exception {

        com.gsrpc.Request request = new com.gsrpc.Request();

        request.setService(this.serviceID);

        request.setMethod((short)1);

        
        com.gsrpc.Param[] params = new com.gsrpc.Param[1];
		{

			com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

			writer.writeUInt32(arg0);

			com.gsrpc.Param param = new com.gsrpc.Param();

			param.setContent(writer.getContent());

			params[0] = (param);

		}


        request.setParams(params);
        

        
        this.net.post(request);
        
    }
    
    public void deviceStateChanged(Device arg0, boolean arg1) throws Exception {

        com.gsrpc.Request request = new com.gsrpc.Request();

        request.setService(this.serviceID);

        request.setMethod((short)2);

        
        com.gsrpc.Param[] params = new com.gsrpc.Param[2];
		{

			com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

			arg0.marshal(writer);

			com.gsrpc.Param param = new com.gsrpc.Param();

			param.setContent(writer.getContent());

			params[0] = (param);

		}

		{

			com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

			writer.writeBoolean(arg1);

			com.gsrpc.Param param = new com.gsrpc.Param();

			param.setContent(writer.getContent());

			params[1] = (param);

		}


        request.setParams(params);
        

        
        this.net.post(request);
        
    }
    
}
