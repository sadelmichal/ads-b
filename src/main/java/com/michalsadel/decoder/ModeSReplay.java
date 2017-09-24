package com.michalsadel.decoder;

import com.google.common.io.*;

public class ModeSReplay {
    private final byte[] payload;
    private short downlinkFormat;

    private void initialize() {
        downlinkFormat = (short) ((payload[0] >> 3) & 0b11111);
    }

    public ModeSReplay(byte[] payload) {
        this.payload = payload;
        initialize();
    }

    @Override
    public String toString() {
        return "ModeSReplay{" + BaseEncoding.base16().upperCase().encode(payload) + "}";
    }

    public short getDownlinkFormat() {
        return downlinkFormat;
    }
}
