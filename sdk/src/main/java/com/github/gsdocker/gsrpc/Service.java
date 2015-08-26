package com.github.gsdocker.gsrpc;

import com.github.gsdocker.gsrpc.Writer;

import com.github.gsdocker.gsrpc.Reader;

import java.nio.ByteBuffer;




/*
 * Service generate by gs2java,don't modify it manually
 */
public enum Service {

    GW((short)0),
	GS((short)1),
	GC((short)2);

    private short value;

    Service(short val){
        this.value = val;
    }

    @Override
    public String toString() {
        switch(this.value)
        {
        
        case 2:
            return "GC";
        
        case 1:
            return "GS";
        
        case 0:
            return "GW";
        
        }

        return String.format("Service#%d",this.value);
    }

    public short getValue() {
        return this.value;
    }

    public void Marshal(Writer writer) throws Exception
    {
        writer.WriteUint16(this.value);
    }

    public static Service Unmarshal(Reader reader) throws Exception
    {
        short code = reader.ReadUint16();

        switch(code)
        {
        
        case 2:
            return Service.GC;
        
        case 1:
            return Service.GS;
        
        case 0:
            return Service.GW;
        
        }

        throw new Exception("unknown enum constant :" + code);
    }
}

