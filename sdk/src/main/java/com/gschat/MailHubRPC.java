package com.gschat;

import java.nio.ByteBuffer;

import com.gschat.UserNotFoundException;

import com.gsrpc.Device;

import com.gsrpc.KV;

import com.gsrpc.Writer;

import com.gsrpc.Reader;

import com.gschat.Mail;

import com.gschat.UnexpectSQIDException;

import com.gschat.ResourceNotFoundException;

import com.gschat.ResourceBusyException;

import com.gschat.UserAuthFailedException;


/*
 * MailHub generate by gs2java,don't modify it manually
 */
public final class MailHubRPC {

    /**
     * gsrpc net interface
     */
    private com.gsrpc.Channel net;

    /**
     * remote service id
     */
    private short serviceID;

    public MailHubRPC(com.gsrpc.Channel net, short serviceID){
        this.net = net;
        this.serviceID = serviceID;
    }

    
    public com.gsrpc.Future<Integer> putSync(final int timeout) throws Exception {

        com.gsrpc.Request request = new com.gsrpc.Request();

        request.setService(this.serviceID);

        request.setMethod((short)0);

        

        
        com.gsrpc.Promise<Integer> promise = new com.gsrpc.Promise<Integer>(timeout){
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

                    
					int returnParam = 0;

					{

						com.gsrpc.BufferReader reader = new com.gsrpc.BufferReader(callReturn.getContent());

						returnParam = reader.readUInt32();

					}


                    Notify(null,returnParam);
                    
                }catch(Exception e1) {
                    Notify(e1,null);
                }
            }
        };

        this.net.send(request,promise);

        return promise;
        
    }
    
    public com.gsrpc.Future<Long> put(Mail arg0, final int timeout) throws Exception {

        com.gsrpc.Request request = new com.gsrpc.Request();

        request.setService(this.serviceID);

        request.setMethod((short)1);

        
        com.gsrpc.Param[] params = new com.gsrpc.Param[1];
		{

			com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

			arg0.marshal(writer);

			com.gsrpc.Param param = new com.gsrpc.Param();

			param.setContent(writer.getContent());

			params[0] = (param);

		}


        request.setParams(params);
        

        
        com.gsrpc.Promise<Long> promise = new com.gsrpc.Promise<Long>(timeout){
            @Override
            public void Return(Exception e,com.gsrpc.Response callReturn){

                if (e != null) {
                    Notify(e,null);
                    return;
                }

                try{

                    if(callReturn.getException() != (byte)-1) {
                        switch(callReturn.getException()) {
                            
                            case 0:{
                            com.gsrpc.BufferReader reader = new com.gsrpc.BufferReader(callReturn.getContent());

                            UserNotFoundException exception = new UserNotFoundException();

                            exception.unmarshal(reader);

                            Notify(exception,null);

                            return;
                        }
                        
                            case 1:{
                            com.gsrpc.BufferReader reader = new com.gsrpc.BufferReader(callReturn.getContent());

                            UnexpectSQIDException exception = new UnexpectSQIDException();

                            exception.unmarshal(reader);

                            Notify(exception,null);

                            return;
                        }
                        
                        default:
                            Notify(new com.gsrpc.RemoteException(),null);
                            return;
                        }
                    }

                    
					long returnParam = 0;

					{

						com.gsrpc.BufferReader reader = new com.gsrpc.BufferReader(callReturn.getContent());

						returnParam = reader.readUInt64();

					}


                    Notify(null,returnParam);
                    
                }catch(Exception e1) {
                    Notify(e1,null);
                }
            }
        };

        this.net.send(request,promise);

        return promise;
        
    }
    
    public com.gsrpc.Future<Sync> sync(int arg0, int arg1, final int timeout) throws Exception {

        com.gsrpc.Request request = new com.gsrpc.Request();

        request.setService(this.serviceID);

        request.setMethod((short)2);

        
        com.gsrpc.Param[] params = new com.gsrpc.Param[2];
		{

			com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

			writer.writeUInt32(arg0);

			com.gsrpc.Param param = new com.gsrpc.Param();

			param.setContent(writer.getContent());

			params[0] = (param);

		}

		{

			com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

			writer.writeUInt32(arg1);

			com.gsrpc.Param param = new com.gsrpc.Param();

			param.setContent(writer.getContent());

			params[1] = (param);

		}


        request.setParams(params);
        

        
        com.gsrpc.Promise<Sync> promise = new com.gsrpc.Promise<Sync>(timeout){
            @Override
            public void Return(Exception e,com.gsrpc.Response callReturn){

                if (e != null) {
                    Notify(e,null);
                    return;
                }

                try{

                    if(callReturn.getException() != (byte)-1) {
                        switch(callReturn.getException()) {
                            
                            case 0:{
                            com.gsrpc.BufferReader reader = new com.gsrpc.BufferReader(callReturn.getContent());

                            UserNotFoundException exception = new UserNotFoundException();

                            exception.unmarshal(reader);

                            Notify(exception,null);

                            return;
                        }
                        
                            case 1:{
                            com.gsrpc.BufferReader reader = new com.gsrpc.BufferReader(callReturn.getContent());

                            ResourceNotFoundException exception = new ResourceNotFoundException();

                            exception.unmarshal(reader);

                            Notify(exception,null);

                            return;
                        }
                        
                            case 2:{
                            com.gsrpc.BufferReader reader = new com.gsrpc.BufferReader(callReturn.getContent());

                            ResourceBusyException exception = new ResourceBusyException();

                            exception.unmarshal(reader);

                            Notify(exception,null);

                            return;
                        }
                        
                        default:
                            Notify(new com.gsrpc.RemoteException(),null);
                            return;
                        }
                    }

                    
					Sync returnParam = new Sync();

					{

						com.gsrpc.BufferReader reader = new com.gsrpc.BufferReader(callReturn.getContent());

						returnParam.unmarshal(reader);

					}


                    Notify(null,returnParam);
                    
                }catch(Exception e1) {
                    Notify(e1,null);
                }
            }
        };

        this.net.send(request,promise);

        return promise;
        
    }
    
    public com.gsrpc.Future<Void> fin(int arg0, final int timeout) throws Exception {

        com.gsrpc.Request request = new com.gsrpc.Request();

        request.setService(this.serviceID);

        request.setMethod((short)3);

        
        com.gsrpc.Param[] params = new com.gsrpc.Param[1];
		{

			com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

			writer.writeUInt32(arg0);

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
    
    public com.gsrpc.Future<Mail[]> fetch(int[] arg0, final int timeout) throws Exception {

        com.gsrpc.Request request = new com.gsrpc.Request();

        request.setService(this.serviceID);

        request.setMethod((short)4);

        
        com.gsrpc.Param[] params = new com.gsrpc.Param[1];
		{

			com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

			writer.writeUInt16((short)arg0.length);

		for(int v3 : arg0){

			writer.writeUInt32(v3);

		}

			com.gsrpc.Param param = new com.gsrpc.Param();

			param.setContent(writer.getContent());

			params[0] = (param);

		}


        request.setParams(params);
        

        
        com.gsrpc.Promise<Mail[]> promise = new com.gsrpc.Promise<Mail[]>(timeout){
            @Override
            public void Return(Exception e,com.gsrpc.Response callReturn){

                if (e != null) {
                    Notify(e,null);
                    return;
                }

                try{

                    if(callReturn.getException() != (byte)-1) {
                        switch(callReturn.getException()) {
                            
                            case 0:{
                            com.gsrpc.BufferReader reader = new com.gsrpc.BufferReader(callReturn.getContent());

                            ResourceNotFoundException exception = new ResourceNotFoundException();

                            exception.unmarshal(reader);

                            Notify(exception,null);

                            return;
                        }
                        
                        default:
                            Notify(new com.gsrpc.RemoteException(),null);
                            return;
                        }
                    }

                    
					Mail[] returnParam = new Mail[0];

					{

						com.gsrpc.BufferReader reader = new com.gsrpc.BufferReader(callReturn.getContent());

						int max6 = reader.readUInt16();

					returnParam = new Mail[max6];

					for(int i6 = 0; i6 < max6; i6 ++ ){

						Mail v6 = new Mail();

						v6.unmarshal(reader);

						returnParam[i6] = v6;

					}

					}


                    Notify(null,returnParam);
                    
                }catch(Exception e1) {
                    Notify(e1,null);
                }
            }
        };

        this.net.send(request,promise);

        return promise;
        
    }
    
}
