package com.michalsadel.signalprocessing;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import java.util.*;
import java.util.concurrent.atomic.*;

import static org.junit.Assert.*;

@RunWith(BlockJUnit4ClassRunner.class)
public class PreambleTest {
    @Test
    public void preambleDetect_MultipleObserversNotification_ShouldHaveTheSameDetectionId() throws Exception {
        AtomicInteger atomicInteger = new AtomicInteger();
        List<String> detectedIds = new ArrayList<>();
        final Preamble preamble = new Preamble(new DetectorsFactory() {
            @Override
            public Detector[] detectors() {
                return new Detector[]{samples -> true};
            }
        }, 1);
        preamble.attach((samples, detectionId) -> {
            atomicInteger.incrementAndGet();
            detectedIds.add(detectionId);
        });
        preamble.attach((samples, detectionId) -> {
            atomicInteger.incrementAndGet();
            detectedIds.add(detectionId);
        });
        preamble.add(1);
        assertEquals(2, atomicInteger.get());
        assertEquals(2, detectedIds.size());
        assertEquals(1, detectedIds.stream().distinct().limit(2).count());
    }
}