package com.michalsadel.signalprocessing.adsb;

final class CorrelationDetector extends AdsbSignalDetector {
    @Override
    public boolean isCorrect(final float[] samples) {
        if (samples.length < 11) {
            return false;
        }
        float highSignalAverage = highSignalAverage(samples);
        return samples[0] > samples[1] && samples[1] < samples[2] && samples[2] > samples[3] && //first tooth
                samples[3] < highSignalAverage && samples[4] < highSignalAverage && samples[5] < highSignalAverage && samples[6] < highSignalAverage && //silence
                samples[7] > samples[8] && samples[8] < samples[9] && samples[9] > samples[10]; //second tooth
    }

}
