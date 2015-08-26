package com.github.gschat.gsim;

import com.github.gsdocker.gsrpc.Writer;

import com.github.gsdocker.gsrpc.Reader;

import java.nio.ByteBuffer;






/*
 * UserMQ generate by gs2java,don't modify it manually
 */
public final class UserMQDispatcher implements com.github.gsdocker.gsrpc.Dispatcher {

    private UserMQ service;

    public UserMQDispatcher(UserMQ service) {
        this.service = service;
    }

    public com.github.gsdocker.gsrpc.Return Dispatch(com.github.gsdocker.gsrpc.Call call) throws Exception
    {
        switch(call.getMethod()){

        
        case 0: {
				com.github.gschat.gsim.Message arg0 = new com.github.gschat.gsim.Message();

				{

					com.github.gsdocker.gsrpc.BufferReader reader = new com.github.gsdocker.gsrpc.BufferReader(call.getParams()[0].getContent());

					arg0.Unmarshal(reader);
				}


                service.PushMessage(arg0);
                
                break;
                
            }
        
        }
        return null;
    }
}


