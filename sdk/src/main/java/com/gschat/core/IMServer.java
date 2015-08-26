package com.gschat.core;

import com.github.gschat.gsim.IMServerRPC;
import com.github.gschat.gsim.Message;
import com.github.gsdocker.gsrpc.CompleteHandler;
import com.gschat.sdk.GSError;
import com.gschat.sdk.GSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class IMServer {

    private final static Logger logger = LoggerFactory.getLogger("IMServer");

    private final CoreServiceBinder serviceBinder;

    private final IMServerRPC server;

    /**
     * create new IMServer
     *
     * @param server server
     */
    IMServer(CoreServiceBinder serviceBinder, IMServerRPC server) {
        this.serviceBinder = serviceBinder;
        this.server = server;
    }

    public void pollMessage(byte[] token, int receivedID) {
        try {

            logger.info("invoke poll ...");

            this.server.Poll(token, receivedID, new CompleteHandler() {
                @Override
                public void Complete(Exception e, Object... args) {
                    if (e != null) {
                        logger.error("invoke poll return error", e);
                    } else {
                        logger.info("invoke poll return success");
                    }
                }
            }, 5);

            logger.info("invoke poll -- success");

        } catch (Exception e) {
            logger.error("invoke poll error", e);
        }
    }

    /**
     * send message to im server
     */
    public void sendMessage(final String id, Message message) throws Exception {

        server.SendMessage(message, new CompleteHandler() {
            @Override
            public void Complete(Exception e, Object... args) {

                GSError errorCode = GSError.SUCCESS;

                if (e != null) {

                    logger.error("send message({}) -- failed",id,e);

                    if (e instanceof GSException) {
                        errorCode = ((GSException) e).getErrorCode();
                    } else {
                        errorCode = GSError.UNKNOWN_ERROR;
                    }
                } else {
                    logger.debug("send message({}) -- success",id);
                }

                IMServer.this.serviceBinder.onSendMessageComplete(errorCode,id);
            }
        }, 5);
    }
}