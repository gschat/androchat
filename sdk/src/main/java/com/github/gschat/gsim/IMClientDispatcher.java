package com.github.gschat.gsim;

import com.github.gsdocker.gsrpc.Writer;

import com.github.gsdocker.gsrpc.Reader;

import java.nio.ByteBuffer;






/*
 * IMClient generate by gs2java,don't modify it manually
 */
public final class IMClientDispatcher implements com.github.gsdocker.gsrpc.Dispatcher {

    private IMClient service;

    public IMClientDispatcher(IMClient service) {
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


                service.Heartbeat(arg0);
                
                break;
                
            }
        
        case 1: {

                service.KickOff();
                
                break;
                
            }
        
        case 3: {
				com.github.gschat.gsim.Message arg0 = new com.github.gschat.gsim.Message();

				{

					com.github.gsdocker.gsrpc.BufferReader reader = new com.github.gsdocker.gsrpc.BufferReader(call.getParams()[0].getContent());

					arg0.Unmarshal(reader);
				}


                service.RecvMessage(arg0);
                
                break;
                
            }
        
        case 0: {
				com.github.gschat.gsim.Message arg0 = new com.github.gschat.gsim.Message();

				{

					com.github.gsdocker.gsrpc.BufferReader reader = new com.github.gsdocker.gsrpc.BufferReader(call.getParams()[0].getContent());

					arg0.Unmarshal(reader);
				}


                service.RecvPushMessage(arg0);
                
                break;
                
            }
        
        }
        return null;
    }
}


