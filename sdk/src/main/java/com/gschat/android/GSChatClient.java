package com.gschat.android;

import com.gschat.Auth;
import com.gschat.AuthRPC;
import com.gschat.ClientDispatcher;
import com.gschat.Mail;
import com.gschat.MailHub;
import com.gschat.MailHubRPC;
import com.gschat.Push;
import com.gschat.PushRPC;
import com.gsrpc.Device;
import com.gsrpc.Register;
import com.gsrpc.UnknownServiceException;
import com.gsrpc.net.DHClientHandler;
import com.gsrpc.net.DHKeyResolver;
import com.gsrpc.net.HeartbeatEchoHandler;
import com.gsrpc.net.RemoteResolver;
import com.gsrpc.net.TCPClient;
import com.gsrpc.net.TCPClientBuilder;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * The gschat android network client facade
 *
 */
public abstract class GSChatClient implements DHKeyResolver,RemoteResolver, com.gschat.Client {

    /**
     * the android device object
     */
    private final Device device;

    /**
     * the gsrpc tcp client builder
     */
    private final TCPClientBuilder clientBuilder;

    /**
     * the heartbeat relay duration
     */
    private int relay;
    /**
     * the heartbeat relay duration unit
     */
    private TimeUnit unit;

    /**
     * the underground tcp client object
     */
    private TCPClient tcpClient;

    /**
     * MailHub rpc agent
     */
    private MailHubRPC mailHub;

    /**
     * Auth service agent
     */
    private AuthRPC auth;

    /**
     * Push service agent;
     */
    private PushRPC push;

    /**
     * create new gschat client with reconnect timeout parameter
     */
    public GSChatClient(Device device){
        this.device = device;

        clientBuilder = new TCPClientBuilder(this);
    }

    public GSChatClient reconnect(int relay, TimeUnit unit) {

        clientBuilder.reconnect(relay,unit);

        return this;
    }

    public GSChatClient heartbeat(int relay, TimeUnit unit){

        this.relay = relay;
        this.unit = unit;

        return this;
    }

    /**
     * set the task executor
     * @param executor task executor
     * @return client self
     */
    public GSChatClient executor(Executor executor) {
        clientBuilder.dispacherExecutor(executor);
        return this;
    }

    /**
     * start and initialize gschat client
     */
    public void start() throws UnknownServiceException {

        tcpClient = clientBuilder.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new DHClientHandler(device, GSChatClient.this));

                ch.pipeline().addLast(new HeartbeatEchoHandler(relay, unit));
            }
        }).build();

        // register client service
        tcpClient.registerDispatcher(new ClientDispatcher(this));

        // bind service agents
        mailHub = new MailHubRPC(tcpClient,Register.getInstance().getID(MailHub.NAME));
        auth = new AuthRPC(tcpClient,Register.getInstance().getID(Auth.NAME));
        push = new PushRPC(tcpClient,Register.getInstance().getID(Push.NAME));
    }

    /**
     * close the client
     */
    public void close() {
        if(tcpClient != null){
            tcpClient.close();
        }
    }


    @Override
    public void push(Mail mail) throws Exception {

    }

    @Override
    public void notify(int SQID) throws Exception {

    }

    @Override
    public void deviceStateChanged(Device device, boolean online) throws Exception {

    }
}
