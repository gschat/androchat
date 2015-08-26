package com.github.gsdocker.gsrpc;

import com.github.gsdocker.gsrpc.Writer;

import com.github.gsdocker.gsrpc.Reader;

import java.nio.ByteBuffer;






/*
 * Param generate by gs2java,don't modify it manually
 */
public class Param
{

    private byte[] content = new byte[0];



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

			writer.WriteBytes(content);


    }

    public void Unmarshal(Reader reader) throws Exception
    {

			content = reader.ReadBytes();


    }
}

