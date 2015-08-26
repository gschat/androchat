package com.gschat.core;

import com.github.gschat.gsim.IMClientDispatcher;
import com.github.gsdocker.gsrpc.Call;
import com.github.gsdocker.gsrpc.Dispatcher;
import com.github.gsdocker.gsrpc.Return;
import com.github.gsdocker.gsrpc.State;
import com.gschat.ipc.IPCNetEvent;
import com.gschat.sdk.GSError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.net.InetSocketAddress;

final class IMChannel extends TcpChannel {

    /**
     * IMChannel logger
     */
    private static final Logger logger = LoggerFactory.getLogger("IMChannel");

    private final CoreServiceBinder coreServiceBinder;

    private final Dispatcher dispatcher;

    IMChannel(CoreServiceBinder coreServiceBinder) {

        super(SystemInfo.getDevice(coreServiceBinder.getContext()), new RemoteResolver() {
            @Override
            public Remote Resolve() throws Exception {
                return new Remote(
                        new InetSocketAddress("218.90.199.9", 13512),
                        new DHKey(
                                new BigInteger("6849211231874234332173554215962568648211715948614349192108760170867674332076420634857278025209099493881977517436387566623834457627945222750416199306671083"),
                                new BigInteger("13196520348498300509170571968898643110806720751219744788129636326922565480984492185368038375211941297871289403061486510064429072584259746910423138674192557")
                        )
                );
            }
        });

        this.coreServiceBinder = coreServiceBinder;

        this.dispatcher = new IMClientDispatcher(this.coreServiceBinder);
    }

    @Override
    void stateChanged(State state, Exception e) {

        logger.debug("{} im channel state changed {}",coreServiceBinder.getClientURI(),state);

        switch (state){

            case Connecting:
                coreServiceBinder.onNetStateChanged(new IPCNetEvent(State.Connecting,GSError.SUCCESS));
                break;
            case Closed:
                coreServiceBinder.onNetStateChanged(new IPCNetEvent(State.Closed,GSError.SUCCESS));
                break;
            case Disconnected:

                coreServiceBinder.onNetStateChanged(new IPCNetEvent(State.Disconnected,GSError.SUCCESS));

                AndroidTimer.startReconnect(coreServiceBinder.getContext(), coreServiceBinder.getClientURI());

                break;
            case Connected:
                coreServiceBinder.onNetStateChanged(new IPCNetEvent(State.Connected,GSError.SUCCESS));
                break;
        }
    }

    @Override
    public Return Dispatch(Call call) throws Exception {
        return this.dispatcher.Dispatch(call);
    }
}