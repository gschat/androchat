package com.gschat;

import com.gsrpc.Device;

import com.gsrpc.KV;

import com.gsrpc.Writer;

import com.gsrpc.Reader;

import java.nio.ByteBuffer;


public class AttachmentImage 
{

    private  String key = "";

    private  String name = "";



    public AttachmentImage(){

    }


    public AttachmentImage(String key, String name ) {
    
        this.key = key;
    
        this.name = name;
    
    }


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



    public void marshal(Writer writer)  throws Exception
    {
        writer.writeByte((byte)2);

        writer.writeByte((byte)com.gsrpc.Tag.String.getValue());
        writer.writeString(key);

        writer.writeByte((byte)com.gsrpc.Tag.String.getValue());
        writer.writeString(name);

    }
    public void unmarshal(Reader reader) throws Exception
    {
        byte __fields = reader.readByte();

        {
            byte tag = reader.readByte();

            if(tag != com.gsrpc.Tag.Skip.getValue()) {
                key = reader.readString();
            }

            if(-- __fields == 0) {
                return;
            }
        }


        {
            byte tag = reader.readByte();

            if(tag != com.gsrpc.Tag.Skip.getValue()) {
                name = reader.readString();
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
