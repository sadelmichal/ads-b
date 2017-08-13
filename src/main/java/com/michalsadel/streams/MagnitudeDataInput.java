package com.michalsadel.streams;

import java.io.*;

public interface MagnitudeDataInput extends Closeable {
    float readMagnitude() throws IOException;
}
