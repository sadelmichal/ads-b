package com.michalsadel;

import com.google.common.io.*;
import com.michalsadel.signalprocessing.*;
import com.michalsadel.signalprocessing.adsb.*;
import com.michalsadel.streams.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.slf4j.*;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;

@RunWith(BlockJUnit4ClassRunner.class)
public class QuadratureTest {
    private static final Logger log = LoggerFactory.getLogger(QuadratureTest.class);

    @Test
    public void testMe() throws IOException {
        Writer dump = new PrintWriter("dump.log");
        try (final MagnitudeInputDataStream mgt = new MagnitudeInputDataStream(
                new IQInputDataStream(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream("sample.bin")), dump)) {
            float magnitude;
            final Preamble preamble = new Preamble(new AdsbDetectorsFactory());
            final Message message = new Message(112 * 2);
            final Adsb adsb = new Adsb();
            preamble.attach(adsb);
            preamble.attach(message);
            while ((magnitude = mgt.readMagnitude()) != -1) {
                adsb.getIndex().incrementAndGet();
                message.add(magnitude, adsb.getIndex().get());
                preamble.add(magnitude);
            }
        }
    }

    @Test
    public void testy() throws IOException, URISyntaxException {
        //two shifts to look for a method in whole src (including rt)
        StringBuilder sb = new StringBuilder();


        final ClassLoader cl = Thread.currentThread().getContextClassLoader();
        final long sampleLength = new File(cl.getResource("sample.bin").toURI()).length();
        int bufferLength = (int) sampleLength;//0x4000;
        double[] magnitude = new double[(int) sampleLength / 2];
        final float[] iqBuffer = new float[2];
        short shortPreamble = 0b1_01111010;
        short preambleResult = 0;
        int l = 0;
        final byte threshold = 0;
        try (InputStream is = cl.getResourceAsStream("sample.bin")) {
            try (ReadableByteChannel channel = Channels.newChannel(is)) {
                final ByteBuffer buffer = ByteBuffer.allocate(bufferLength);
                int i = 0;
                while (channel.read(buffer) > 0) {
                    buffer.flip();
                    while (buffer.position() < buffer.limit()) {
                        final byte b = buffer.get();
                        iqBuffer[i++ % 2] = Byte.toUnsignedInt(b) - 127.5f;//unsigned java has no unsigned types, then center the sample from 0..255 into -127.5..127.5
                        if (i % 2 == 0) {
                            magnitude[l++] = Math.sqrt(iqBuffer[0] * iqBuffer[0] + iqBuffer[1] * iqBuffer[1]);
                            sb.append(String.format("%-10.1f%6.1f%6.1f%14.8f\n", (l - 1) / 2f, iqBuffer[0], iqBuffer[1], magnitude[l - 1]));
                            int k = 0;

                            if (l > 1) {
                                if (magnitude[l - 1] - magnitude[l - 2] + threshold < 0) {
                                    preambleResult |= 1 << 0;
                                }
                                preambleResult = (short) ((preambleResult << 1) & 0b00000001_11111111);
                                if (preambleResult == shortPreamble) {
                                    log.info("Got preamble at #{},{} first value is {}", l - 1, (l - 1 - 8) / 2f, magnitude[l - 1 - 8]);
                                }
                            }
                        }
                    }
                    i = 0;
                    buffer.clear();
                }
            }
        }
        Files.asCharSink(new File("dump.txt"), Charset.defaultCharset()).write(sb);
    }

//    @Test
//    public void testSimpleMagnitude() throws URISyntaxException, IOException {
//        final ClassPathResource classPathResource = new ClassPathResource("sample.bin");
//        final Path path = Paths.get(classPathResource.getURL().toURI());
//        try (InputStream in = Files.newInputStream(path)) {
//            short[] data = new short[50];
//            double[] magnitude = new double[25];
//            final int read = in.read(data);
//            int k = 0;
//            for (int i = 0; i < magnitude.length; i++) {
//                magnitude[i] = Math.sqrt(Math.pow(data[k], 2) + Math.pow(data[k + 1], 2));
//                k = k + 2;
//            }
//            data = null;
//        }
//    }
}
