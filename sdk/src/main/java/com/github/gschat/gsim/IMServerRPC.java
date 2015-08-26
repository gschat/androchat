package com.github.gschat.gsim;

import com.github.gsdocker.gsrpc.Writer;

import com.github.gsdocker.gsrpc.Reader;

import java.nio.ByteBuffer;





/*
 * IMServer generate by gs2java,don't modify it manually
 */
public final class IMServerRPC {

    private com.github.gsdocker.gsrpc.Net net;

    private short serviceid;

    public IMServerRPC(com.github.gsdocker.gsrpc.Net net, short serviceid){
        this.net = net;
        this.serviceid = serviceid;
    }

    
    public void Heartbeat(int arg0, final com.github.gsdocker.gsrpc.CompleteHandler completeHandler,final int timeout) throws Exception {

        com.github.gsdocker.gsrpc.Call call = new com.github.gsdocker.gsrpc.Call();

        call.setService(this.serviceid);
        call.setMethod((short)2);
        
        com.github.gsdocker.gsrpc.Param[] params = new com.github.gsdocker.gsrpc.Param[1];
		{

			com.github.gsdocker.gsrpc.BufferWriter writer = new com.github.gsdocker.gsrpc.BufferWriter();

			writer.WriteUint32(arg0);
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
					int arg0 = 0;

					{

						com.github.gsdocker.gsrpc.BufferReader reader = new com.github.gsdocker.gsrpc.BufferReader(callReturn.getParams()[0].getContent());

						arg0 = reader.ReadUint32();
					}


                    completeHandler.Complete(null, arg0);
                }catch(Exception e1) {
                    completeHandler.Complete(e1);
                }
            }
        });
    }
    
    public void Poll(byte[] arg0, int arg1, final com.github.gsdocker.gsrpc.CompleteHandler completeHandler,final int timeout) throws Exception {

        com.github.gsdocker.gsrpc.Call call = new com.github.gsdocker.gsrpc.Call();

        call.setService(this.serviceid);
        call.setMethod((short)1);
        
        com.github.gsdocker.gsrpc.Param[] params = new com.github.gsdocker.gsrpc.Param[2];
		{

			com.github.gsdocker.gsrpc.BufferWriter writer = new com.github.gsdocker.gsrpc.BufferWriter();

			writer.WriteBytes(arg0);
			com.github.gsdocker.gsrpc.Param param = new com.github.gsdocker.gsrpc.Param();

			param.setContent(writer.Content());

			params[0] = (param);

		}

		{

			com.github.gsdocker.gsrpc.BufferWriter writer = new com.github.gsdocker.gsrpc.BufferWriter();

			writer.WriteUint32(arg1);
			com.github.gsdocker.gsrpc.Param param = new com.github.gsdocker.gsrpc.Param();

			param.setContent(writer.Content());

			params[1] = (param);

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
    
    public void ResendMessage(int arg0, final com.github.gsdocker.gsrpc.CompleteHandler completeHandler,final int timeout) throws Exception {

        com.github.gsdocker.gsrpc.Call call = new com.github.gsdocker.gsrpc.Call();

        call.setService(this.serviceid);
        call.setMethod((short)3);
        
        com.github.gsdocker.gsrpc.Param[] params = new com.github.gsdocker.gsrpc.Param[1];
		{

			com.github.gsdocker.gsrpc.BufferWriter writer = new com.github.gsdocker.gsrpc.BufferWriter();

			writer.WriteUint32(arg0);
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
					boolean arg0 = false;

					{

						com.github.gsdocker.gsrpc.BufferReader reader = new com.github.gsdocker.gsrpc.BufferReader(callReturn.getParams()[0].getContent());

						arg0 = reader.ReadBoolean();
					}


                    completeHandler.Complete(null, arg0);
                }catch(Exception e1) {
                    completeHandler.Complete(e1);
                }
            }
        });
    }
    
    public void SendMessage(com.github.gschat.gsim.Message arg0, final com.github.gsdocker.gsrpc.CompleteHandler completeHandler,final int timeout) throws Exception {

        com.github.gsdocker.gsrpc.Call call = new com.github.gsdocker.gsrpc.Call();

        call.setService(this.serviceid);
        call.setMethod((short)0);
        
        com.github.gsdocker.gsrpc.Param[] params = new com.github.gsdocker.gsrpc.Param[1];
		{

			com.github.gsdocker.gsrpc.BufferWriter writer = new com.github.gsdocker.gsrpc.BufferWriter();

			arg0.Marshal(writer);
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
					long arg0 = 0;

					{

						com.github.gsdocker.gsrpc.BufferReader reader = new com.github.gsdocker.gsrpc.BufferReader(callReturn.getParams()[0].getContent());

						arg0 = reader.ReadUint64();
					}


                    completeHandler.Complete(null, arg0);
                }catch(Exception e1) {
                    completeHandler.Complete(e1);
                }
            }
        });
    }
    
}

