package com.gschat;

import com.gschat.UserNotFoundException;

import com.gsrpc.Device;

import com.gsrpc.KV;

import java.nio.ByteBuffer;

import com.gsrpc.Reader;

import com.gschat.Mail;

import com.gschat.UnexpectSQIDException;

import com.gschat.ResourceNotFoundException;

import com.gschat.ResourceBusyException;

import com.gschat.UserAuthFailedException;

import com.gsrpc.Writer;


public class Sync 
{

    private  int offset = 0;

    private  int count = 0;



    public Sync(){

    }


    public Sync(int offset, int count ) {
    
        this.offset = offset;
    
        this.count = count;
    
    }


    public int getOffset()
    {
        return this.offset;
    }
    public void setOffset(int arg)
    {
        this.offset = arg;
    }

    public int getCount()
    {
        return this.count;
    }
    public void setCount(int arg)
    {
        this.count = arg;
    }



    public void marshal(Writer writer)  throws Exception
    {
        writer.writeByte((byte)2);

        writer.writeByte((byte)com.gsrpc.Tag.I32.getValue());
        writer.writeUInt32(offset);

        writer.writeByte((byte)com.gsrpc.Tag.I32.getValue());
        writer.writeUInt32(count);

    }
    public void unmarshal(Reader reader) throws Exception
    {
        byte __fields = reader.readByte();

        {
            byte tag = reader.readByte();

            if(tag != com.gsrpc.Tag.Skip.getValue()) {
                offset = reader.readUInt32();
            }

            if(-- __fields == 0) {
                return;
            }
        }


        {
            byte tag = reader.readByte();

            if(tag != com.gsrpc.Tag.Skip.getValue()) {
                count = reader.readUInt32();
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
