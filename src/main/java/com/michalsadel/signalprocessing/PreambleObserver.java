package com.michalsadel.signalprocessing;

@FunctionalInterface
public interface PreambleObserver {
    void detected(float[] samples);
}
