package com.gschat;

import java.nio.ByteBuffer;

import com.gschat.UserNotFoundException;

import com.gsrpc.Device;

import com.gsrpc.KV;

import com.gschat.UserAuthFailedException;

import com.gsrpc.Writer;

import com.gsrpc.Reader;

import com.gschat.Mail;

import com.gschat.UnexpectSQIDException;

import com.gschat.ResourceNotFoundException;

import com.gschat.ResourceBusyException;


/*
 * Push generate by gs2java,don't modify it manually
 */
public final class PushRPC {

    /**
     * gsrpc net interface
     */
    private com.gsrpc.Channel net;

    /**
     * remote service id
     */
    private short serviceID;

    public PushRPC(com.gsrpc.Channel net, short serviceID){
        this.net = net;
        this.serviceID = serviceID;
    }

    
    public com.gsrpc.Future<Void> register(byte[] arg0, final int timeout) throws Exception {

        com.gsrpc.Request request = new com.gsrpc.Request();

        request.setService(this.serviceID);

        request.setMethod((short)0);

        
        com.gsrpc.Param[] params = new com.gsrpc.Param[1];
		{

			com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

			writer.writeBytes(arg0);

			com.gsrpc.Param param = new com.gsrpc.Param();

			param.setContent(writer.getContent());

			params[0] = (param);

		}


        request.setParams(params);
        

        
        com.gsrpc.Promise<Void> promise = new com.gsrpc.Promise<Void>(timeout){
            @Override
            public void Return(Exception e,com.gsrpc.Response callReturn){

                if (e != null) {
                    Notify(e,null);
                    return;
                }

                try{

                    if(callReturn.getException() != (byte)-1) {
                        switch(callReturn.getException()) {
                            
                        default:
                            Notify(new com.gsrpc.RemoteException(),null);
                            return;
                        }
                    }

                    
                    Notify(null,null);
                    
                }catch(Exception e1) {
                    Notify(e1,null);
                }
            }
        };

        this.net.send(request,promise);

        return promise;
        
    }
    
    public com.gsrpc.Future<Void> unregister(final int timeout) throws Exception {

        com.gsrpc.Request request = new com.gsrpc.Request();

        request.setService(this.serviceID);

        request.setMethod((short)1);

        

        
        com.gsrpc.Promise<Void> promise = new com.gsrpc.Promise<Void>(timeout){
            @Override
            public void Return(Exception e,com.gsrpc.Response callReturn){

                if (e != null) {
                    Notify(e,null);
                    return;
                }

                try{

                    if(callReturn.getException() != (byte)-1) {
                        switch(callReturn.getException()) {
                            
                        default:
                            Notify(new com.gsrpc.RemoteException(),null);
                            return;
                        }
                    }

                    
                    Notify(null,null);
                    
                }catch(Exception e1) {
                    Notify(e1,null);
                }
            }
        };

        this.net.send(request,promise);

        return promise;
        
    }
    
}
