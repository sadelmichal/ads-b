package com.michalsadel.signalprocessing;

import com.michalsadel.streams.*;

import java.io.*;

public final class Processor {
    private MagnitudeDataInput magnitudeDataInput;
    private Preamble preamble;
    private Payload payload;

    private void start(Test test) throws IOException {
//        final ExecutorService executorService = Executors.newSingleThreadExecutor();
//        executorService.submit(() -> {
            try {
                try (final MagnitudeDataInput mg = magnitudeDataInput) {
                    preamble.attach(payload);
                    float magnitude;
                    while ((magnitude = mg.readMagnitude()) != -1) {
                        if (test != null) {
                            test.test(magnitude);
                        }
                        payload.add(magnitude);
                        preamble.add(magnitude);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
//        });
    }

    private Processor() {
    }

    public static DetectorSpec from(MagnitudeDataInput magnitudeDataInput) {
        return new Builder(magnitudeDataInput);
    }

    public interface DetectorSpec {
        PayloadDecoderSpec preambleDetectionUsing(DetectorsFactory detectorsFactory, int size);
    }

    public interface PayloadDecoderSpec {
        BuildSpec payloadDecodeUsing(PayloadDecoder payloadDecoder);
    }

    @FunctionalInterface
    public interface Test {
        void test(Float magnitude);
    }

    public interface BuildSpec {
        BuildSpec whenPreambleDetected(SampleObserver observer);

        BuildSpec whenPayloadDetected(SampleObserver observer);

        <T> BuildSpec whenPayloadDecoded(MessageObserver<T> observer);

        void process() throws IOException;

        void process(Test test) throws IOException;
    }

    private static class Builder implements DetectorSpec, PayloadDecoderSpec, BuildSpec {
        private final Processor processor = new Processor();

        private Builder(MagnitudeDataInput magnitudeDataInput) {
            processor.magnitudeDataInput = magnitudeDataInput;
        }

        @Override
        public PayloadDecoderSpec preambleDetectionUsing(DetectorsFactory detectorsFactory, int size) {
            processor.preamble = new Preamble(detectorsFactory, size);
            return this;
        }

        @Override
        public BuildSpec payloadDecodeUsing(PayloadDecoder payloadDecoder) {
            processor.payload = new Payload(payloadDecoder);
            return this;
        }

        @Override
        public BuildSpec whenPreambleDetected(SampleObserver observer) {
            processor.preamble.attach(observer);
            return this;
        }

        @Override
        public BuildSpec whenPayloadDetected(SampleObserver observer) {
            processor.payload.attach(observer);
            return this;
        }

        @Override
        public <T> BuildSpec whenPayloadDecoded(MessageObserver<T> observer) {
            processor.payload.attach(observer);
            return this;
        }

        @Override
        public void process() throws IOException {
            processor.start(null);
        }

        @Override
        public void process(Test test) throws IOException {
            processor.start(test);
        }

    }
}
