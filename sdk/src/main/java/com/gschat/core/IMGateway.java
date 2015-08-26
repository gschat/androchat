package com.gschat.core;

import com.github.gschat.gsim.IMGatewayRPC;
import com.github.gsdocker.gsrpc.CompleteHandler;
import com.gschat.sdk.GSError;
import com.gschat.sdk.GSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * The im gateway facade
 */
public final class IMGateway {
    private final static Logger logger = LoggerFactory.getLogger("IMGateway");

    private final CoreServiceBinder serviceBinder;

    private final IMGatewayRPC gateway;

    public IMGateway(CoreServiceBinder serviceBinder, IMGatewayRPC gateway) {

        this.serviceBinder = serviceBinder;

        this.gateway = gateway;
    }

    public void fastLogin(final String username, final byte[] token) throws Exception {

        logger.info("start fast login({}) : {}",username, Arrays.toString(token));

        gateway.FastLogin(token, new CompleteHandler() {
            @Override
            public void Complete(Exception e, Object... args) {

                GSError errorCode = GSError.SUCCESS;

                if (e != null) {
                    if (e instanceof GSException) {
                        errorCode = ((GSException) e).getErrorCode();
                    } else {
                        errorCode = GSError.UNKNOWN_ERROR;
                    }
                }

                serviceBinder.onLoginComplete(errorCode, token);
            }
        }, 5);
    }

    public void Login(final String username) throws Exception {


        gateway.Login(username, new CompleteHandler() {
            @Override
            public void Complete(Exception e, Object... args) {

                GSError errorCode = GSError.SUCCESS;

                if (e != null) {

                    logger.error("login({}) -- failed", username, e);

                    if (e instanceof GSException) {
                        errorCode = ((GSException) e).getErrorCode();
                    } else {
                        errorCode = GSError.UNKNOWN_ERROR;
                    }

                    serviceBinder.onLoginComplete(errorCode, null);

                } else {
                    logger.debug("login({}) -- success",username);

                    serviceBinder.onLoginComplete(errorCode, (byte[]) args[0]);
                }

            }
        }, 5);
    }

    public void Logoff(final byte[] token) throws Exception {
        gateway.Logoff(token, new CompleteHandler() {
            @Override
            public void Complete(Exception e, Object... args) {

                GSError errorCode = GSError.SUCCESS;

                if (e != null) {

                    logger.error("logoff -- failed", e);

                    if (e instanceof GSException) {
                        errorCode = ((GSException) e).getErrorCode();
                    } else {
                        errorCode = GSError.UNKNOWN_ERROR;
                    }

                } else {
                    logger.debug("logoff -- success");
                }

                serviceBinder.onLogoffComplete(errorCode);
            }
        }, 5);
    }
}