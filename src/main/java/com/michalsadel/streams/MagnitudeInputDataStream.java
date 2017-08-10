package com.michalsadel.streams;

import java.io.*;

public final class MagnitudeInputDataStream implements Closeable {
    private final IQInputDataStream in;
    private final Writer dump;

    public MagnitudeInputDataStream(IQInputDataStream in) {
        this(in, null);
    }

    public MagnitudeInputDataStream(IQInputDataStream in, Writer dumpFile) {
        this.in = in;
        this.dump = dumpFile;
    }

    public float readMagnitude() throws IOException {
        final IQData iqData = in.readIQData();
        final float magnitude = (iqData != null) ? (float) Math.sqrt(iqData.getISquare() + iqData.getQSquare()) : -1;
        if (dump != null && magnitude != -1) {
            dump.write(iqData.toString() + " mg{" + magnitude + "}\n");
        }
        return magnitude;
    }

    @Override
    public void close() throws IOException {
        in.close();
        if (dump != null) {
            dump.close();
        }
    }
}
