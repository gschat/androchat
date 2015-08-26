package com.github.gschat.gsim;

import com.github.gsdocker.gsrpc.Writer;

import com.github.gsdocker.gsrpc.Reader;

import java.nio.ByteBuffer;






/*
 * MSG generate by gs2java,don't modify it manually
 */
public class Message
{

    private long ts = 0;

    private String source = "";

    private String target = "";

    private com.github.gschat.gsim.MessageType type = com.github.gschat.gsim.MessageType.Single;

    private int seqid = 0;

    private byte[] content = new byte[0];



    public long getTS()
    {
        return this.ts;
    }
    public void setTS(long arg)
    {
        this.ts = arg;
    }

    public String getSource()
    {
        return this.source;
    }
    public void setSource(String arg)
    {
        this.source = arg;
    }

    public String getTarget()
    {
        return this.target;
    }
    public void setTarget(String arg)
    {
        this.target = arg;
    }

    public com.github.gschat.gsim.MessageType getType()
    {
        return this.type;
    }
    public void setType(com.github.gschat.gsim.MessageType arg)
    {
        this.type = arg;
    }

    public int getSeqID()
    {
        return this.seqid;
    }
    public void setSeqID(int arg)
    {
        this.seqid = arg;
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

			writer.WriteUint64(ts);


			writer.WriteString(source);


			writer.WriteString(target);


			type.Marshal(writer);


			writer.WriteUint32(seqid);


			writer.WriteBytes(content);


    }

    public void Unmarshal(Reader reader) throws Exception
    {

			ts = reader.ReadUint64();


			source = reader.ReadString();


			target = reader.ReadString();


			type = MessageType.Unmarshal(reader);


			seqid = reader.ReadUint32();


			content = reader.ReadBytes();


    }
}

