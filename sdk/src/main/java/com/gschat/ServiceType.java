package com.gschat;

import com.gsrpc.Writer;

import com.gsrpc.Reader;

import java.nio.ByteBuffer;


/*
 * ServiceType generate by gs2java,don't modify it manually
 */
public enum ServiceType {
    Unknown((byte)0),
	IM((byte)1),
	Push((byte)2),
	Auth((byte)3),
	Client((byte)4),
	Status((byte)5);
    private byte value;
    ServiceType(byte val){
        this.value = val;
    }
    @Override
    public String toString() {
        switch(this.value)
        {
        
        case 0:
            return "Unknown";
        
        case 1:
            return "IM";
        
        case 2:
            return "Push";
        
        case 3:
            return "Auth";
        
        case 4:
            return "Client";
        
        case 5:
            return "Status";
        
        }
        return String.format("ServiceType#%d",this.value);
    }
    public byte getValue() {
        return this.value;
    }
    public void Marshal(Writer writer) throws Exception
    {
         writer.WriteByte(getValue()); 
    }
    public static ServiceType Unmarshal(Reader reader) throws Exception
    {
        byte code =   reader.ReadByte(); 
        switch(code)
        {
        
        case 0:
            return ServiceType.Unknown;
        
        case 1:
            return ServiceType.IM;
        
        case 2:
            return ServiceType.Push;
        
        case 3:
            return ServiceType.Auth;
        
        case 4:
            return ServiceType.Client;
        
        case 5:
            return ServiceType.Status;
        
        }
        throw new Exception("unknown enum constant :" + code);
    }
}
