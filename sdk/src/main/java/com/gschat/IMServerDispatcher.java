package com.gschat;

import com.gsrpc.Writer;

import com.gsrpc.Reader;

import java.nio.ByteBuffer;



/*
 * IMServer generate by gs2java,don't modify it manually
 */
public final class IMServerDispatcher implements com.gsrpc.Dispatcher {

    private IMServer service;

    public IMServerDispatcher(IMServer service) {
        this.service = service;
    }

    public com.gsrpc.Response Dispatch(com.gsrpc.Request call) throws Exception
    {
        switch(call.getMethod()){
        
        case 0: {
				Mail arg0 = new Mail();

				{

					com.gsrpc.BufferReader reader = new com.gsrpc.BufferReader(call.getParams()[0].getContent());

					arg0.Unmarshal(reader);

				}



                try{
                    long ret = this.service.Put(arg0);

                    com.gsrpc.Response callReturn = new com.gsrpc.Response();
                    callReturn.setID(call.getID());
                    callReturn.setService(call.getService());
                    callReturn.setException((byte)-1);

                    
    				byte[] returnParam;

				{

					com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

					writer.WriteUInt64(ret);

					returnParam = writer.Content();

				}


                    callReturn.setContent(returnParam);
                    

                    return callReturn;

                } catch(Exception e){
                    
                    if(e instanceof UserNotFoundException){

                        com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

                        ((UserNotFoundException)e).Marshal(writer);

                        com.gsrpc.Response callReturn = new com.gsrpc.Response();
                        callReturn.setID(call.getID());
                        callReturn.setService(call.getService());
                        callReturn.setException((byte)0);
                        callReturn.setContent(writer.Content());

                        return callReturn;
                    }
                    
                }
            }
        
        case 1: {
				int arg0 = 0;

				{

					com.gsrpc.BufferReader reader = new com.gsrpc.BufferReader(call.getParams()[0].getContent());

					arg0 = reader.ReadUInt32();

				}



                try{
                    this.service.Pull(arg0);

                    com.gsrpc.Response callReturn = new com.gsrpc.Response();
                    callReturn.setID(call.getID());
                    callReturn.setService(call.getService());
                    callReturn.setException((byte)-1);

                    

                    return callReturn;

                } catch(Exception e){
                    
                    if(e instanceof UserNotFoundException){

                        com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

                        ((UserNotFoundException)e).Marshal(writer);

                        com.gsrpc.Response callReturn = new com.gsrpc.Response();
                        callReturn.setID(call.getID());
                        callReturn.setService(call.getService());
                        callReturn.setException((byte)0);
                        callReturn.setContent(writer.Content());

                        return callReturn;
                    }
                    
                }
            }
        
        }
        return null;
    }
}
