package com.michalsadel.signalprocessing;

import com.michalsadel.commons.*;

import java.util.*;

final class Payload<T> implements SampleObserver {
    private final int size;
    private final Map<String, LeftShiftedBuffer> detectedPayloadBuffer;
    private final PayloadDecoder<T> payloadDecoder;
    private final GeneralObservable<SampleObserver> sampleObservable = new GeneralObservable<>();
    private final GeneralObservable<MessageObserver<T>> messageObservable = new GeneralObservable<>();

    public Payload(PayloadDecoder payloadDecoder) {
        this.detectedPayloadBuffer = new HashMap<>();
        this.payloadDecoder = payloadDecoder;
        this.size = (this.payloadDecoder != null) ? this.payloadDecoder.size() : 0;
    }

    public void add(float magnitude) {
        final Iterator<Map.Entry<String, LeftShiftedBuffer>> payloads = detectedPayloadBuffer.entrySet().iterator();
        while (payloads.hasNext()) {
            final Map.Entry<String, LeftShiftedBuffer> nextPayload = payloads.next();
            nextPayload.getValue().add(magnitude);
            if (nextPayload.getValue().size() == size) {
                final String payloadKey = nextPayload.getKey();
                final float[] payload = nextPayload.getValue().getBuffer();
                payloads.remove();
                sampleObservable.getObservers().forEach(x -> x.detected(payload, payloadKey));
                Optional.ofNullable(payloadDecoder)
                        .ifPresent(decoder -> messageObservable
                                .getObservers()
                                .forEach(messageObserver -> messageObserver.decoded(decoder.decode(payload), payloadKey)));
            }
        }
    }

    @Override
    public void detected(float[] samples, String detectionId) {
        detectedPayloadBuffer.put(detectionId, new LeftShiftedBuffer(size));
    }

    public void attach(SampleObserver observer) {
        sampleObservable.attach(observer);
    }

    public void attach(MessageObserver observer) {
        messageObservable.attach(observer);
    }
}
