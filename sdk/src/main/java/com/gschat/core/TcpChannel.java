package com.gschat.core;

import com.github.gsdocker.gsrpc.BufferReader;
import com.github.gsdocker.gsrpc.BufferWriter;
import com.github.gsdocker.gsrpc.Call;
import com.github.gsdocker.gsrpc.Callback;
import com.github.gsdocker.gsrpc.Code;
import com.github.gsdocker.gsrpc.Device;
import com.github.gsdocker.gsrpc.Dispatcher;
import com.github.gsdocker.gsrpc.ErrReturn;
import com.github.gsdocker.gsrpc.Message;
import com.github.gsdocker.gsrpc.Net;
import com.github.gsdocker.gsrpc.Return;
import com.github.gsdocker.gsrpc.State;
import com.github.gsdocker.gsrpc.WhoAmI;
import com.gschat.sdk.GSError;
import com.gschat.sdk.GSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channel;
import java.nio.channels.SocketChannel;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * the client to server's tcp channel
 */
abstract class TcpChannel implements Net, Dispatcher {

    /**
     * im server resolver
     */
    public interface RemoteResolver {
        Remote Resolve() throws Exception;
    }

    /**
     * the tcp channel rpc task class
     */
    private class Task {

        private final short id;
        private final Callback callback;

        private final Calendar deadline;

        public Task(short id, Callback callback) {

            this.id = id;

            this.callback = callback;

            this.deadline = Calendar.getInstance();

            this.deadline.add(Calendar.SECOND, callback.getTimeout());
        }

        public short getID() {
            return id;
        }

        public boolean expired() {
            return Calendar.getInstance().after(this.deadline);
        }

        public Callback getCallback() {
            return callback;
        }
    }

    private static final Logger logger = LoggerFactory.getLogger("TcpChannel");

    /**
     * client device name
     */
    private final Device device;

    /**
     * the chat room remote resolver
     */
    private final RemoteResolver remoteResolver;

    /**
     * tcp client channel state
     */
    private volatile State state = State.Disconnected;

    /**
     * the socket channel connect to remote gateway.
     */
    private volatile SocketChannel channel;

    /**
     * rpc send queue
     */
    private LinkedBlockingQueue<Message> sendQ = new LinkedBlockingQueue<>();

    /**
     * The rpc wait Q
     */
    private final ConcurrentHashMap<Short, Task> waitQ = new ConcurrentHashMap<>();

    /**
     * sequence id generate
     */
    private AtomicInteger idGen = new AtomicInteger(0);


    /**
     * update time of last heartbeat from server
     */
    private Calendar updateTime;

    /**
     * create new tcp client channel to im server
     * @param device local device type
     * @param remoteResolver remote service resolver
     */
    TcpChannel(Device device, RemoteResolver remoteResolver) {
        this.device = device;
        this.remoteResolver = remoteResolver;
    }


    /**
     * try handle timeout task
     */
    public void timeoutHandle() {

        for (final Task task : waitQ.values()) {
            if (task.expired()) {

                if (waitQ.remove(task.getID(), task)) {

                    logger.debug("task timeout :{}", task.getID());

                    task.getCallback().Return(new GSException("RPC timeout",GSError.TIMEOUT), null);
                }
            }
        }

        ping();

        Calendar now = Calendar.getInstance();

        now.add(Calendar.SECOND, -20);

        if(now.after(this.updateTime)) {

            logger.warn("heartbeat timeout ...");

            this.disconnect();
            this.connect();
        }

    }

    public void ping() {
        try {

            Message message = new Message();

            message.setCode(Code.Heartbeat);

            this.sendMessage(message);

        } catch (InterruptedException e) {
            logger.error("send heartbeat error",e);
        }
    }


    /**
     * create new send sequence id
     *
     * @return sequence id
     */
    private short id() {


        while (true) {
            short id = (short) idGen.incrementAndGet();

            if (waitQ.containsKey(id)) {
                continue;
            }

            return id;
        }
    }

    /**
     * implement Net interface
     *
     * @param call     call message
     * @param callback callback handler
     * @throws Exception
     */
    @Override
    public void send(Call call, Callback callback) throws Exception {

        connect();

        call.setID(this.id());

        BufferWriter writer = new BufferWriter();

        call.Marshal(writer);

        Message message = new Message();

        message.setCode(Code.Call);

        message.setContent(writer.Content());

        // register wait group
        this.waitQ.putIfAbsent(call.getID(), new Task(call.getID(), callback));
        // send message
        sendMessage(message);
    }

    private void sendMessage(Message message) throws InterruptedException {

        this.sendQ.put(message);
    }

    abstract void stateChanged(State state, Exception e);

    public void close() {

        synchronized (this) {
            this.state = State.Closed;

            if (this.channel != null) {
                try {
                    this.channel.close();
                } catch (IOException e) {
                    logger.error("close channel {} error", this.channel, e);
                }
                this.channel = null;
            }
        }
    }

    /**
     * disconnect from gw
     */
    public void disconnect() {
        synchronized (this) {
            this.state = State.Disconnected;

            if (this.channel != null) {
                try {
                    this.channel.close();
                } catch (IOException e) {
                    logger.error("close channel {} error", this.channel, e);
                }
                this.channel = null;
            }

            this.stateChanged(this.state,null);
        }
    }


    public void connect() {

        try {
            synchronized (this) {

                if (this.state != State.Disconnected) {

                    logger.warn("skip connect im server -- invalid state({})", this.state);

                    return;
                }

                logger.debug("connect im server current state({})",this.state);

                if (this.channel != null) {
                    this.channel.close();
                    this.channel = null;
                }

                this.updateTime = Calendar.getInstance();

                this.state = State.Connecting;

                channel = SocketChannel.open();

                stateChanged(this.state, null);

                new Thread(new Runnable() {
                @Override
                public void run() {
                    TcpChannel.this.doConnect(channel);
                }
            }).start();
        }

            logger.warn("start thread to connect im server");

        } catch (Exception e) {

            handleException(null, e);
        }


    }

    private void doConnect(SocketChannel channel) {

        InetSocketAddress remote;

        DHKey dhKey;

        try {
            final Remote chatRoomRemote = this.remoteResolver.Resolve();

            remote = chatRoomRemote.getAddress();

            dhKey = chatRoomRemote.getDhKey();

        } catch (Exception e) {

            logger.error("resolve remote -- failed", e.getMessage());

            handleException(channel, e);

            return;
        }

        try {

            logger.debug("begin connect to {}", remote.toString());

            channel.socket().connect(remote, 5000);

            logger.debug("connect to {} -- success", remote.toString());

            logger.debug("send handshake to {}", remote.toString());

            WhoAmI whoAmI = new WhoAmI();

            whoAmI.setID(this.device);

            BigInteger sendE = dhKey.Exchange();

            logger.debug("send exchanged key {}", sendE);

            whoAmI.setContext(sendE.toString().getBytes("UTF8"));

            Message message = new Message();

            message.setCode(Code.WhoAmI);

            BufferWriter writer = new BufferWriter();

            whoAmI.Marshal(writer);

            message.setContent(writer.Content());

            writer.reset();

            message.Marshal(writer);

            writeMessage(channel, writer.Content());

            logger.debug("send handshake to {} -- success", remote.toString());

            logger.debug("recv handshake from {}", remote.toString());

            BufferReader reader = new BufferReader(readMessage(channel));

            message.Unmarshal(reader);

            BigInteger E = new BigInteger(new String(message.getContent(), "UTF-8"));

            logger.debug("recv exchanged key {}", E);

            long sharedKey = dhKey.SharedKey(E).longValue();

            logger.debug("recv handshake from {} -- success, sharedKey is {}", remote.toString(), sharedKey);

            ByteBuffer buff = ByteBuffer.allocate(8);

            buff.order(ByteOrder.BIG_ENDIAN);

            buff.putLong(sharedKey);

            channel.socket().setSoTimeout(3000);

            connected(channel, buff.array());

        } catch (Exception e) {

            logger.error("connect to {} -- failed\n{}", remote.toString(), e.getMessage());

            handleException(channel, e);
        }
    }

    private byte[] readFull(SocketChannel channel, int length) throws IOException {

        byte[] buff = new byte[length];

        int offset = 0;

        while (true) {
            int readBytes = channel.read(ByteBuffer.wrap(buff, offset, buff.length - offset));

            if (readBytes == -1) {
                throw new IOException("EOF");
            }

            offset += readBytes;

            if (offset == buff.length) {
                break;
            }
        }

        return buff;

    }

    private byte[] readMessage(SocketChannel channel) throws IOException {

        ByteBuffer buff = ByteBuffer.wrap(readFull(channel, 2));

        buff.order(ByteOrder.LITTLE_ENDIAN);

        short length = buff.getShort();

        return readFull(channel, length);
    }

    private void writeMessage(SocketChannel channel, byte[] block) throws Exception {

        ByteBuffer buff = ByteBuffer.allocate(block.length + 2);

        buff.order(ByteOrder.LITTLE_ENDIAN);

        buff.putShort((short) block.length);

        buff.put(block);

        int sendBytes = channel.write(ByteBuffer.wrap(buff.array()));

        logger.debug("send message length {}", sendBytes);
    }

    private void connected(final SocketChannel channel, final byte[] sharedKey) {

        synchronized (this) {

            if (this.state == State.Connecting && channel == this.channel) {

                this.state = State.Connected;

                stateChanged(this.state, null);

                final Thread sendThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        TcpChannel.this.sendLoop(channel, sharedKey);
                    }
                });

                sendThread.start();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        TcpChannel.this.recvLoop(sendThread,channel, sharedKey);
                    }
                }).start();

            } else {
                try {
                    logger.debug("closed unused channel");
                    channel.close();
                } catch (IOException e) {
                    logger.error("close channel error",e);
                }
            }
        }
    }

    private void sendLoop(SocketChannel channel, byte[] rawKey) {

        try {

            SecureRandom random = new SecureRandom();

            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

            DESKeySpec dks = new DESKeySpec(rawKey);

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

            SecretKey sharedKey = keyFactory.generateSecret(dks);

            cipher.init(Cipher.ENCRYPT_MODE, sharedKey, random);

            BufferWriter writer = new BufferWriter();

            while (true) {

                Message message = sendQ.take();

                if (!channel.isOpen()) {
                    logger.debug("exit sendLoop {}",channel);
                    return;
                }

                logger.debug("send message : {}", Arrays.toString(message.getContent()));

                message.setContent(cipher.doFinal(message.getContent()));

                writer.reset();

                message.Marshal(writer);

                writeMessage(channel, writer.Content());

                logger.debug("send message -- success");

                channel.socket().getOutputStream().flush();
            }
        } catch (Exception e) {
            handleException(channel, e);
        }


        logger.debug("exit send loop {}", channel);
    }

    private void recvLoop(Thread sendThread,SocketChannel channel, byte[] rawKey) {

        try {

            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

            DESKeySpec dks = new DESKeySpec(rawKey);

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

            SecretKey sharedKey = keyFactory.generateSecret(dks);

            cipher.init(Cipher.DECRYPT_MODE, sharedKey);

            while(true) {

                BufferReader reader = new BufferReader(readMessage(channel));

                Message message = new Message();

                message.Unmarshal(reader);

                if(message.getCode() == Code.Heartbeat) {
                    logger.debug("received gw heartbeat");

                    updateTime = Calendar.getInstance();

                    try {
                        this.sendMessage(message);
                    } catch (InterruptedException e) {
                        logger.error("send heartbeat error",e);
                    }

                    continue;
                }

                message.setContent(cipher.doFinal(message.getContent()));

                logger.trace("received message {}",message.getCode());

                dispatch(message);
            }

        } catch (Exception e) {
            sendThread.interrupt();
            handleException(channel, e);
        }

        logger.debug("exit rev loop {}", channel);
    }

    private void dispatch(Message message) throws Exception {

        logger.debug("dispatch {}", message.getCode());

        switch (message.getCode()) {
            case Return: {
                final Return callReturn = new Return();

                callReturn.Unmarshal(new BufferReader(message.getContent()));

                final Task task = this.waitQ.remove(callReturn.getID());

                if (task != null) {
                    task.getCallback().Return(null, callReturn);
                }

                break;
            }
            case ErrReturn: {
                final ErrReturn errReturn = new ErrReturn();

                errReturn.Unmarshal(new BufferReader(message.getContent()));

                final Task task = this.waitQ.remove(errReturn.getID());

                if (task != null) {
                    task.getCallback().Return(
                            new Exception(String.format(
                                    "remote error :\n\tcatalog: %s \n\tcode:%d",
                                    UUID.nameUUIDFromBytes(errReturn.getUUID()),
                                    errReturn.getCode()
                            )), null);
                }

                break;
            }

            case Call: {
                final Call call = new Call();

                call.Unmarshal(new BufferReader(message.getContent()));

                TcpChannel.this.handleCall(call);

                break;
            }
        }
    }

    private void handleCall(Call call) {

        Return callReturn = null;

        try {

            callReturn = TcpChannel.this.Dispatch(call);

        } catch (Exception e) {
            logger.error("dispatch call error :{}", e);
        }

        if (callReturn == null) {

            callReturn = new Return();

            callReturn.setID(call.getID());

            callReturn.setService(call.getService());
        }

        Message message = new Message();

        message.setCode(Code.Return);

        BufferWriter writer = new BufferWriter();

        try {

            callReturn.Marshal(writer);

            message.setContent(writer.Content());

            sendMessage(message);

        } catch (Exception e) {

            logger.error("dispatch call error :{}", e);
        }
    }


    /**
     * handle client io exceptions
     *
     * @param channel   exception throws from
     * @param exception exception object
     */
    private void handleException(SocketChannel channel, Exception exception) {


        synchronized (this) {

            try {

                if (channel != null) {
                    channel.close();
                }
            } catch (IOException e) {
                logger.error("close channel {} exception", channel, exception);
            }

            logger.error("{} catch error",channel,exception);

            logger.error("current channel {}",this.channel);

            if (this.channel == channel && this.state != State.Closed) {

                this.channel = null;

                this.state = State.Disconnected;

                //this.sendQ.clear();

                stateChanged(this.state, exception);
            }
        }
    }
}
