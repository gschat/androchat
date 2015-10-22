package com.gschat;

import com.gsrpc.Writer;

import com.gsrpc.Reader;

import java.nio.ByteBuffer;


/*
 * AttachmentType generate by gs2java,don't modify it manually
 */
public enum AttachmentType {
    Text((byte)0),
	Image((byte)1),
	Video((byte)2),
	Audio((byte)3),
	GPS((byte)4),
	CMD((byte)5),
	Customer((byte)6);
    private byte value;
    AttachmentType(byte val){
        this.value = val;
    }
    @Override
    public String toString() {
        switch(this.value)
        {
        
        case 0:
            return "Text";
        
        case 1:
            return "Image";
        
        case 2:
            return "Video";
        
        case 3:
            return "Audio";
        
        case 4:
            return "GPS";
        
        case 5:
            return "CMD";
        
        case 6:
            return "Customer";
        
        }
        return "AttachmentType#" + this.value;
    }
    public byte getValue() {
        return this.value;
    }
    public void marshal(Writer writer) throws Exception
    {
         writer.writeByte(getValue()); 
    }
    public static AttachmentType unmarshal(Reader reader) throws Exception
    {
        byte code =   reader.readByte(); 
        switch(code)
        {
        
        case 0:
            return AttachmentType.Text;
        
        case 1:
            return AttachmentType.Image;
        
        case 2:
            return AttachmentType.Video;
        
        case 3:
            return AttachmentType.Audio;
        
        case 4:
            return AttachmentType.GPS;
        
        case 5:
            return AttachmentType.CMD;
        
        case 6:
            return AttachmentType.Customer;
        
        }
        throw new Exception("unknown enum constant :" + code);
    }
}
