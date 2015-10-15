package com.gschat;

import java.nio.ByteBuffer;

import com.gsrpc.Writer;

import com.gsrpc.Reader;


/*
 * AttachmentGPS generate by gs2java,don't modify it manually
 */
public class AttachmentGPS
{

    private  double longitude = 0;

    private  double latitude = 0;

    private  String address = "";



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

    public void Marshal(Writer writer)  throws Exception
    {

        writer.WriteFloat64(longitude);

        writer.WriteFloat64(latitude);

        writer.WriteString(address);

    }
    public void Unmarshal(Reader reader) throws Exception
    {

        longitude = reader.ReadFloat64();

        latitude = reader.ReadFloat64();

        address = reader.ReadString();

    }
}
