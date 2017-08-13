package com.michalsadel.signalprocessing.adsb;

import com.michalsadel.signalprocessing.*;

public class ManchesterDecoder implements PayloadDecoder<byte[]> {
    private final int messageSize = 112;

    @Override
    public int size() {
        return messageSize * 2;
    }


    @Override
    public byte[] decode(float[] payload) {
        byte[] result = new byte[(messageSize / 8)];
        for (int i = 0, messageLength = payload.length; i < messageLength; i += 2) {
            if (payload[i] > payload[i + 1]) {
                result[i / 2 / 8] |= 1;
            }
            if ((i + 2) % 16 != 0) {
                result[i / 2 / 8] <<= 1;
            }
        }
        return result;
    }
}
