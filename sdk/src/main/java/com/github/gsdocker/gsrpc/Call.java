package com.github.gsdocker.gsrpc;

import com.github.gsdocker.gsrpc.Writer;

import com.github.gsdocker.gsrpc.Reader;

import java.nio.ByteBuffer;






/*
 * Call generate by gs2java,don't modify it manually
 */
public class Call
{

    private short id = 0;

    private short method = 0;

    private short service = 0;

    private com.github.gsdocker.gsrpc.Param[] params = new com.github.gsdocker.gsrpc.Param[0];



    public short getID()
    {
        return this.id;
    }
    public void setID(short arg)
    {
        this.id = arg;
    }

    public short getMethod()
    {
        return this.method;
    }
    public void setMethod(short arg)
    {
        this.method = arg;
    }

    public short getService()
    {
        return this.service;
    }
    public void setService(short arg)
    {
        this.service = arg;
    }

    public com.github.gsdocker.gsrpc.Param[] getParams()
    {
        return this.params;
    }
    public void setParams(com.github.gsdocker.gsrpc.Param[] arg)
    {
        this.params = arg;
    }


    public void Marshal(Writer writer)  throws Exception
    {

			writer.WriteUint16(id);


			writer.WriteUint16(method);


			writer.WriteUint16(service);


			writer.WriteUint16((short)params.length);
			for(com.github.gsdocker.gsrpc.Param v3 : params){
				v3.Marshal(writer);
			}


    }

    public void Unmarshal(Reader reader) throws Exception
    {

			id = reader.ReadUint16();


			method = reader.ReadUint16();


			service = reader.ReadUint16();


			int imax3 = reader.ReadUint16();

			params = new com.github.gsdocker.gsrpc.Param[imax3];

			for(int i3 = 0; i3 < imax3; i3 ++ ){

				com.github.gsdocker.gsrpc.Param v3 = new com.github.gsdocker.gsrpc.Param();

				v3.Unmarshal(reader);

				params[i3] = v3;

			}


    }
}

