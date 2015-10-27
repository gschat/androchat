package com.gschat;

import java.nio.ByteBuffer;

import com.gsrpc.Writer;

import com.gsrpc.Reader;



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

                
                
                    int ret = this.service.prepare();

                    com.gsrpc.Response callReturn = new com.gsrpc.Response();
                    callReturn.setID(call.getID());
                    callReturn.setService(call.getService());
                    callReturn.setException((byte)-1);

                    
    				byte[] returnParam;

				{

					com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

					writer.writeUInt32(ret);

					returnParam = writer.getContent();

				}


                    callReturn.setContent(returnParam);
                    

                    return callReturn;

                
                
            }
        
        case 1: {
				Mail arg0 = new Mail();

				{

					com.gsrpc.BufferReader reader = new com.gsrpc.BufferReader(call.getParams()[0].getContent());

					arg0.unmarshal(reader);

				}


                
                
                try{
                
                    long ret = this.service.put(arg0);

                    com.gsrpc.Response callReturn = new com.gsrpc.Response();
                    callReturn.setID(call.getID());
                    callReturn.setService(call.getService());
                    callReturn.setException((byte)-1);

                    
    				byte[] returnParam;

				{

					com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

					writer.writeUInt64(ret);

					returnParam = writer.getContent();

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
                    callReturn.setContent(writer.getContent());

                    return callReturn;
                } catch(UnexpectSQIDException e) {

                    com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

                    e.marshal(writer);

                    com.gsrpc.Response callReturn = new com.gsrpc.Response();
                    callReturn.setID(call.getID());
                    callReturn.setService(call.getService());
                    callReturn.setException((byte)1);
                    callReturn.setContent(writer.getContent());

                    return callReturn;
                }
                
            }
        
        case 2: {
				int arg0 = 0;

				{

					com.gsrpc.BufferReader reader = new com.gsrpc.BufferReader(call.getParams()[0].getContent());

					arg0 = reader.readUInt32();

				}

				int arg1 = 0;

				{

					com.gsrpc.BufferReader reader = new com.gsrpc.BufferReader(call.getParams()[1].getContent());

					arg1 = reader.readUInt32();

				}


                
                
                try{
                
                    int ret = this.service.sync(arg0, arg1);

                    com.gsrpc.Response callReturn = new com.gsrpc.Response();
                    callReturn.setID(call.getID());
                    callReturn.setService(call.getService());
                    callReturn.setException((byte)-1);

                    
    				byte[] returnParam;

				{

					com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

					writer.writeUInt32(ret);

					returnParam = writer.getContent();

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
                    callReturn.setContent(writer.getContent());

                    return callReturn;
                }
                
            }
        
        }
        return null;
    }
}
