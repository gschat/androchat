package com.gschat;

import java.nio.ByteBuffer;

import com.gsrpc.Writer;

import com.gsrpc.Reader;



/*
 * IMPush generate by gs2java,don't modify it manually
 */
public final class IMPushDispatcher implements com.gsrpc.Dispatcher {

    private IMPush service;

    public IMPushDispatcher(IMPush service) {
        this.service = service;
    }

    public com.gsrpc.Response Dispatch(com.gsrpc.Request call) throws Exception
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
                    callReturn.setService(call.getService());
                    callReturn.setException((byte)-1);

                    

                    return callReturn;

                
            }
        
        case 1: {

                
                    this.service.unregister();

                    com.gsrpc.Response callReturn = new com.gsrpc.Response();
                    callReturn.setID(call.getID());
                    callReturn.setService(call.getService());
                    callReturn.setException((byte)-1);

                    

                    return callReturn;

                
            }
        
        }
        return null;
    }
}
