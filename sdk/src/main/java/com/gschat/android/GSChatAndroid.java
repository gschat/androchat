package com.gschat.android;

import com.gsrpc.Device;
import com.gsrpc.net.DHKey;
import com.gsrpc.net.DHKeyResolver;
import com.gsrpc.net.RemoteResolver;
import com.gsrpc.net.TCPClient;
import com.gsrpc.net.TCPClientBuilder;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.HashedWheelTimer;

/**
 * gschat android client
 */
public class GSChatAndroid implements RemoteResolver,DHKeyResolver,Executor {

    /**
     * the Gsrpc client
     */
    private TCPClient client;
    /**
     * create new android client
     */
    public GSChatAndroid() throws Exception {

        client = new TCPClientBuilder(this)
                .group(new NioEventLoopGroup(1, Executors.defaultThreadFactory()))
                .dispacherExecutor(this)
                .wheelTimer(new HashedWheelTimer(5, TimeUnit.SECONDS))
                .reconnect(5,TimeUnit.SECONDS)
                .build();

        client.connect();
    }

    @Override
    public DHKey resolver(Device device) throws Exception {
        return null;
    }

    @Override
    public InetSocketAddress Resolve() throws Exception {
        return null;
    }

    @Override
    public void execute(Runnable runnable) {

    }
}
