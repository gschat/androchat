package com.github.gsdocker.gsrpc;

import com.github.gsdocker.gsrpc.Writer;

import com.github.gsdocker.gsrpc.Reader;

import java.nio.ByteBuffer;




/*
 * Code generate by gs2java,don't modify it manually
 */
public enum Code {

    Accept((byte)1),
    Reject((byte)2),
    Call((byte)3),
    Return((byte)4),
    ErrReturn((byte)5),
    Heartbeat((byte)6),
    WhoAmI((byte)0);

    private byte value;

    Code(byte val){
        this.value = val;
    }

    @Override
    public String toString() {
        switch(this.value)
        {

            case 1:
                return "Accept";

            case 3:
                return "Call";

            case 5:
                return "ErrReturn";

            case 6:
                return "Heartbeat";

            case 2:
                return "Reject";

            case 4:
                return "Return";

            case 0:
                return "WhoAmI";

        }

        return String.format("Code#%d",this.value);
    }

    public byte getValue() {
        return this.value;
    }

    public void Marshal(Writer writer) throws Exception
    {
        writer.WriteByte(this.value);
    }

    public static Code Unmarshal(Reader reader) throws Exception
    {
        byte code = reader.ReadByte();

        switch(code)
        {

            case 1:
                return Code.Accept;

            case 3:
                return Code.Call;

            case 5:
                return Code.ErrReturn;

            case 6:
                return Code.Heartbeat;

            case 2:
                return Code.Reject;

            case 4:
                return Code.Return;

            case 0:
                return Code.WhoAmI;

        }

        throw new Exception("unknown enum constant :" + code);
    }
}

