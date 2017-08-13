package com.michalsadel.signalprocessing;

@FunctionalInterface
public interface MessageObserver<T> {
    void decoded(T message, String detectionId);
}
