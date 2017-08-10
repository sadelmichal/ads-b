package com.michalsadel.signalprocessing;

public interface Detector {
    boolean isCorrect(final float[] samples);
}
