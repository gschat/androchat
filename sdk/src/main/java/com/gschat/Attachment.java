package com.gschat;

import com.gsrpc.Writer;

import com.gsrpc.Reader;

import java.nio.ByteBuffer;


/*
 * Attachment generate by gs2java,don't modify it manually
 */
public class Attachment
{

    private  AttachmentType type = AttachmentType.Text;

    private  byte[] content = new byte[0];



    public AttachmentType getType()
    {
        return this.type;
    }
    public void setType(AttachmentType arg)
    {
        this.type = arg;
    }

    public byte[] getContent()
    {
        return this.content;
    }
    public void setContent(byte[] arg)
    {
        this.content = arg;
    }

    public void Marshal(Writer writer)  throws Exception
    {

        type.Marshal(writer);

        writer.WriteBytes(content);

    }
    public void Unmarshal(Reader reader) throws Exception
    {

        type = AttachmentType.Unmarshal(reader);

        content = reader.ReadBytes();

    }
}
