package com.gschat;

import java.nio.ByteBuffer;

import com.gsrpc.Writer;

import com.gsrpc.Reader;


public class AttachmentGPS 
{

    private  double longitude = 0;

    private  double latitude = 0;

    private  String address = "";



    public AttachmentGPS(){

    }


    public AttachmentGPS(double longitude, double latitude, String address ) {
    
        this.longitude = longitude;
    
        this.latitude = latitude;
    
        this.address = address;
    
    }


    public double getLongitude()
    {
        return this.longitude;
    }
    public void setLongitude(double arg)
    {
        this.longitude = arg;
    }

    public double getLatitude()
    {
        return this.latitude;
    }
    public void setLatitude(double arg)
    {
        this.latitude = arg;
    }

    public String getAddress()
    {
        return this.address;
    }
    public void setAddress(String arg)
    {
        this.address = arg;
    }



    public void marshal(Writer writer)  throws Exception
    {
        writer.writeByte((byte)3);

        writer.writeByte((byte)com.gsrpc.Tag.I64.getValue());
        writer.writeFloat64(longitude);

        writer.writeByte((byte)com.gsrpc.Tag.I64.getValue());
        writer.writeFloat64(latitude);

        writer.writeByte((byte)com.gsrpc.Tag.String.getValue());
        writer.writeString(address);

    }
    public void unmarshal(Reader reader) throws Exception
    {
        byte __fields = reader.readByte();

        {
            byte tag = reader.readByte();

            if(tag != com.gsrpc.Tag.Skip.getValue()) {
                longitude = reader.readFloat64();
            }

            if(-- __fields == 0) {
                return;
            }
        }


        {
            byte tag = reader.readByte();

            if(tag != com.gsrpc.Tag.Skip.getValue()) {
                latitude = reader.readFloat64();
            }

            if(-- __fields == 0) {
                return;
            }
        }


        {
            byte tag = reader.readByte();

            if(tag != com.gsrpc.Tag.Skip.getValue()) {
                address = reader.readString();
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
