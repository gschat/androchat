package com.gschat;

import com.gsrpc.Writer;

import com.gsrpc.Reader;

import java.nio.ByteBuffer;

import com.gsrpc.Device;

import com.gsrpc.KV;


public class AttachmentText 
{

    private  String text = "";



    public AttachmentText(){

    }


    public AttachmentText(String text ) {
    
        this.text = text;
    
    }


    public String getText()
    {
        return this.text;
    }
    public void setText(String arg)
    {
        this.text = arg;
    }



    public void marshal(Writer writer)  throws Exception
    {
        writer.writeByte((byte)1);

        writer.writeByte((byte)com.gsrpc.Tag.String.getValue());
        writer.writeString(text);

    }
    public void unmarshal(Reader reader) throws Exception
    {
        byte __fields = reader.readByte();

        {
            byte tag = reader.readByte();

            if(tag != com.gsrpc.Tag.Skip.getValue()) {
                text = reader.readString();
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
