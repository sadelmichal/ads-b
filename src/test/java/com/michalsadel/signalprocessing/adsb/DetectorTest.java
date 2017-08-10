package com.michalsadel.signalprocessing.adsb;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import static org.junit.Assert.*;

@RunWith(BlockJUnit4ClassRunner.class)
public class DetectorTest {

    @Test
    public void sizeDetectorShouldReturnTrueOnlyWhenPreambleSizeIsEqualToSampleBuffer() {
        final SizeDetector detector = new SizeDetector(3);
        assertFalse("Sample size is less than preamble size", detector.isCorrect(new float[1]));
        assertFalse("Sample size is greater than preamble size", detector.isCorrect(new float[4]));
        assertTrue("Sample size is not of preamble size", detector.isCorrect(new float[3]));
    }

    @Test
    public void correlationDetectorShouldReturnTrueOnlyWhenPreambleHasProperPattern() {
        final CorrelationDetector detector = new CorrelationDetector();
        assertTrue("Sample has improper pattern", detector.isCorrect(new float[]{2, 1, 2, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 1}));
        assertFalse("Sample has proper pattern", detector.isCorrect(new float[]{1, 1, 2, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 1}));
    }

    @Test
    public void correlationDetectorShouldReturnFalseWhenNotEnoughSamplesProvided() {
        final CorrelationDetector detector = new CorrelationDetector();
        assertFalse(detector.isCorrect(new float[]{2, 1, 2, 1, 1, 1, 1, 2, 1, 2}));
    }

    @Test
    public void correlationDetectorShouldReturnFalseWhenAverageOfHighSpikesIsLessThanEachSampleInSilencePosition() {
        final CorrelationDetector detector = new CorrelationDetector();
        assertFalse(detector.isCorrect(new float[]{2, 1f, 2, 1.9f, 1.9f, 1.9f, 1.9f, 2, 1, 1.1f, 2, 1}));
    }

    @Test
    public void signalToNoiseRatioDetectorShouldReturnFalseWhenRatioIsLowerThan4Decibels() {
        final SignalToNoiseRatioDetector detector = new SignalToNoiseRatioDetector();
        assertFalse(detector.isCorrect(new float[]{1.58f, 1f, 1.58f, 1.9f, 1.0f, 1.0f, 1.9f, 1.58f, 1, 1.58f, 2, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f}));
    }

    @Test
    public void noSignalDetectorShouldReturnTrueOnlyWhenInHighSignalPositionValueIsGreaterThanEachAmplitudeInLowSignalPositions() {
        final NoSignalDetector detector = new NoSignalDetector();
        assertTrue("High is not always high :)", detector.isCorrect(new float[]{2, 1, 2, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 1}));
        assertFalse("High is always high", detector.isCorrect(new float[]{2, 1, 2, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 3, 1, 1}));
    }

    @Test
    public void similarSignalDetectorShouldReturnTrueWhenDifferenceBetweenMaxAndMinValueOfHighOrLowIsLessThanThreshold() {
        final SimilarSignalDifferenceDetector detector = new SimilarSignalDifferenceDetector();
        assertFalse("High signal is similar", detector.isCorrect(new float[]{15, 1, 31, 1, 1, 1, 1, 15, 1, 15, 1, 1, 1, 1, 1, 1}));
        assertFalse("Low signal is similar", detector.isCorrect(new float[]{15, 1, 15, 1, 1, 1, 1, 15, 1, 15, 1, 1, 17, 1, 1, 1}));
    }
}