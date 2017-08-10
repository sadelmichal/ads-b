package com.michalsadel.signalprocessing;

import com.google.common.primitives.*;
import org.slf4j.*;

import java.util.*;

//remove public wherever you can
public final class Preamble {
    private static final Logger log = LoggerFactory.getLogger(Preamble.class);
    private final int preambleSize;
    private final Set<PreambleObserver> observers = new HashSet<>();
    private final List<Float> buffer;
    private final Detector[] detectors;


    public Preamble(DetectorsFactory detectorsFactory) {
        assert detectorsFactory != null;
        this.preambleSize = detectorsFactory.minDetectorSampleSize();
        this.buffer = new ArrayList<>(this.preambleSize);
        this.detectors = detectorsFactory.detectors();
    }

    List<Float> getBuffer() {
        return Collections.unmodifiableList(buffer);
    }

    int getPreambleSize() {
        return preambleSize;
    }

    public void add(float magnitude) {
        buffer.add(magnitude);
        final float[] samples = Floats.toArray(buffer);
        if (buffer.size() == preambleSize) {
            buffer.remove(0);
        }
        detect(samples);
    }

    private void detect(float[] samples) {
        if (detectors == null) {
            return;
        }
        boolean detected = detectors.length > 0;
        for (Detector detector : detectors) {
            detected &= detector.isCorrect(samples);
            if (!detected) {
                return;
            }
        }
        if (detected) {
            observers.forEach(x -> x.detected(samples));
        }
    }

    public void attach(PreambleObserver preambleObserver) {
        observers.add(preambleObserver);
    }
}
