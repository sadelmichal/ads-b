package com.michalsadel.signalprocessing;

public abstract class DetectorsFactory {
    public abstract Detector[] detectors();

    public abstract int minDetectorSampleSize();
}
