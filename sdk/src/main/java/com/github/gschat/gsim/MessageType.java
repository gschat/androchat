package com.github.gschat.gsim;

import com.github.gsdocker.gsrpc.Writer;

import com.github.gsdocker.gsrpc.Reader;

import java.nio.ByteBuffer;




/*
 * MessageType generate by gs2java,don't modify it manually
 */
public enum MessageType {

    Single((byte)0),
	Multi((byte)1),
	System((byte)2);

    private byte value;

    MessageType(byte val){
        this.value = val;
    }

    @Override
    public String toString() {
        switch(this.value)
        {
        
        case 1:
            return "Multi";
        
        case 0:
            return "Single";
        
        case 2:
            return "System";
        
        }

        return String.format("MessageType#%d",this.value);
    }

    public byte getValue() {
        return this.value;
    }

    public void Marshal(Writer writer) throws Exception
    {
        writer.WriteByte(this.value);
    }

    public static MessageType Unmarshal(Reader reader) throws Exception
    {
        byte code = reader.ReadByte();

        switch(code)
        {
        
        case 1:
            return MessageType.Multi;
        
        case 0:
            return MessageType.Single;
        
        case 2:
            return MessageType.System;
        
        }

        throw new Exception("unknown enum constant :" + code);
    }
}

