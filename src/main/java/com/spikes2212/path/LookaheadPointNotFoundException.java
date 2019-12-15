package com.spikes2212.path;

public class LookaheadPointNotFoundException extends Exception {

    public LookaheadPointNotFoundException() {
        super();
    }

    public LookaheadPointNotFoundException(String message) {
        super(message);
    }

    public LookaheadPointNotFoundException(Throwable throwable) { super(throwable); }

    public LookaheadPointNotFoundException(String message, Throwable throwable) { super(message, throwable);}
}
