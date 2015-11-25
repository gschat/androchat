package com.gschat;

import com.gsrpc.Writer;

import com.gsrpc.Reader;

import java.nio.ByteBuffer;

import com.gsrpc.Device;

import com.gsrpc.KV;


/*
 * Service generate by gs2java,don't modify it manually
 */
public enum Service {
    Unknown((byte)1),
	MailHub((byte)2),
	Push((byte)3),
	Auth((byte)4),
	Client((byte)5),
	UserBinder((byte)6),
	PushServiceProvider((byte)7),
	DHKeyResolver((byte)8),
	UserResolverListener((byte)9),
	UserResolver((byte)10),
	Gateway((byte)11);
    private byte value;
    Service(byte val){
        this.value = val;
    }
    @Override
    public String toString() {
        switch(this.value)
        {
        
        case 1:
            return "Unknown";
        
        case 2:
            return "MailHub";
        
        case 3:
            return "Push";
        
        case 4:
            return "Auth";
        
        case 5:
            return "Client";
        
        case 6:
            return "UserBinder";
        
        case 7:
            return "PushServiceProvider";
        
        case 8:
            return "DHKeyResolver";
        
        case 9:
            return "UserResolverListener";
        
        case 10:
            return "UserResolver";
        
        case 11:
            return "Gateway";
        
        }
        return "Service#" + this.value;
    }
    public byte getValue() {
        return this.value;
    }
    public void marshal(Writer writer) throws Exception
    {
         writer.writeByte(getValue()); 
    }
    public static Service unmarshal(Reader reader) throws Exception
    {
        byte code =   reader.readByte(); 
        switch(code)
        {
        
        case 1:
            return Service.Unknown;
        
        case 2:
            return Service.MailHub;
        
        case 3:
            return Service.Push;
        
        case 4:
            return Service.Auth;
        
        case 5:
            return Service.Client;
        
        case 6:
            return Service.UserBinder;
        
        case 7:
            return Service.PushServiceProvider;
        
        case 8:
            return Service.DHKeyResolver;
        
        case 9:
            return Service.UserResolverListener;
        
        case 10:
            return Service.UserResolver;
        
        case 11:
            return Service.Gateway;
        
        }
        throw new Exception("unknown enum constant :" + code);
    }
}
