package com.michalsadel.signalprocessing;

import com.google.common.io.*;
import com.google.common.primitives.*;
import com.michalsadel.obsolete.*;
import com.michalsadel.signalprocessing.adsb.*;
import com.michalsadel.streams.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.slf4j.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.*;

import static org.junit.Assert.*;

@RunWith(BlockJUnit4ClassRunner.class)
public class ProcessorTest {
    private static final Logger log = LoggerFactory.getLogger(ProcessorTest.class);

    static class Result {
        float[] preamble;
        float[] message;
        String decoded;
    }

    @Test
    public void processorStart_IQFileInputData_OneMessageIsDetected() throws Exception {
        //dump.write(iqData.toString() + " mg{" + magnitude + "}\n");
        //Writer dump = new PrintWriter("process/dump.log");
        final AtomicInteger detected = new AtomicInteger();
        final InputStream oneSample = Thread.currentThread().getContextClassLoader().getResourceAsStream("one_sample.bin");
        final Result r = new Result();
        Processor
                .from(new MagnitudeInputDataStream(new IQInputDataStream(oneSample)))
                .preambleDetectionUsing(new AdsbDetectorsFactory(), 16)
                .payloadDecodeUsing(new ManchesterDecoder())
                .whenPreambleDetected((samples, detectionId) -> {
                    log.info("Preamble {} : {}", detectionId, samples);
                    detected.incrementAndGet();
                    r.preamble = samples;
                })
                .whenPayloadDetected((samples, detectionId) -> {
                    log.info("Payload {} : {}", detectionId, samples);
                    r.message = samples;
                })
                .<byte[]>whenPayloadDecoded((message, detectionId) -> log.info("Decoded {} : {}", detectionId, BaseEncoding.base16().upperCase().encode(message)))
                .process();
        assertEquals(1, detected.get());
        final float[] concat = Floats.concat(r.preamble, r.message);
        assertArrayEquals(new float[]{
                31.216982f, 8.276473f, 30.602287f, 7.5166483f, 2.5495098f, 1.5811388f, 2.5495098f, 32.596012f, 9.617692f, 28.60944f, 7.905694f, 2.5495098f, 2.1213202f, 1.5811388f, 1.5811388f, 0.70710677f,
                1.5811388f, 34.532593f, 42.80771f, 5.700877f, 1.5811388f, 37.503334f, 42.73757f, 7.905694f, 29.53811f, 9.513149f, 29.807716f, 7.905694f, 2.1213202f, 35.92353f, 43.96021f, 4.527693f, 0.70710677f, 36.74915f, 42.87773f, 4.7434163f, 0.70710677f, 34.154064f, 8.631338f, 32.534595f, 43.063908f, 6.519202f, 31.945265f, 9.924717f, 2.5495098f, 35.531677f, 43.525856f, 4.3011627f, 1.5811388f, 34.300144f, 8.514693f, 33.44398f, 44.5926f, 4.527693f, 0.70710677f, 32.256783f, 9.192389f, 29.774149f, 11.335784f, 30.73272f, 11.067972f, 29.706902f, 11.597413f, 31.567389f, 11.510864f, 31.75689f, 10.511898f, 31.184933f, 42.50294f, 5.147815f, 0.70710677f, 32.596012f, 8.746428f, 32.03904f, 10.511898f, 33.533566f, 42.573467f, 6.9641943f, 32.318726f, 9.924717f, 2.1213202f, 36.117863f, 41.575233f, 6.670832f, 32.03904f, 11.423659f, 29.774149f, 10.700467f, 31.312937f, 8.631338f, 2.5495098f, 36.74915f, 42.361538f, 5.700877f, 2.1213202f, 33.889526f, 9.8234415f, 32.534595f, 44.0284f, 6.519202f, 1.5811388f, 36.830692f, 42.80771f, 5.700877f, 1.5811388f, 35.10698f, 43.156693f, 6.363961f, 1.5811388f, 37.396523f, 41.838978f, 6.670832f, 31.184933f, 10.511898f, 2.9154758f, 36.090164f, 44.096485f, 3.8078866f, 0.70710677f, 35.92353f, 8.860023f, 33.143627f, 43.891914f, 6.519202f, 30.372684f, 8.631338f, 2.5495098f, 34.820972f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f, 0.70710677f
        }, concat, 0.001f);
        //new Adsb().generatePlot(concat, "build/plot/signal", 1, 1, "");
    }

    @Test
    @Ignore
    public void fakeTest() throws Exception {
        final InputStream oneSample = Thread.currentThread().getContextClassLoader().getResourceAsStream("sample.bin");
        final Map<String, Result> map = new HashMap<>();
        Processor
                .from(new MagnitudeInputDataStream(new IQInputDataStream(oneSample)))
                .preambleDetectionUsing(new AdsbDetectorsFactory(), 16)
                .payloadDecodeUsing(new ManchesterDecoder())
                .whenPreambleDetected((samples, detectionId) -> {
                    final Result result = new Result();
                    result.preamble = samples;
                    map.put(detectionId, result);
                })
                .whenPayloadDetected((samples, detectionId) -> {
                    final Result result = map.get(detectionId);
                    result.message = samples;
                })
                .<byte[]>whenPayloadDecoded((message, detectionId) -> {
                    final Result result = map.get(detectionId);
                    result.decoded = BaseEncoding.base16().upperCase().encode(message);
                    //log.info("Decoded {} : {}", detectionId, BaseEncoding.base16().upperCase().encode(message));
                })
                .process();
        final Adsb adsb = new Adsb();
        for (Map.Entry<String, Result> stringResultEntry : map.entrySet()) {
            adsb.generatePlot(Floats.concat(stringResultEntry.getValue().preamble, stringResultEntry.getValue().message),
                    "build/plot/" + stringResultEntry.getKey(), stringResultEntry.getValue().decoded);
        }
    }

}
