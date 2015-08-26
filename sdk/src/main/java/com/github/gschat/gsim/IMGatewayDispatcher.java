package com.github.gschat.gsim;

import com.github.gsdocker.gsrpc.Writer;

import com.github.gsdocker.gsrpc.Reader;

import java.nio.ByteBuffer;






/*
 * IMGateway generate by gs2java,don't modify it manually
 */
public final class IMGatewayDispatcher implements com.github.gsdocker.gsrpc.Dispatcher {

    private IMGateway service;

    public IMGatewayDispatcher(IMGateway service) {
        this.service = service;
    }

    public com.github.gsdocker.gsrpc.Return Dispatch(com.github.gsdocker.gsrpc.Call call) throws Exception
    {
        switch(call.getMethod()){

        
        case 2: {
				byte[] arg0 = new byte[0];

				{

					com.github.gsdocker.gsrpc.BufferReader reader = new com.github.gsdocker.gsrpc.BufferReader(call.getParams()[0].getContent());

					arg0 = reader.ReadBytes();
				}


                service.FastLogin(arg0);
                
                break;
                
            }
        
        case 0: {
				String arg0 = "";

				{

					com.github.gsdocker.gsrpc.BufferReader reader = new com.github.gsdocker.gsrpc.BufferReader(call.getParams()[0].getContent());

					arg0 = reader.ReadString();
				}


                byte[] ret = service.Login(arg0);
                
                com.github.gsdocker.gsrpc.Param[] params = new com.github.gsdocker.gsrpc.Param[1];
				{

					com.github.gsdocker.gsrpc.BufferWriter writer = new com.github.gsdocker.gsrpc.BufferWriter();

					writer.WriteBytes(ret);
					com.github.gsdocker.gsrpc.Param param = new com.github.gsdocker.gsrpc.Param();

					param.setContent(writer.Content());

					params[0] = (param);

				}


                com.github.gsdocker.gsrpc.Return callReturn = new com.github.gsdocker.gsrpc.Return();
                callReturn.setID(call.getID());
                callReturn.setService(call.getService());
                callReturn.setParams(params);
                return callReturn;
                
            }
        
        case 1: {
				byte[] arg0 = new byte[0];

				{

					com.github.gsdocker.gsrpc.BufferReader reader = new com.github.gsdocker.gsrpc.BufferReader(call.getParams()[0].getContent());

					arg0 = reader.ReadBytes();
				}


                service.Logoff(arg0);
                
                break;
                
            }
        
        }
        return null;
    }
}


