package com.michalsadel.streams;

import java.io.*;

public final class IQInputDataStream implements Closeable {
    private final DataInputStream in;

    public IQInputDataStream(InputStream in) {
        this.in = new DataInputStream(in);
    }

    public final IQData readIQData() throws IOException {
        try {
            final int ch1 = in.readUnsignedByte();
            final int ch2 = in.readUnsignedByte();
            return new IQData(ch1 - 127.5f, ch2 - 127.5f);
        } catch (EOFException eof) {
            return null;
        }
    }

    @Override
    public void close() throws IOException {
        in.close();
    }
}
