package com.michalsadel.commons;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;

import static org.junit.Assert.*;

@RunWith(BlockJUnit4ClassRunner.class)
public class LeftShiftedBufferTest {
    @Test(expected = IllegalArgumentException.class)
    public void bufferConstruction_SizeIsLessThanZero_ThrowsIllegalArgumentException() throws Exception {
        new LeftShiftedBuffer(0);
    }

    @Test
    public void bufferAdd_AlreadyAddedMoreThanBufferSize_IsRightShifted() throws Exception {
        final int bufferSize = 5;
        final LeftShiftedBuffer buffer = new LeftShiftedBuffer(bufferSize);
        for (int i = 0; i < bufferSize + 1; i++) {
            buffer.add(i);
        }
        assertArrayEquals(new float[]{1, 2, 3, 4, 5}, buffer.getBuffer(), 0);
    }
}
