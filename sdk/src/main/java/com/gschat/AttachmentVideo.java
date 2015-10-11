package com.gschat;

import java.nio.ByteBuffer;

import com.gsrpc.Writer;

import com.gsrpc.Reader;


/*
 * AttachmentVideo generate by gs2java,don't modify it manually
 */
public class AttachmentVideo
{

    private  String key = "";

    private  String name = "";

    private  short duration = 0;



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

    public short getDuration()
    {
        return this.duration;
    }
    public void setDuration(short arg)
    {
        this.duration = arg;
    }

    public void Marshal(Writer writer)  throws Exception
    {

        writer.WriteString(key);

        writer.WriteString(name);

        writer.WriteInt16(duration);

    }
    public void Unmarshal(Reader reader) throws Exception
    {

        key = reader.ReadString();

        name = reader.ReadString();

        duration = reader.ReadInt16();

    }
}
