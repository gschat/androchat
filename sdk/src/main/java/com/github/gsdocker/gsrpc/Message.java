package com.github.gsdocker.gsrpc;

import com.github.gsdocker.gsrpc.Writer;

import com.github.gsdocker.gsrpc.Reader;

import java.nio.ByteBuffer;






/*
 * MSG generate by gs2java,don't modify it manually
 */
public class Message
{

    private com.github.gsdocker.gsrpc.Code code = com.github.gsdocker.gsrpc.Code.WhoAmI;

    private byte[] content = new byte[0];



    public com.github.gsdocker.gsrpc.Code getCode()
    {
        return this.code;
    }
    public void setCode(com.github.gsdocker.gsrpc.Code arg)
    {
        this.code = arg;
    }

    public byte[] getContent()
    {
        return this.content;
    }
    public void setContent(byte[] arg)
    {
        this.content = arg;
    }


    public void Marshal(Writer writer)  throws Exception
    {

			code.Marshal(writer);


			writer.WriteBytes(content);


    }

    public void Unmarshal(Reader reader) throws Exception
    {

			code = Code.Unmarshal(reader);


			content = reader.ReadBytes();


    }
}

