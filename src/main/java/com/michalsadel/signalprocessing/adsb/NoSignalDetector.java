package com.michalsadel.signalprocessing.adsb;

final class NoSignalDetector extends AdsbSignalDetector {
    @Override
    public boolean isCorrect(final float[] samples) {
        boolean result = true;
        for (int i : getHighSignalPosition()) {
            for (int k : getLowSignalPosition()) {
                result &= samples[i] > samples[k];
                if (!result) {
                    return result;
                }
            }
        }
        return result;
    }
}
