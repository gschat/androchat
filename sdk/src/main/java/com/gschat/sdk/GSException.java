package com.gschat.sdk;


public final class GSException extends Exception{

    private final GSError errorCode;

    public GSException(GSError errorCode){

        super(errorCode.toString());

        this.errorCode = errorCode;
    }

    public GSException(String message, GSError errorCode) {

        super(message);

        this.errorCode = errorCode;
    }

    public GSError getErrorCode() {
        return errorCode;
    }
}
