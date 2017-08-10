package com.michalsadel.signalprocessing.adsb;

import com.michalsadel.signalprocessing.*;

public class AdsbDetectorsFactory extends DetectorsFactory {
    @Override
    public Detector[] detectors() {
        return new Detector[]{new SizeDetector(minDetectorSampleSize()), new CorrelationDetector(), new SignalToNoiseRatioDetector(),
                new NoSignalDetector(), new SimilarSignalDifferenceDetector()};
    }

    @Override
    public int minDetectorSampleSize() {
        return 16;
    }
}
