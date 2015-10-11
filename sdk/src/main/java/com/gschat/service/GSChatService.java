package com.gschat.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.gsrpc.Device;
import com.gsrpc.net.DHClientHandler;
import com.gsrpc.net.DHKey;
import com.gsrpc.net.DHKeyResolver;
import com.gsrpc.net.RemoteResolver;
import com.gsrpc.net.TCPClientBuilder;

import java.math.BigInteger;
import java.net.InetSocketAddress;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * GSChat background service to support persistent connection and communication with chat server
 */
public class GSChatService extends Service {


    @Override
    public void onCreate() {
        super.onCreate();

        try {
            new TCPClientBuilder(new RemoteResolver() {
                @Override
                public InetSocketAddress Resolve() throws Exception {
                    return new InetSocketAddress("localhost", 13512);
                }
            }).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new DHClientHandler(new Device(), new DHKeyResolver() {
                        @Override
                        public DHKey resolver(Device device) throws Exception {
                            return new DHKey(
                                    new BigInteger("6849211231874234332173554215962568648211715948614349192108760170867674332076420634857278025209099493881977517436387566623834457627945222750416199306671083"),
                                    new BigInteger("13196520348498300509170571968898643110806720751219744788129636326922565480984492185368038375211941297871289403061486510064429072584259746910423138674192557")
                            );
                        }
                    }));
                }
            }).reconnect(2, java.util.concurrent.TimeUnit.SECONDS).Build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new GSChatServiceBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
