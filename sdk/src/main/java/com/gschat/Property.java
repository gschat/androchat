package com.gschat;

import com.gsrpc.Reader;

import java.nio.ByteBuffer;

import com.gsrpc.Writer;


/*
 * Property generate by gs2java,don't modify it manually
 */
public class Property
{

    private  String key = "";

    private  String value = "";



    public String getKey()
    {
        return this.key;
    }
    public void setKey(String arg)
    {
        this.key = arg;
    }

    public String getValue()
    {
        return this.value;
    }
    public void setValue(String arg)
    {
        this.value = arg;
    }

    public void Marshal(Writer writer)  throws Exception
    {

        writer.WriteString(key);

        writer.WriteString(value);

    }
    public void Unmarshal(Reader reader) throws Exception
    {

        key = reader.ReadString();

        value = reader.ReadString();

    }
}
