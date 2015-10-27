package com.gschat;

import java.nio.ByteBuffer;

import com.gsrpc.Writer;

import com.gsrpc.Reader;


public class Property 
{

    private  String key = "";

    private  String value = "";



    public Property(){

    }


    public Property(String key, String value ) {
    
        this.key = key;
    
        this.value = value;
    
    }


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



    public void marshal(Writer writer)  throws Exception
    {
        writer.writeByte((byte)2);

        writer.writeByte((byte)com.gsrpc.Tag.String.getValue());
        writer.writeString(key);

        writer.writeByte((byte)com.gsrpc.Tag.String.getValue());
        writer.writeString(value);

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
                value = reader.readString();
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
