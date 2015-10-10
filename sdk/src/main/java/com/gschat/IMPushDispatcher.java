package com.gschat;

import com.gsrpc.Writer;

import com.gsrpc.Reader;

import java.nio.ByteBuffer;



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

					arg0 = reader.ReadBytes();

				}



                try{
                    this.service.Register(arg0);

                    com.gsrpc.Response callReturn = new com.gsrpc.Response();
                    callReturn.setID(call.getID());
                    callReturn.setService(call.getService());
                    callReturn.setException((byte)-1);

                    

                    return callReturn;

                } catch(Exception e){
                    
                }
            }
        
        case 1: {


                try{
                    this.service.Unregister();

                    com.gsrpc.Response callReturn = new com.gsrpc.Response();
                    callReturn.setID(call.getID());
                    callReturn.setService(call.getService());
                    callReturn.setException((byte)-1);

                    

                    return callReturn;

                } catch(Exception e){
                    
                }
            }
        
        }
        return null;
    }
}
