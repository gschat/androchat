package com.gschat;

import com.gsrpc.Reader;

import java.nio.ByteBuffer;

import com.gsrpc.Writer;


/*
 * AttachmentText generate by gs2java,don't modify it manually
 */
public class AttachmentText
{

    private  String text = "";



    public String getText()
    {
        return this.text;
    }
    public void setText(String arg)
    {
        this.text = arg;
    }

    public void Marshal(Writer writer)  throws Exception
    {

        writer.WriteString(text);

    }
    public void Unmarshal(Reader reader) throws Exception
    {

        text = reader.ReadString();

    }
}
