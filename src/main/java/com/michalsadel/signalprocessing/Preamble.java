package com.michalsadel.signalprocessing;

import com.michalsadel.commons.*;

import java.util.*;

final class Preamble {
    private final LeftShiftedBuffer buffer;
    private final Detector[] detectors;
    private final GeneralObservable<SampleObserver> sampleObservable = new GeneralObservable<>();

    public Preamble(DetectorsFactory detectorsFactory, int preambleSize) {
        if (detectorsFactory == null) {
            throw new IllegalArgumentException();
        }
        this.buffer = new LeftShiftedBuffer(preambleSize);
        this.detectors = detectorsFactory.detectors();
    }

    public void add(float magnitude) {
        buffer.add(magnitude);
        detect(buffer.getBuffer());
    }

    private void detect(float[] samples) {
        if (detectors == null) {
            return;
        }
        boolean detected = detectors.length > 0;
        for (Detector detector : detectors) {
            //check inspections
            detected &= detector.isCorrect(samples);
            if (!detected) {
                return;
            }
        }
        if (detected) {
            final String detectionId = UUID.randomUUID().toString();
            sampleObservable.getObservers().forEach(x -> x.detected(samples, detectionId));
        }
    }

    public void attach(SampleObserver observer) {
        sampleObservable.attach(observer);
    }
}
