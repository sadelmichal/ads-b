package com.michalsadel.signalprocessing;

import com.google.common.io.*;
import com.google.common.primitives.*;
import org.slf4j.*;

import java.util.*;
import java.util.concurrent.atomic.*;

public class Message implements PreambleObserver {
    private static final Logger log = LoggerFactory.getLogger(Message.class);
    private final int messageSize;
    private final List<Integer> currentMessages = new ArrayList<>();
    private final Map<Integer, List<Float>> messageBuffer;
    private final Map<Integer, List<Float>> preambleBuffer;
    private final AtomicInteger atomicInteger = new AtomicInteger();
    private final AtomicInteger atomicInteger1 = new AtomicInteger();
    private final AtomicInteger suffix = new AtomicInteger();
    private final Adsb adsb = new Adsb();

    public Message(final int messageSize) {
        this.messageSize = messageSize;
        this.messageBuffer = new HashMap<>();// new ArrayList<>(this.messageSize);
        this.preambleBuffer = new HashMap<>();
    }

    public void add(float magnitude, int index) {
        atomicInteger1.incrementAndGet();
        Iterator<Integer> iterator = currentMessages.iterator();
        while (iterator.hasNext()) {
            Integer currentMessage = iterator.next();
            messageBuffer.putIfAbsent(currentMessage, new ArrayList<>(messageSize));
            messageBuffer.get(currentMessage).add(magnitude);
            if (messageBuffer.get(currentMessage).size() == messageSize) {
                final List<Float> message = messageBuffer.remove(currentMessage);
                final List<Float> preamble = preambleBuffer.remove(currentMessage);
                iterator.remove();
                parseMessage(Floats.toArray(preamble), Floats.toArray(message), index);
            }
        }
    }

    @Override
    public void detected(float[] samples) {
        int i = atomicInteger1.get();
        final int i1 = atomicInteger.incrementAndGet();
        currentMessages.add(i1);
        preambleBuffer.putIfAbsent(i1, Floats.asList(samples));
    }

    private void parseMessage(float[] preamble, float[] message, int title) {
        final int i1 = suffix.incrementAndGet();
//        adsb.generatePlot(preamble, "plot/preamble", i1);
//        adsb.generatePlot(message, "plot/msg", i1);
        byte[] msg = new byte[16];
        byte theByte = 0;
        StringBuilder sb = new StringBuilder();
        int k = 0;
        int cb = 0;
        for (int i = 0, messageLength = message.length; i < messageLength; i += 2) {
            float low = message[i];
            float high = message[i + 1];
//            float delta = Math.abs(low - high);
//            if (i > 0 && delta < 4) {
//                theByte |= theByte >> 1;
//            } else
            if (low > high) {
                theByte |= 1;
                sb.append(1);
            } else {
                sb.append(0);
            }
            if (++k % 8 == 0) {
                msg[cb++] = theByte;
                theByte = 0;
            } else {
                theByte <<= 1;
            }

        }
        final String decodedMessage = BaseEncoding.base16().upperCase().encode(msg);
        log.info("message {} parsed: {}", sb.toString(), decodedMessage);
        adsb.generatePlot(Floats.concat(preamble, message), "plot/joined", i1, title, decodedMessage);
//        if (atomicInteger.get() > 7)
//            throw new RuntimeException("tes");
    }

}
