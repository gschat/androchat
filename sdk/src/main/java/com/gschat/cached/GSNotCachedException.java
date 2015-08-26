package com.gschat.cached;


public final class GSNotCachedException extends Exception{

    private final GSCached cached;

    public GSNotCachedException(GSCached cached) {

        this.cached = cached;
    }

    public GSCached getCached() {
        return cached;
    }
}
