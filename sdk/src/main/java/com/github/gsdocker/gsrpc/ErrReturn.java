package com.github.gsdocker.gsrpc;

import com.github.gsdocker.gsrpc.Writer;

import com.github.gsdocker.gsrpc.Reader;

import java.nio.ByteBuffer;






/*
 * ErrReturn generate by gs2java,don't modify it manually
 */
public class ErrReturn
{

    private short id = 0;

    private short service = 0;

    private byte[] uuid = new byte[0];

    private int code = 0;



    public short getID()
    {
        return this.id;
    }
    public void setID(short arg)
    {
        this.id = arg;
    }

    public short getService()
    {
        return this.service;
    }
    public void setService(short arg)
    {
        this.service = arg;
    }

    public byte[] getUUID()
    {
        return this.uuid;
    }
    public void setUUID(byte[] arg)
    {
        this.uuid = arg;
    }

    public int getCode()
    {
        return this.code;
    }
    public void setCode(int arg)
    {
        this.code = arg;
    }


    public void Marshal(Writer writer)  throws Exception
    {

			writer.WriteUint16(id);


			writer.WriteUint16(service);


			writer.WriteBytes(uuid);


			writer.WriteInt32(code);


    }

    public void Unmarshal(Reader reader) throws Exception
    {

			id = reader.ReadUint16();


			service = reader.ReadUint16();


			uuid = reader.ReadBytes();


			code = reader.ReadInt32();


    }
}

