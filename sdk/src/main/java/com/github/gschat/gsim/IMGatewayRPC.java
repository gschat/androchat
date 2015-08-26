package com.github.gschat.gsim;

import com.github.gsdocker.gsrpc.Writer;

import com.github.gsdocker.gsrpc.Reader;

import java.nio.ByteBuffer;





/*
 * IMGateway generate by gs2java,don't modify it manually
 */
public final class IMGatewayRPC {

    private com.github.gsdocker.gsrpc.Net net;

    private short serviceid;

    public IMGatewayRPC(com.github.gsdocker.gsrpc.Net net, short serviceid){
        this.net = net;
        this.serviceid = serviceid;
    }

    
    public void FastLogin(byte[] arg0, final com.github.gsdocker.gsrpc.CompleteHandler completeHandler,final int timeout) throws Exception {

        com.github.gsdocker.gsrpc.Call call = new com.github.gsdocker.gsrpc.Call();

        call.setService(this.serviceid);
        call.setMethod((short)2);
        
        com.github.gsdocker.gsrpc.Param[] params = new com.github.gsdocker.gsrpc.Param[1];
		{

			com.github.gsdocker.gsrpc.BufferWriter writer = new com.github.gsdocker.gsrpc.BufferWriter();

			writer.WriteBytes(arg0);
			com.github.gsdocker.gsrpc.Param param = new com.github.gsdocker.gsrpc.Param();

			param.setContent(writer.Content());

			params[0] = (param);

		}


        call.setParams(params);
        
        this.net.send(call,new com.github.gsdocker.gsrpc.Callback(){

            @Override
            public int getTimeout() {
                return timeout;
            }

            @Override
            public void Return(Exception e,com.github.gsdocker.gsrpc.Return callReturn){

                if (e != null) {
                    completeHandler.Complete(e);
                    return;
                }

                try{

                    completeHandler.Complete(null);
                }catch(Exception e1) {
                    completeHandler.Complete(e1);
                }
            }
        });
    }
    
    public void Login(String arg0, final com.github.gsdocker.gsrpc.CompleteHandler completeHandler,final int timeout) throws Exception {

        com.github.gsdocker.gsrpc.Call call = new com.github.gsdocker.gsrpc.Call();

        call.setService(this.serviceid);
        call.setMethod((short)0);
        
        com.github.gsdocker.gsrpc.Param[] params = new com.github.gsdocker.gsrpc.Param[1];
		{

			com.github.gsdocker.gsrpc.BufferWriter writer = new com.github.gsdocker.gsrpc.BufferWriter();

			writer.WriteString(arg0);
			com.github.gsdocker.gsrpc.Param param = new com.github.gsdocker.gsrpc.Param();

			param.setContent(writer.Content());

			params[0] = (param);

		}


        call.setParams(params);
        
        this.net.send(call,new com.github.gsdocker.gsrpc.Callback(){

            @Override
            public int getTimeout() {
                return timeout;
            }

            @Override
            public void Return(Exception e,com.github.gsdocker.gsrpc.Return callReturn){

                if (e != null) {
                    completeHandler.Complete(e);
                    return;
                }

                try{
					byte[] arg0 = new byte[0];

					{

						com.github.gsdocker.gsrpc.BufferReader reader = new com.github.gsdocker.gsrpc.BufferReader(callReturn.getParams()[0].getContent());

						arg0 = reader.ReadBytes();
					}


                    completeHandler.Complete(null, arg0);
                }catch(Exception e1) {
                    completeHandler.Complete(e1);
                }
            }
        });
    }
    
    public void Logoff(byte[] arg0, final com.github.gsdocker.gsrpc.CompleteHandler completeHandler,final int timeout) throws Exception {

        com.github.gsdocker.gsrpc.Call call = new com.github.gsdocker.gsrpc.Call();

        call.setService(this.serviceid);
        call.setMethod((short)1);
        
        com.github.gsdocker.gsrpc.Param[] params = new com.github.gsdocker.gsrpc.Param[1];
		{

			com.github.gsdocker.gsrpc.BufferWriter writer = new com.github.gsdocker.gsrpc.BufferWriter();

			writer.WriteBytes(arg0);
			com.github.gsdocker.gsrpc.Param param = new com.github.gsdocker.gsrpc.Param();

			param.setContent(writer.Content());

			params[0] = (param);

		}


        call.setParams(params);
        
        this.net.send(call,new com.github.gsdocker.gsrpc.Callback(){

            @Override
            public int getTimeout() {
                return timeout;
            }

            @Override
            public void Return(Exception e,com.github.gsdocker.gsrpc.Return callReturn){

                if (e != null) {
                    completeHandler.Complete(e);
                    return;
                }

                try{

                    completeHandler.Complete(null);
                }catch(Exception e1) {
                    completeHandler.Complete(e1);
                }
            }
        });
    }
    
}

