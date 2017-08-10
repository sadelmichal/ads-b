package com.michalsadel.streams;

public final class IQData {
    private final float i;
    private final float q;

    public IQData(float i, float q) {
        this.i = i;
        this.q = q;
    }

    public float getI() {
        return i;
    }

    public float getQ() {
        return q;
    }

    public float getISquare() {
        return i * i;
    }

    public float getQSquare() {
        return q * q;
    }

    @Override
    public String toString() {
        return "iq{" + "i=" + i + ", q=" + q + '}';
    }
}
