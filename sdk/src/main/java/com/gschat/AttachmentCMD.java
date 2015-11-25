package com.gschat;

import com.gsrpc.KV;

import com.gsrpc.Writer;

import com.gsrpc.Reader;

import java.nio.ByteBuffer;

import com.gsrpc.Device;


public class AttachmentCMD 
{

    private  String command = "";



    public AttachmentCMD(){

    }


    public AttachmentCMD(String command ) {
    
        this.command = command;
    
    }


    public String getCommand()
    {
        return this.command;
    }
    public void setCommand(String arg)
    {
        this.command = arg;
    }



    public void marshal(Writer writer)  throws Exception
    {
        writer.writeByte((byte)1);

        writer.writeByte((byte)com.gsrpc.Tag.String.getValue());
        writer.writeString(command);

    }
    public void unmarshal(Reader reader) throws Exception
    {
        byte __fields = reader.readByte();

        {
            byte tag = reader.readByte();

            if(tag != com.gsrpc.Tag.Skip.getValue()) {
                command = reader.readString();
            }

            if(-- __fields == 0) {
                return;
            }
        }



        for(int i = 0; i < (int)__fields; i ++) {
            byte tag = reader.readByte();

            if (tag == com.gsrpc.Tag.Skip.getValue()) {
                continue;
            }

            reader.readSkip(tag);
        }
    }

}
