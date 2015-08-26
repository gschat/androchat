package com.github.gschat.gsim;

import com.github.gsdocker.gsrpc.Writer;

import com.github.gsdocker.gsrpc.Reader;

import java.nio.ByteBuffer;




/*
 * Tunnel generate by gs2java,don't modify it manually
 */
public enum Tunnel {

    IM((byte)0),
	Push((byte)1);

    private byte value;

    Tunnel(byte val){
        this.value = val;
    }

    @Override
    public String toString() {
        switch(this.value)
        {
        
        case 0:
            return "IM";
        
        case 1:
            return "Push";
        
        }

        return String.format("Tunnel#%d",this.value);
    }

    public byte getValue() {
        return this.value;
    }

    public void Marshal(Writer writer) throws Exception
    {
        writer.WriteByte(this.value);
    }

    public static Tunnel Unmarshal(Reader reader) throws Exception
    {
        byte code = reader.ReadByte();

        switch(code)
        {
        
        case 0:
            return Tunnel.IM;
        
        case 1:
            return Tunnel.Push;
        
        }

        throw new Exception("unknown enum constant :" + code);
    }
}

