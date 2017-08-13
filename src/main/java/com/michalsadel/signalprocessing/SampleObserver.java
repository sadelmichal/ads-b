package com.michalsadel.signalprocessing;

@FunctionalInterface
public interface SampleObserver {
    void detected(float[] samples, String detectionId);
}
