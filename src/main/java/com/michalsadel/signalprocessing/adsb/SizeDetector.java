package com.michalsadel.signalprocessing.adsb;

final class SizeDetector extends AdsbSignalDetector  {
    private final int preambleSize;

    SizeDetector(int preambleSize) {
        this.preambleSize = preambleSize;
    }

    @Override
    public boolean isCorrect(final float[] samples) {
        return samples.length == preambleSize;
    }
}
