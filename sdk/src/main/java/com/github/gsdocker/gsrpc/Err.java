package com.github.gsdocker.gsrpc;

import com.github.gsdocker.gsrpc.Writer;

import com.github.gsdocker.gsrpc.Reader;

import java.nio.ByteBuffer;




/*
 * Err generate by gs2java,don't modify it manually
 */
public enum Err {

    Closed((byte)3),
	Cancel((byte)4),
	NotFound((byte)8),
	Service((byte)10),
	Overflow((byte)6),
	Unknown((byte)7),
	Code((byte)9),
	Success((byte)0),
	KickOff((byte)1),
	Timeout((byte)2),
	RPC((byte)5);

    private byte value;

    Err(byte val){
        this.value = val;
    }

    @Override
    public String toString() {
        switch(this.value)
        {
        
        case 4:
            return "Cancel";
        
        case 3:
            return "Closed";
        
        case 9:
            return "Code";
        
        case 1:
            return "KickOff";
        
        case 8:
            return "NotFound";
        
        case 6:
            return "Overflow";
        
        case 5:
            return "RPC";
        
        case 10:
            return "Service";
        
        case 0:
            return "Success";
        
        case 2:
            return "Timeout";
        
        case 7:
            return "Unknown";
        
        }

        return String.format("Err#%d",this.value);
    }

    public byte getValue() {
        return this.value;
    }

    public void Marshal(Writer writer) throws Exception
    {
        writer.WriteByte(this.value);
    }

    public static Err Unmarshal(Reader reader) throws Exception
    {
        byte code = reader.ReadByte();

        switch(code)
        {
        
        case 4:
            return Err.Cancel;
        
        case 3:
            return Err.Closed;
        
        case 9:
            return Err.Code;
        
        case 1:
            return Err.KickOff;
        
        case 8:
            return Err.NotFound;
        
        case 6:
            return Err.Overflow;
        
        case 5:
            return Err.RPC;
        
        case 10:
            return Err.Service;
        
        case 0:
            return Err.Success;
        
        case 2:
            return Err.Timeout;
        
        case 7:
            return Err.Unknown;
        
        }

        throw new Exception("unknown enum constant :" + code);
    }
}

