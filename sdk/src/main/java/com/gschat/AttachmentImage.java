package com.gschat;

import java.nio.ByteBuffer;

import com.gsrpc.Writer;

import com.gsrpc.Reader;


/*
 * AttachmentImage generate by gs2java,don't modify it manually
 */
public class AttachmentImage
{

    private  String key = "";

    private  String name = "";



    public String getKey()
    {
        return this.key;
    }
    public void setKey(String arg)
    {
        this.key = arg;
    }

    public String getName()
    {
        return this.name;
    }
    public void setName(String arg)
    {
        this.name = arg;
    }

    public void Marshal(Writer writer)  throws Exception
    {

        writer.WriteString(key);

        writer.WriteString(name);

    }
    public void Unmarshal(Reader reader) throws Exception
    {

        key = reader.ReadString();

        name = reader.ReadString();

    }
}
