package com.michalsadel.signalprocessing;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.mockito.*;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(BlockJUnit4ClassRunner.class)
public class PreambleTest {
    @Test
    public void addingMagnitudeToPreambleShouldShiftLeftValuesWhenDefaultCapacityIsOverflown() throws Exception {
        final DetectorsFactory mock = Mockito.mock(DetectorsFactory.class);
        when(mock.minDetectorSampleSize()).thenReturn(11);
        final Preamble preamble = new Preamble(mock);
        for (int i = 0; i < preamble.getPreambleSize() + 1; i++) {
            preamble.add(i);
        }
        assertEquals(Arrays.asList(11f, 10f, 9f, 8f, 7f, 6f, 5f, 4f, 3f, 2f), preamble.getBuffer());
    }
}