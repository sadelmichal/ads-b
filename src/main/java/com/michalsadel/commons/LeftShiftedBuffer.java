package com.michalsadel.commons;

import java.util.*;

public final class LeftShiftedBuffer {
    private final float[] buffer;
    private volatile int index = 0;

    public LeftShiftedBuffer(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Buffer size must be greater than 0");
        }
        this.buffer = new float[size];
    }

    public void add(float value) {
        if (index == buffer.length) {
            System.arraycopy(buffer, 1, buffer, 0, buffer.length - 1);
            buffer[index - 1] = value;
        } else {
            buffer[index++] = value;
        }
    }

    public int size() {
        return index;
    }

    public float[] getBuffer() {
        return Arrays.copyOf(buffer, buffer.length);
    }
}
