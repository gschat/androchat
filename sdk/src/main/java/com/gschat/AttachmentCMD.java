package com.gschat;

import java.nio.ByteBuffer;

import com.gsrpc.Writer;

import com.gsrpc.Reader;


/*
 * AttachmentCMD generate by gs2java,don't modify it manually
 */
public class AttachmentCMD
{

    private  String command = "";



    public String getCommand()
    {
        return this.command;
    }
    public void setCommand(String arg)
    {
        this.command = arg;
    }

    public void Marshal(Writer writer)  throws Exception
    {

        writer.WriteString(command);

    }
    public void Unmarshal(Reader reader) throws Exception
    {

        command = reader.ReadString();

    }
}