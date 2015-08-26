package com.github.gsdocker.gsrpc;

import com.github.gsdocker.gsrpc.Writer;

import com.github.gsdocker.gsrpc.Reader;

import java.nio.ByteBuffer;






/*
 * Device generate by gs2java,don't modify it manually
 */
public class Device
{

    private String id = "";

    private String type = "";

    private com.github.gsdocker.gsrpc.ArchType arch = com.github.gsdocker.gsrpc.ArchType.X86;

    private com.github.gsdocker.gsrpc.OSType os = com.github.gsdocker.gsrpc.OSType.Windows;

    private String osversion = "";



    public String getID()
    {
        return this.id;
    }
    public void setID(String arg)
    {
        this.id = arg;
    }

    public String getType()
    {
        return this.type;
    }
    public void setType(String arg)
    {
        this.type = arg;
    }

    public com.github.gsdocker.gsrpc.ArchType getArch()
    {
        return this.arch;
    }
    public void setArch(com.github.gsdocker.gsrpc.ArchType arg)
    {
        this.arch = arg;
    }

    public com.github.gsdocker.gsrpc.OSType getOS()
    {
        return this.os;
    }
    public void setOS(com.github.gsdocker.gsrpc.OSType arg)
    {
        this.os = arg;
    }

    public String getOSVersion()
    {
        return this.osversion;
    }
    public void setOSVersion(String arg)
    {
        this.osversion = arg;
    }


    public void Marshal(Writer writer)  throws Exception
    {

			writer.WriteString(id);


			writer.WriteString(type);


			arch.Marshal(writer);


			os.Marshal(writer);


			writer.WriteString(osversion);


    }

    public void Unmarshal(Reader reader) throws Exception
    {

			id = reader.ReadString();


			type = reader.ReadString();


			arch = ArchType.Unmarshal(reader);


			os = OSType.Unmarshal(reader);


			osversion = reader.ReadString();


    }
}

