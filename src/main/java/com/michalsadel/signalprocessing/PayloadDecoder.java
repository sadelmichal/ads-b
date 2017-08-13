package com.michalsadel.signalprocessing;

public interface PayloadDecoder<T> {
    int size();

    T decode(float[] payload);
}
