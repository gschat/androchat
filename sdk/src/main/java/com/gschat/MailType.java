package com.gschat;

import com.gsrpc.Writer;

import com.gsrpc.Reader;

import java.nio.ByteBuffer;


/*
 * MailType generate by gs2java,don't modify it manually
 */
public enum MailType {
    Single((byte)0),
	Multi((byte)1),
	System((byte)2);
    private byte value;
    MailType(byte val){
        this.value = val;
    }
    @Override
    public String toString() {
        switch(this.value)
        {
        
        case 0:
            return "Single";
        
        case 1:
            return "Multi";
        
        case 2:
            return "System";
        
        }
        return String.format("MailType#%d",this.value);
    }
    public byte getValue() {
        return this.value;
    }
    public void Marshal(Writer writer) throws Exception
    {
         writer.WriteByte(getValue()); 
    }
    public static MailType Unmarshal(Reader reader) throws Exception
    {
        byte code =   reader.ReadByte(); 
        switch(code)
        {
        
        case 0:
            return MailType.Single;
        
        case 1:
            return MailType.Multi;
        
        case 2:
            return MailType.System;
        
        }
        throw new Exception("unknown enum constant :" + code);
    }
}
