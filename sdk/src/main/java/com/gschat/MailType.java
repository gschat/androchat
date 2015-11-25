package com.gschat;

import com.gsrpc.Writer;

import com.gsrpc.Reader;

import java.nio.ByteBuffer;

import com.gsrpc.Device;

import com.gsrpc.KV;


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
        return "MailType#" + this.value;
    }
    public byte getValue() {
        return this.value;
    }
    public void marshal(Writer writer) throws Exception
    {
         writer.writeByte(getValue()); 
    }
    public static MailType unmarshal(Reader reader) throws Exception
    {
        byte code =   reader.readByte(); 
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
