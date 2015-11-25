package com.gschat;

import com.gsrpc.KV;

import java.nio.ByteBuffer;

import com.gschat.UserNotFoundException;

import com.gsrpc.Device;

import com.gschat.UnexpectSQIDException;

import com.gschat.ResourceNotFoundException;

import com.gschat.ResourceBusyException;

import com.gschat.UserAuthFailedException;

import com.gsrpc.Writer;

import com.gsrpc.Reader;

import com.gschat.Mail;



/*
 * Auth generate by gs2java,don't modify it manually
 */
public final class AuthDispatcher implements com.gsrpc.NamedDispatcher {

    private Auth service;

    public AuthDispatcher(Auth service) {
        this.service = service;
    }

    public String name() {
        return "com.gschat.Auth";
    }

    public com.gsrpc.Response dispatch(com.gsrpc.Request call) throws Exception
    {
        switch(call.getMethod()){
        
        case 0: {
				String arg0 = "";

				{

					com.gsrpc.BufferReader reader = new com.gsrpc.BufferReader(call.getParams()[0].getContent());

					arg0 = reader.readString();

				}

				KV[] arg1 = new KV[0];

				{

					com.gsrpc.BufferReader reader = new com.gsrpc.BufferReader(call.getParams()[1].getContent());

					int max5 = reader.readUInt16();

				arg1 = new KV[max5];

				for(int i5 = 0; i5 < max5; i5 ++ ){

					KV v5 = new KV();

					v5.unmarshal(reader);

					arg1[i5] = v5;

				}

				}


                
                
                try{
                
                    KV[] ret = this.service.login(arg0, arg1);

                    com.gsrpc.Response callReturn = new com.gsrpc.Response();
                    callReturn.setID(call.getID());
                    callReturn.setException((byte)-1);

                    
    				byte[] returnParam;

				{

					com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

					writer.writeUInt16((short)ret.length);

				for(KV v5 : ret){

					v5.marshal(writer);

				}

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
                } catch(UserAuthFailedException e) {

                    com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

                    e.marshal(writer);

                    com.gsrpc.Response callReturn = new com.gsrpc.Response();
                    callReturn.setID(call.getID());
                    callReturn.setException((byte)1);
                    callReturn.setContent(writer.getContent());

                    return callReturn;
                } catch(ResourceNotFoundException e) {

                    com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

                    e.marshal(writer);

                    com.gsrpc.Response callReturn = new com.gsrpc.Response();
                    callReturn.setID(call.getID());
                    callReturn.setException((byte)2);
                    callReturn.setContent(writer.getContent());

                    return callReturn;
                }
                
            }
        
        case 1: {
				KV[] arg0 = new KV[0];

				{

					com.gsrpc.BufferReader reader = new com.gsrpc.BufferReader(call.getParams()[0].getContent());

					int max5 = reader.readUInt16();

				arg0 = new KV[max5];

				for(int i5 = 0; i5 < max5; i5 ++ ){

					KV v5 = new KV();

					v5.unmarshal(reader);

					arg0[i5] = v5;

				}

				}


                
                
                    this.service.logoff(arg0);

                    com.gsrpc.Response callReturn = new com.gsrpc.Response();
                    callReturn.setID(call.getID());
                    callReturn.setException((byte)-1);

                    

                    return callReturn;

                
                
            }
        
        }
        return null;
    }
}
