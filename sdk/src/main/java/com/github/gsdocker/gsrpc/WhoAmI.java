package com.github.gsdocker.gsrpc;

import com.github.gsdocker.gsrpc.Writer;

import com.github.gsdocker.gsrpc.Reader;

import java.nio.ByteBuffer;






/*
 * WhoAmI generate by gs2java,don't modify it manually
 */
public class WhoAmI
{

    private com.github.gsdocker.gsrpc.Device id = new com.github.gsdocker.gsrpc.Device();

    private byte[] context = new byte[0];



    public com.github.gsdocker.gsrpc.Device getID()
    {
        return this.id;
    }
    public void setID(com.github.gsdocker.gsrpc.Device arg)
    {
        this.id = arg;
    }

    public byte[] getContext()
    {
        return this.context;
    }
    public void setContext(byte[] arg)
    {
        this.context = arg;
    }


    public void Marshal(Writer writer)  throws Exception
    {

			id.Marshal(writer);


			writer.WriteBytes(context);


    }

    public void Unmarshal(Reader reader) throws Exception
    {

			id.Unmarshal(reader);


			context = reader.ReadBytes();


    }
}

