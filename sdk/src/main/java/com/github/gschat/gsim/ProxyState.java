package com.github.gschat.gsim;

import com.github.gsdocker.gsrpc.Writer;

import com.github.gsdocker.gsrpc.Reader;

import java.nio.ByteBuffer;




/*
 * ProxyState generate by gs2java,don't modify it manually
 */
public enum ProxyState {

    Unknown((byte)0),
	IMNode((byte)1);

    private byte value;

    ProxyState(byte val){
        this.value = val;
    }

    @Override
    public String toString() {
        switch(this.value)
        {
        
        case 1:
            return "IMNode";
        
        case 0:
            return "Unknown";
        
        }

        return String.format("ProxyState#%d",this.value);
    }

    public byte getValue() {
        return this.value;
    }

    public void Marshal(Writer writer) throws Exception
    {
        writer.WriteByte(this.value);
    }

    public static ProxyState Unmarshal(Reader reader) throws Exception
    {
        byte code = reader.ReadByte();

        switch(code)
        {
        
        case 1:
            return ProxyState.IMNode;
        
        case 0:
            return ProxyState.Unknown;
        
        }

        throw new Exception("unknown enum constant :" + code);
    }
}

