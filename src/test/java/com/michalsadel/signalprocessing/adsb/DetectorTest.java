package com.michalsadel.signalprocessing.adsb;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import static org.junit.Assert.*;

@RunWith(BlockJUnit4ClassRunner.class)
public class DetectorTest {
    //MethodUnderTest_Scenario_ExpectedBehavior
    @Test
    public void correlationDetectorIsCorrect_ImproperPreamblePattern_ReturnFalse() {
        //Proper pattern is HLHLLLLHLHLLLLLL
        assertFalse(new CorrelationDetector().isCorrect(new float[]{1, 1, 2, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 1}));
    }

    @Test
    public void correlationDetectorIsCorrect_NotEnoughSamples_ReturnFalse() {
        assertFalse(new CorrelationDetector().isCorrect(new float[]{2, 1, 2, 1, 1, 1, 1, 2, 1, 2}));
    }

    @Test
    public void correlationDetectorIsCorrect_AverageOfHighSpikesIsLessThanEachSampleInSilencePosition_ReturnFalse() {
        assertFalse(new CorrelationDetector().isCorrect(new float[]{2, 1f, 2, 1.9f, 1.9f, 1.9f, 1.9f, 2, 1, 1.1f, 2, 1}));
    }

    @Test
    public void signalToNoiseRatioDetectorIsCorrect_AmplitudeRatioIsLowerThan4Decibels_ReturnFalse() {
        assertFalse(new SignalToNoiseRatioDetector().isCorrect(new float[]{1.58f, 1f, 1.58f, 1.9f, 1.0f, 1.0f, 1.9f, 1.58f, 1, 1.58f, 2, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f}));
    }

    @Test
    public void noSignalDetectorIsCorrect_HighSignalPositionValueIsLessThanLowSignalPositionValue_ReturnFalse() {
        assertFalse(new NoSignalDetector().isCorrect(new float[]{2, 1, 2, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 3, 1, 1}));
    }

    @Test
    public void similarSignalDetectorIsCorrect_DifferenceBetweenMaxAndMinValueOfHighOrLowIsLessThanDefinedThreshold_ReturnFalse() {
        final SimilarSignalDifferenceDetector detector = new SimilarSignalDifferenceDetector();
        assertFalse("High signal is similar", detector.isCorrect(new float[]{15, 1, 31, 1, 1, 1, 1, 15, 1, 15, 1, 1, 1, 1, 1, 1}));
        assertFalse("Low signal is similar", detector.isCorrect(new float[]{15, 1, 15, 1, 1, 1, 1, 15, 1, 15, 1, 1, 17, 1, 1, 1}));
    }
}