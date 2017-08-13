package com.michalsadel.signalprocessing;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import java.util.concurrent.atomic.*;

import static org.junit.Assert.*;

@RunWith(BlockJUnit4ClassRunner.class)
public class PayloadTest {
    private static final int payloadSize = 5;
    private PayloadDecoder payloadDecoder = new PayloadDecoder() {
        @Override
        public int size() {
            return payloadSize;
        }

        @Override
        public Object decode(float[] payload) {
            return null;
        }
    };

    //MethodName_ExpectedBehavior_StateUnderTest
    //withdrawMoney_ThrowsException_IfAccountIsInvalid
    @Test
    public void Add_CallsObserverWithSamplesStartedAtOne_WhenIndexOfDetectionIsZero() throws Exception {
        final AtomicInteger detected = new AtomicInteger();
        Payload p = new Payload(payloadDecoder);
        p.attach((SampleObserver) (samples, detectionId) -> {
            detected.incrementAndGet();
            assertArrayEquals(new float[]{1, 2, 3, 4, 5}, samples, 0);
        });
        for (int i = 0; i < payloadSize + 1; i++) {
            p.add(i);
            if (i == 0) {
                p.detected(new float[0], "ID");
            }
        }
        assertEquals(1, detected.get());
    }
}