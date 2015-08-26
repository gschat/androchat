package com.github.gschat.gsim;

import com.github.gsdocker.gsrpc.Writer;

import com.github.gsdocker.gsrpc.Reader;

import java.nio.ByteBuffer;






/*
 * IMServer generate by gs2java,don't modify it manually
 */
public final class IMServerDispatcher implements com.github.gsdocker.gsrpc.Dispatcher {

    private IMServer service;

    public IMServerDispatcher(IMServer service) {
        this.service = service;
    }

    public com.github.gsdocker.gsrpc.Return Dispatch(com.github.gsdocker.gsrpc.Call call) throws Exception
    {
        switch(call.getMethod()){

        
        case 2: {
				int arg0 = 0;

				{

					com.github.gsdocker.gsrpc.BufferReader reader = new com.github.gsdocker.gsrpc.BufferReader(call.getParams()[0].getContent());

					arg0 = reader.ReadUint32();
				}


                int ret = service.Heartbeat(arg0);
                
                com.github.gsdocker.gsrpc.Param[] params = new com.github.gsdocker.gsrpc.Param[1];
				{

					com.github.gsdocker.gsrpc.BufferWriter writer = new com.github.gsdocker.gsrpc.BufferWriter();

					writer.WriteUint32(ret);
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

				int arg1 = 0;

				{

					com.github.gsdocker.gsrpc.BufferReader reader = new com.github.gsdocker.gsrpc.BufferReader(call.getParams()[1].getContent());

					arg1 = reader.ReadUint32();
				}


                service.Poll(arg0, arg1);
                
                break;
                
            }
        
        case 3: {
				int arg0 = 0;

				{

					com.github.gsdocker.gsrpc.BufferReader reader = new com.github.gsdocker.gsrpc.BufferReader(call.getParams()[0].getContent());

					arg0 = reader.ReadUint32();
				}


                boolean ret = service.ResendMessage(arg0);
                
                com.github.gsdocker.gsrpc.Param[] params = new com.github.gsdocker.gsrpc.Param[1];
				{

					com.github.gsdocker.gsrpc.BufferWriter writer = new com.github.gsdocker.gsrpc.BufferWriter();

					writer.WriteBoolean(ret);
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
        
        case 0: {
				com.github.gschat.gsim.Message arg0 = new com.github.gschat.gsim.Message();

				{

					com.github.gsdocker.gsrpc.BufferReader reader = new com.github.gsdocker.gsrpc.BufferReader(call.getParams()[0].getContent());

					arg0.Unmarshal(reader);
				}


                long ret = service.SendMessage(arg0);
                
                com.github.gsdocker.gsrpc.Param[] params = new com.github.gsdocker.gsrpc.Param[1];
				{

					com.github.gsdocker.gsrpc.BufferWriter writer = new com.github.gsdocker.gsrpc.BufferWriter();

					writer.WriteUint64(ret);
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
        
        }
        return null;
    }
}


