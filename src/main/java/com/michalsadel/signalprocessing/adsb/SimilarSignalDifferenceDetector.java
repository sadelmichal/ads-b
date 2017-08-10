package com.michalsadel.signalprocessing.adsb;

import com.google.common.primitives.*;

final class SimilarSignalDifferenceDetector extends AdsbSignalDetector {
    private final float differenceThreshold = 16;

    @Override
    public boolean isCorrect(final float[] samples) {
        final float[] highSamples = getHighSamples(samples);
        final float[] lowSamples = getLowSamples(samples);
        return Floats.max(highSamples) - Floats.min(highSamples) < differenceThreshold && Floats.max(lowSamples) - Floats.min(lowSamples) < differenceThreshold;

    }
}
