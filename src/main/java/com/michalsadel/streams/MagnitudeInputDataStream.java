package com.michalsadel.streams;

import java.io.*;

public final class MagnitudeInputDataStream implements MagnitudeDataInput {
    private final IQInputDataStream in;

    public MagnitudeInputDataStream(IQInputDataStream in) {
        this.in = in;
    }

    @Override
    public void close() throws IOException {
        in.close();
    }

    @Override
    public float readMagnitude() throws IOException {
        final IQData iqData = in.readIQData();
        final float magnitude = (iqData != null) ? (float) Math.sqrt(iqData.getISquare() + iqData.getQSquare()) : -1;
        return magnitude;
    }
}
