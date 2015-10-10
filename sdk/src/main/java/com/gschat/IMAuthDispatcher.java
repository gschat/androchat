package com.gschat;

import com.gsrpc.Writer;

import com.gsrpc.Reader;

import java.nio.ByteBuffer;



/*
 * IMAuth generate by gs2java,don't modify it manually
 */
public final class IMAuthDispatcher implements com.gsrpc.Dispatcher {

    private IMAuth service;

    public IMAuthDispatcher(IMAuth service) {
        this.service = service;
    }

    public com.gsrpc.Response Dispatch(com.gsrpc.Request call) throws Exception
    {
        switch(call.getMethod()){
        
        case 0: {
				String arg0 = "";

				{

					com.gsrpc.BufferReader reader = new com.gsrpc.BufferReader(call.getParams()[0].getContent());

					arg0 = reader.ReadString();

				}

				Property[] arg1 = new Property[0];

				{

					com.gsrpc.BufferReader reader = new com.gsrpc.BufferReader(call.getParams()[1].getContent());

					int max5 = reader.ReadUInt16();

				arg1 = new Property[max5];

				for(int i5 = 0; i5 < max5; i5 ++ ){

					Property v5 = new Property();

					v5.Unmarshal(reader);

					arg1[i5] = v5;

				}

				}



                try{
                    Property[] ret = this.service.Login(arg0, arg1);

                    com.gsrpc.Response callReturn = new com.gsrpc.Response();
                    callReturn.setID(call.getID());
                    callReturn.setService(call.getService());
                    callReturn.setException((byte)-1);

                    
    				byte[] returnParam;

				{

					com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

					writer.WriteUInt16((short)ret.length);

				for(Property v5 : ret){

					v5.Marshal(writer);

				}

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
                    
                    if(e instanceof UserAuthFailedException){

                        com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

                        ((UserAuthFailedException)e).Marshal(writer);

                        com.gsrpc.Response callReturn = new com.gsrpc.Response();
                        callReturn.setID(call.getID());
                        callReturn.setService(call.getService());
                        callReturn.setException((byte)1);
                        callReturn.setContent(writer.Content());

                        return callReturn;
                    }
                    
                }
            }
        
        case 1: {
				Property[] arg0 = new Property[0];

				{

					com.gsrpc.BufferReader reader = new com.gsrpc.BufferReader(call.getParams()[0].getContent());

					int max5 = reader.ReadUInt16();

				arg0 = new Property[max5];

				for(int i5 = 0; i5 < max5; i5 ++ ){

					Property v5 = new Property();

					v5.Unmarshal(reader);

					arg0[i5] = v5;

				}

				}



                try{
                    this.service.Logoff(arg0);

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
