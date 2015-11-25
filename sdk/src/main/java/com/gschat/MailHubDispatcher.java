package com.gschat;

import com.gschat.UnexpectSQIDException;

import com.gschat.ResourceNotFoundException;

import com.gschat.ResourceBusyException;

import com.gschat.UserAuthFailedException;

import com.gsrpc.Writer;

import com.gsrpc.Reader;

import com.gschat.Mail;

import com.gsrpc.KV;

import java.nio.ByteBuffer;

import com.gschat.UserNotFoundException;

import com.gsrpc.Device;



/*
 * MailHub generate by gs2java,don't modify it manually
 */
public final class MailHubDispatcher implements com.gsrpc.NamedDispatcher {

    private MailHub service;

    public MailHubDispatcher(MailHub service) {
        this.service = service;
    }

    public String name() {
        return "com.gschat.MailHub";
    }

    public com.gsrpc.Response dispatch(com.gsrpc.Request call) throws Exception
    {
        switch(call.getMethod()){
        
        case 0: {

                
                
                    int ret = this.service.putSync();

                    com.gsrpc.Response callReturn = new com.gsrpc.Response();
                    callReturn.setID(call.getID());
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
                    callReturn.setException((byte)0);
                    callReturn.setContent(writer.getContent());

                    return callReturn;
                } catch(UnexpectSQIDException e) {

                    com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

                    e.marshal(writer);

                    com.gsrpc.Response callReturn = new com.gsrpc.Response();
                    callReturn.setID(call.getID());
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
                
                    Sync ret = this.service.sync(arg0, arg1);

                    com.gsrpc.Response callReturn = new com.gsrpc.Response();
                    callReturn.setID(call.getID());
                    callReturn.setException((byte)-1);

                    
    				byte[] returnParam;

				{

					com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

					ret.marshal(writer);

					returnParam = writer.getContent();

				}


                    callReturn.setContent(returnParam);
                    

                    return callReturn;

                } catch(UserNotFoundException e) {

                    com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

                    e.marshal(writer);

                    com.gsrpc.Response callReturn = new com.gsrpc.Response();
                    callReturn.setID(call.getID());
                    callReturn.setException((byte)0);
                    callReturn.setContent(writer.getContent());

                    return callReturn;
                } catch(ResourceNotFoundException e) {

                    com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

                    e.marshal(writer);

                    com.gsrpc.Response callReturn = new com.gsrpc.Response();
                    callReturn.setID(call.getID());
                    callReturn.setException((byte)1);
                    callReturn.setContent(writer.getContent());

                    return callReturn;
                } catch(ResourceBusyException e) {

                    com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

                    e.marshal(writer);

                    com.gsrpc.Response callReturn = new com.gsrpc.Response();
                    callReturn.setID(call.getID());
                    callReturn.setException((byte)2);
                    callReturn.setContent(writer.getContent());

                    return callReturn;
                }
                
            }
        
        case 3: {
				int arg0 = 0;

				{

					com.gsrpc.BufferReader reader = new com.gsrpc.BufferReader(call.getParams()[0].getContent());

					arg0 = reader.readUInt32();

				}


                
                
                    this.service.fin(arg0);

                    com.gsrpc.Response callReturn = new com.gsrpc.Response();
                    callReturn.setID(call.getID());
                    callReturn.setException((byte)-1);

                    

                    return callReturn;

                
                
            }
        
        case 4: {
				int[] arg0 = new int[0];

				{

					com.gsrpc.BufferReader reader = new com.gsrpc.BufferReader(call.getParams()[0].getContent());

					int max5 = reader.readUInt16();

				arg0 = new int[max5];

				for(int i5 = 0; i5 < max5; i5 ++ ){

					int v5 = 0;

					v5 = reader.readUInt32();

					arg0[i5] = v5;

				}

				}


                
                
                try{
                
                    Mail[] ret = this.service.fetch(arg0);

                    com.gsrpc.Response callReturn = new com.gsrpc.Response();
                    callReturn.setID(call.getID());
                    callReturn.setException((byte)-1);

                    
    				byte[] returnParam;

				{

					com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

					writer.writeUInt16((short)ret.length);

				for(Mail v5 : ret){

					v5.marshal(writer);

				}

					returnParam = writer.getContent();

				}


                    callReturn.setContent(returnParam);
                    

                    return callReturn;

                } catch(ResourceNotFoundException e) {

                    com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

                    e.marshal(writer);

                    com.gsrpc.Response callReturn = new com.gsrpc.Response();
                    callReturn.setID(call.getID());
                    callReturn.setException((byte)0);
                    callReturn.setContent(writer.getContent());

                    return callReturn;
                }
                
            }
        
        }
        return null;
    }
}
