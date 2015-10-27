package com.gschat;

import java.nio.ByteBuffer;

import com.gsrpc.Writer;

import com.gsrpc.Reader;



/*
 * IMClient generate by gs2java,don't modify it manually
 */
public final class IMClientDispatcher implements com.gsrpc.Dispatcher {

    private IMClient service;

    public IMClientDispatcher(IMClient service) {
        this.service = service;
    }

    public com.gsrpc.Response Dispatch(com.gsrpc.Request call) throws Exception
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
