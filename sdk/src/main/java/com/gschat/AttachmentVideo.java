package com.gschat;

import com.gsrpc.Writer;

import com.gsrpc.Reader;

import java.nio.ByteBuffer;


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

    public void marshal(Writer writer)  throws Exception
    {
        writer.writeByte((byte)3);

        writer.writeByte((byte)com.gsrpc.Tag.String.getValue());
        writer.writeString(key);

        writer.writeByte((byte)com.gsrpc.Tag.String.getValue());
        writer.writeString(name);

        writer.writeByte((byte)com.gsrpc.Tag.I16.getValue());
        writer.writeInt16(duration);

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

        
        {
            byte tag = reader.readByte();

            if(tag != com.gsrpc.Tag.Skip.getValue()) {
                duration = reader.readInt16();
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
