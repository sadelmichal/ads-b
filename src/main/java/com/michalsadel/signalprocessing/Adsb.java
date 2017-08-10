package com.michalsadel.signalprocessing;

import com.google.common.primitives.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.*;
import org.jfree.chart.renderer.xy.*;
import org.jfree.data.xy.*;
import org.jfree.ui.*;
import org.slf4j.*;

import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;

public class Adsb implements PreambleObserver {
    private static final Logger log = LoggerFactory.getLogger(Adsb.class);
    private final AtomicInteger index = new AtomicInteger();

    public AtomicInteger getIndex() {
        return index;
    }

    private StandardChartTheme theme;

    public Adsb() {
        theme = (StandardChartTheme) StandardChartTheme.createJFreeTheme();

        theme.setExtraLargeFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        theme.setLargeFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        theme.setRegularFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));

        theme.setRangeGridlinePaint(Color.decode("#C0C0C0"));
        theme.setPlotBackgroundPaint(Color.white);
        theme.setChartBackgroundPaint(Color.white);
        theme.setGridBandPaint(Color.red);
        theme.setAxisOffset(new RectangleInsets(0, 0, 0, 0));
        theme.setBarPainter(new StandardBarPainter());
        theme.setAxisLabelPaint(Color.decode("#666666"));
        Path rootPath = Paths.get("plot/");
        try {
            Files.walk(rootPath, FileVisitOption.FOLLOW_LINKS)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
        }
    }

    @Override
    public void detected(float[] samples) {
//        log.info("Detected: at {}, {}", atomicInteger.incrementAndGet(), samples);
        log.info("Detected at {} : {}", index.get(), samples);
        //generatePlot(samples, "plot/preamble");
    }

    public void generatePlot(float[] samples, String prefix, int next, int index, String message) {
        if (!Files.exists(Paths.get("plot"))) {
            try {
                Files.createDirectory(Paths.get("plot"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        DefaultXYDataset xyDataset = new DefaultXYDataset();
        //final double[] x = Doubles.toArray(IntStream.range(0, samples.length).boxed().collect(Collectors.toList()));
        //final double[] y = Doubles.toArray(Floats.asList(samples));
        final double[] px = Doubles.toArray(IntStream.range(0, 16).boxed().collect(Collectors.toList()));
        final double[] py = Doubles.toArray(Floats.asList(samples).subList(0, 16));
        xyDataset.addSeries("Samples", new double[][]{px, py});
        final double[] mx = Doubles.toArray(IntStream.range(16, samples.length).boxed().collect(Collectors.toList()));
        final double[] my = Doubles.toArray(Floats.asList(samples).subList(16, samples.length));
        xyDataset.addSeries("Samples1", new double[][]{mx, my});
        XYBarDataset dataset = new XYBarDataset(xyDataset, 0.75);
        JFreeChart chart = ChartFactory.createXYBarChart("Signal at " + (index - 112 * 2 + 1), "message: " + message, false, "amplitude", dataset, PlotOrientation.VERTICAL, false, false, false);
        theme.apply(chart);
        final XYPlot plot = chart.getXYPlot();
        plot.setOutlineVisible(false);
        plot.getRangeAxis().setAxisLineVisible(false);
        plot.getRangeAxis().setTickMarksVisible(false);
        plot.setRangeGridlineStroke(new BasicStroke());
        plot.getRangeAxis().setTickLabelPaint(Color.decode("#666666"));
        plot.getDomainAxis().setTickLabelPaint(Color.decode("#666666"));
        chart.setTextAntiAlias(true);
        chart.setAntiAlias(true);
        final XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
        renderer.setShadowVisible(false);
        renderer.setDrawBarOutline(false);
        renderer.setBarPainter(new StandardXYBarPainter());
        renderer.setSeriesPaint(1, Color.decode("#708FA3"));
        renderer.setSeriesPaint(0, Color.decode("#FFAAAA"));
        try {
            try (FileOutputStream fos = new FileOutputStream(prefix + next + ".png")) {
                ChartUtilities.writeChartAsPNG(fos, chart, 1280, 300);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
