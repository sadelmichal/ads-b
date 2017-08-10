package com.michalsadel.signalprocessing.adsb;

final class SignalToNoiseRatioDetector extends AdsbSignalDetector {

    @Override
    public boolean isCorrect(final float[] samples) {
        return 20 * Math.log10(highSignalAverage(samples) / lowSignalAverage(samples)) > 4;
    }
}
