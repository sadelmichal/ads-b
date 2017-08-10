package com.michalsadel.signalprocessing;

import com.google.common.primitives.*;
import com.michalsadel.signalprocessing.adsb.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.mockito.*;

import java.util.*;

import static org.mockito.Mockito.*;

@RunWith(BlockJUnit4ClassRunner.class)
public class AdsbTest {
    @Test
    public void sizeDetectorShouldCauseAdsbToBeNotifiedThatSampleIsCorrect() {
        float[] samples = new float[]{1f, 2f, 3f};
        Preamble p = new Preamble(new AdsbDetectorsFactory());
        final Adsb adsb = Mockito.mock(Adsb.class);
        p.attach(adsb);
        for (float sample : samples) {
            p.add(sample);
        }
        final List<Float> reversed = Floats.asList(samples);
        Collections.reverse(reversed);
        verify(adsb, times(1)).detected(Floats.toArray(reversed));
    }
}