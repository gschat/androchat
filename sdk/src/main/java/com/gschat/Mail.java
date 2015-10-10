package com.gschat;

import java.nio.ByteBuffer;

import com.gsrpc.Writer;

import com.gsrpc.Reader;


/*
 * Mail generate by gs2java,don't modify it manually
 */
public class Mail
{

    private  String mailID = "";

    private  int sQID = 0;

    private  long tS = 0;

    private  String sender = "";

    private  String receiver = "";

    private  MailType type = MailType.Single;

    private  String content = "";

    private  Attachment[] attachments = new Attachment[0];

    private  byte[] extension = new byte[0];



    public String getMailID()
    {
        return this.mailID;
    }
    public void setMailID(String arg)
    {
        this.mailID = arg;
    }

    public int getSQID()
    {
        return this.sQID;
    }
    public void setSQID(int arg)
    {
        this.sQID = arg;
    }

    public long getTS()
    {
        return this.tS;
    }
    public void setTS(long arg)
    {
        this.tS = arg;
    }

    public String getSender()
    {
        return this.sender;
    }
    public void setSender(String arg)
    {
        this.sender = arg;
    }

    public String getReceiver()
    {
        return this.receiver;
    }
    public void setReceiver(String arg)
    {
        this.receiver = arg;
    }

    public MailType getType()
    {
        return this.type;
    }
    public void setType(MailType arg)
    {
        this.type = arg;
    }

    public String getContent()
    {
        return this.content;
    }
    public void setContent(String arg)
    {
        this.content = arg;
    }

    public Attachment[] getAttachments()
    {
        return this.attachments;
    }
    public void setAttachments(Attachment[] arg)
    {
        this.attachments = arg;
    }

    public byte[] getExtension()
    {
        return this.extension;
    }
    public void setExtension(byte[] arg)
    {
        this.extension = arg;
    }

    public void Marshal(Writer writer)  throws Exception
    {

        writer.WriteString(mailID);

        writer.WriteUInt32(sQID);

        writer.WriteUInt64(tS);

        writer.WriteString(sender);

        writer.WriteString(receiver);

        type.Marshal(writer);

        writer.WriteString(content);

        writer.WriteUInt16((short)attachments.length);

		for(Attachment v3 : attachments){

			v3.Marshal(writer);

		}

        writer.WriteBytes(extension);

    }
    public void Unmarshal(Reader reader) throws Exception
    {

        mailID = reader.ReadString();

        sQID = reader.ReadUInt32();

        tS = reader.ReadUInt64();

        sender = reader.ReadString();

        receiver = reader.ReadString();

        type = MailType.Unmarshal(reader);

        content = reader.ReadString();

        int max3 = reader.ReadUInt16();

		attachments = new Attachment[max3];

		for(int i3 = 0; i3 < max3; i3 ++ ){

			Attachment v3 = new Attachment();

			v3.Unmarshal(reader);

			attachments[i3] = v3;

		}

        extension = reader.ReadBytes();

    }
}
