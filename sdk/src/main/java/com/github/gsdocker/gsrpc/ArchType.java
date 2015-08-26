package com.github.gsdocker.gsrpc;

import com.github.gsdocker.gsrpc.Writer;

import com.github.gsdocker.gsrpc.Reader;

import java.nio.ByteBuffer;




/*
 * ArchType generate by gs2java,don't modify it manually
 */
public enum ArchType {

    X86((byte)0),
	X64((byte)1),
	ARM((byte)2);

    private byte value;

    ArchType(byte val){
        this.value = val;
    }

    @Override
    public String toString() {
        switch(this.value)
        {
        
        case 2:
            return "ARM";
        
        case 1:
            return "X64";
        
        case 0:
            return "X86";
        
        }

        return String.format("ArchType#%d",this.value);
    }

    public byte getValue() {
        return this.value;
    }

    public void Marshal(Writer writer) throws Exception
    {
        writer.WriteByte(this.value);
    }

    public static ArchType Unmarshal(Reader reader) throws Exception
    {
        byte code = reader.ReadByte();

        switch(code)
        {
        
        case 2:
            return ArchType.ARM;
        
        case 1:
            return ArchType.X64;
        
        case 0:
            return ArchType.X86;
        
        }

        throw new Exception("unknown enum constant :" + code);
    }
}

