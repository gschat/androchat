package com.gschat;

import com.gsrpc.Reader;

import java.nio.ByteBuffer;

import com.gsrpc.Writer;


/*
 * IMAuth generate by gs2java,don't modify it manually
 */
public final class IMAuthRPC {

    /**
     * gsrpc net interface
     */
    private com.gsrpc.Channel net;

    /**
     * remote service id
     */
    private short serviceID;

    public IMAuthRPC(com.gsrpc.Channel net, short serviceID){
        this.net = net;
        this.serviceID = serviceID;
    }

    
    public com.gsrpc.Future<Property[]> Login(String arg0, Property[] arg1, final int timeout) throws Exception {

        com.gsrpc.Request request = new com.gsrpc.Request();

        request.setService(this.serviceID);

        request.setMethod((short)0);

        
        com.gsrpc.Param[] params = new com.gsrpc.Param[2];
		{

			com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

			writer.WriteString(arg0);

			com.gsrpc.Param param = new com.gsrpc.Param();

			param.setContent(writer.Content());

			params[0] = (param);

		}

		{

			com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

			writer.WriteUInt16((short)arg1.length);

		for(Property v3 : arg1){

			v3.Marshal(writer);

		}

			com.gsrpc.Param param = new com.gsrpc.Param();

			param.setContent(writer.Content());

			params[1] = (param);

		}


        request.setParams(params);
        

        com.gsrpc.Promise<Property[]> promise = new com.gsrpc.Promise<Property[]>(timeout){
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

                            exception.Unmarshal(reader);

                            Notify(exception,null);

                            return;
                        }
                        
                            case 1:{
                            com.gsrpc.BufferReader reader = new com.gsrpc.BufferReader(callReturn.getContent());

                            UserAuthFailedException exception = new UserAuthFailedException();

                            exception.Unmarshal(reader);

                            Notify(exception,null);

                            return;
                        }
                        
                        default:
                            Notify(new com.gsrpc.RemoteException(),null);
                            return;
                        }
                    }

                    
					Property[] returnParam = new Property[0];

					{

						com.gsrpc.BufferReader reader = new com.gsrpc.BufferReader(callReturn.getContent());

						int max6 = reader.ReadUInt16();

					returnParam = new Property[max6];

					for(int i6 = 0; i6 < max6; i6 ++ ){

						Property v6 = new Property();

						v6.Unmarshal(reader);

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
    
    public com.gsrpc.Future<Void> Logoff(Property[] arg0, final int timeout) throws Exception {

        com.gsrpc.Request request = new com.gsrpc.Request();

        request.setService(this.serviceID);

        request.setMethod((short)1);

        
        com.gsrpc.Param[] params = new com.gsrpc.Param[1];
		{

			com.gsrpc.BufferWriter writer = new com.gsrpc.BufferWriter();

			writer.WriteUInt16((short)arg0.length);

		for(Property v3 : arg0){

			v3.Marshal(writer);

		}

			com.gsrpc.Param param = new com.gsrpc.Param();

			param.setContent(writer.Content());

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
    
}
