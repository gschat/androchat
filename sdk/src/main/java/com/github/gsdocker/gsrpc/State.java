package com.github.gsdocker.gsrpc;

import com.github.gsdocker.gsrpc.Writer;

import com.github.gsdocker.gsrpc.Reader;

import java.nio.ByteBuffer;




/*
 * State generate by gs2java,don't modify it manually
 */
public enum State {

    Connecting((byte)2),
	Closed((byte)3),
	Disconnected((byte)0),
	Connected((byte)1);

    private byte value;

    State(byte val){
        this.value = val;
    }

    @Override
    public String toString() {
        switch(this.value)
        {
        
        case 3:
            return "Closed";
        
        case 1:
            return "Connected";
        
        case 2:
            return "Connecting";
        
        case 0:
            return "Disconnected";
        
        }

        return String.format("State#%d",this.value);
    }

    public byte getValue() {
        return this.value;
    }

    public void Marshal(Writer writer) throws Exception
    {
        writer.WriteByte(this.value);
    }

    public static State Unmarshal(Reader reader) throws Exception
    {
        byte code = reader.ReadByte();

        switch(code)
        {
        
        case 3:
            return State.Closed;
        
        case 1:
            return State.Connected;
        
        case 2:
            return State.Connecting;
        
        case 0:
            return State.Disconnected;
        
        }

        throw new Exception("unknown enum constant :" + code);
    }
}

