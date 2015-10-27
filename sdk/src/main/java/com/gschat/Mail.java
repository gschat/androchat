package com.gschat;

import java.nio.ByteBuffer;

import com.gsrpc.Writer;

import com.gsrpc.Reader;


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



    public Mail(){

    }


    public Mail(String mailID, int sQID, long tS, String sender, String receiver, MailType type, String content, Attachment[] attachments, byte[] extension ) {
    
        this.mailID = mailID;
    
        this.sQID = sQID;
    
        this.tS = tS;
    
        this.sender = sender;
    
        this.receiver = receiver;
    
        this.type = type;
    
        this.content = content;
    
        this.attachments = attachments;
    
        this.extension = extension;
    
    }


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



    public void marshal(Writer writer)  throws Exception
    {
        writer.writeByte((byte)9);

        writer.writeByte((byte)com.gsrpc.Tag.String.getValue());
        writer.writeString(mailID);

        writer.writeByte((byte)com.gsrpc.Tag.I32.getValue());
        writer.writeUInt32(sQID);

        writer.writeByte((byte)com.gsrpc.Tag.I64.getValue());
        writer.writeUInt64(tS);

        writer.writeByte((byte)com.gsrpc.Tag.String.getValue());
        writer.writeString(sender);

        writer.writeByte((byte)com.gsrpc.Tag.String.getValue());
        writer.writeString(receiver);

        writer.writeByte((byte)com.gsrpc.Tag.I8.getValue());
        type.marshal(writer);

        writer.writeByte((byte)com.gsrpc.Tag.String.getValue());
        writer.writeString(content);

        writer.writeByte((byte)((com.gsrpc.Tag.Table.getValue() << 4)|com.gsrpc.Tag.List.getValue()));
        writer.writeUInt16((short)attachments.length);

		for(Attachment v3 : attachments){

			v3.marshal(writer);

		}

        writer.writeByte((byte)((com.gsrpc.Tag.I8.getValue() << 4)|com.gsrpc.Tag.List.getValue()));
        writer.writeBytes(extension);

    }
    public void unmarshal(Reader reader) throws Exception
    {
        byte __fields = reader.readByte();

        {
            byte tag = reader.readByte();

            if(tag != com.gsrpc.Tag.Skip.getValue()) {
                mailID = reader.readString();
            }

            if(-- __fields == 0) {
                return;
            }
        }


        {
            byte tag = reader.readByte();

            if(tag != com.gsrpc.Tag.Skip.getValue()) {
                sQID = reader.readUInt32();
            }

            if(-- __fields == 0) {
                return;
            }
        }


        {
            byte tag = reader.readByte();

            if(tag != com.gsrpc.Tag.Skip.getValue()) {
                tS = reader.readUInt64();
            }

            if(-- __fields == 0) {
                return;
            }
        }


        {
            byte tag = reader.readByte();

            if(tag != com.gsrpc.Tag.Skip.getValue()) {
                sender = reader.readString();
            }

            if(-- __fields == 0) {
                return;
            }
        }


        {
            byte tag = reader.readByte();

            if(tag != com.gsrpc.Tag.Skip.getValue()) {
                receiver = reader.readString();
            }

            if(-- __fields == 0) {
                return;
            }
        }


        {
            byte tag = reader.readByte();

            if(tag != com.gsrpc.Tag.Skip.getValue()) {
                type = MailType.unmarshal(reader);
            }

            if(-- __fields == 0) {
                return;
            }
        }


        {
            byte tag = reader.readByte();

            if(tag != com.gsrpc.Tag.Skip.getValue()) {
                content = reader.readString();
            }

            if(-- __fields == 0) {
                return;
            }
        }


        {
            byte tag = reader.readByte();

            if(tag != com.gsrpc.Tag.Skip.getValue()) {
                int max3 = reader.readUInt16();

		attachments = new Attachment[max3];

		for(int i3 = 0; i3 < max3; i3 ++ ){

			Attachment v3 = new Attachment();

			v3.unmarshal(reader);

			attachments[i3] = v3;

		}
            }

            if(-- __fields == 0) {
                return;
            }
        }


        {
            byte tag = reader.readByte();

            if(tag != com.gsrpc.Tag.Skip.getValue()) {
                extension = reader.readBytes();
            }

            if(-- __fields == 0) {
                return;
            }
        }



        for(int i = 0; i < (int)__fields; i ++) {
            byte tag = reader.readByte();

            if (tag == com.gsrpc.Tag.Skip.getValue()) {
                continue;
            }

            reader.readSkip(tag);
        }
    }

}
