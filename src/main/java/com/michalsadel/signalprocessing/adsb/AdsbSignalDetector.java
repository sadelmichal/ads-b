package com.michalsadel.signalprocessing.adsb;

import com.michalsadel.signalprocessing.*;

abstract class AdsbSignalDetector implements Detector {
    private final int[] highSignalPosition = new int[]{0, 2, 7, 9};
    private final int[] lowSignalPosition = new int[]{4, 5, 11, 12, 13, 14};

    private float signalSum(final float[] samples, final int[] positions) {
        float signal = 0;
        for (int i : positions) {
            signal += samples[i];
        }
        return signal;
    }

    protected final float highSignalAverage(final float[] samples) {
        return highSignalSum(samples) / highSignalPosition.length;
    }

    protected final float lowSignalAverage(final float[] samples) {
        return lowSignalSum(samples) / lowSignalPosition.length;
    }

    protected final float highSignalSum(final float[] samples) {
        return signalSum(samples, highSignalPosition);
    }

    protected final float lowSignalSum(final float[] samples) {
        return signalSum(samples, lowSignalPosition);
    }

    protected final int[] getHighSignalPosition() {
        return highSignalPosition;
    }

    protected final int[] getLowSignalPosition() {
        return lowSignalPosition;
    }

    private float[] signalSamples(final float[] samples, final int[] positions) {
        float[] result = new float[positions.length];
        for (int i = 0; i < positions.length; i++) {
            result[i] = samples[positions[i]];
        }
        return result;
    }


    protected final float[] getHighSamples(final float[] samples) {
        return signalSamples(samples, highSignalPosition);
    }

    protected final float[] getLowSamples(final float[] samples) {
        return signalSamples(samples, lowSignalPosition);
    }
}
