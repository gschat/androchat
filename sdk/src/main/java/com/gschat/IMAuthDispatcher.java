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

					arg0 = reader.readString();

				}

				Property[] arg1 = new Property[0];

				{

					com.gsrpc.BufferReader reader = new com.gsrpc.BufferReader(call.getParams()[1].getContent());

					int max5 = reader.readUInt16();

				arg1 = new Property[max5];

				for(int i5 = 0; i5 < max5; i5 ++ ){

					Property v5 = new Property();

					v5.unmarshal(reader);

					arg1[i5] = v5;

				}

				}


                
                try{
                
                    Property[] ret = this.service.login(arg0, arg1);

                    com.gsrpc.Response callReturn = new com.gsrpc.Response();
                    callReturn.setID(call.getID());
                    callReturn.setService(call.getService());
                    callReturn.setException((byte)-1);

                    
    				byte[] returnParam;

				{

					com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

					writer.writeUInt16((short)ret.length);

				for(Property v5 : ret){

					v5.marshal(writer);

				}

					returnParam = writer.Content();

				}


                    callReturn.setContent(returnParam);
                    

                    return callReturn;

                } catch(UserNotFoundException e) {

                    com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

                    e.marshal(writer);

                    com.gsrpc.Response callReturn = new com.gsrpc.Response();
                    callReturn.setID(call.getID());
                    callReturn.setService(call.getService());
                    callReturn.setException((byte)0);
                    callReturn.setContent(writer.Content());

                    return callReturn;
                } catch(UserAuthFailedException e) {

                    com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

                    e.marshal(writer);

                    com.gsrpc.Response callReturn = new com.gsrpc.Response();
                    callReturn.setID(call.getID());
                    callReturn.setService(call.getService());
                    callReturn.setException((byte)1);
                    callReturn.setContent(writer.Content());

                    return callReturn;
                }
            }
        
        case 1: {
				Property[] arg0 = new Property[0];

				{

					com.gsrpc.BufferReader reader = new com.gsrpc.BufferReader(call.getParams()[0].getContent());

					int max5 = reader.readUInt16();

				arg0 = new Property[max5];

				for(int i5 = 0; i5 < max5; i5 ++ ){

					Property v5 = new Property();

					v5.unmarshal(reader);

					arg0[i5] = v5;

				}

				}


                
                    this.service.logoff(arg0);

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
