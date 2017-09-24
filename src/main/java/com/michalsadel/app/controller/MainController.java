package com.michalsadel.app.controller;

import com.google.common.primitives.*;
import com.michalsadel.signalprocessing.*;
import com.michalsadel.signalprocessing.adsb.*;
import com.michalsadel.streams.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.chart.*;
import org.slf4j.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class MainController implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    private ObservableList<XYChart.Data> dataList;

    @FXML
    private BarChart<?, ?> barChart;

    private float[] loadFromFile() {
        List<Float> floats = new ArrayList<>();
        try {
            try (BufferedReader br = new BufferedReader(new FileReader("c:\\Users\\Me\\Desktop\\1b0bc956-d830-4c63-a548-4f76ba7b386c.txt"))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    Scanner scanner = new Scanner(line);
                    scanner.useLocale(Locale.ENGLISH);
                    scanner.useDelimiter(",");
                    while (scanner.hasNextFloat()) {
                        floats.add(scanner.nextFloat());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Floats.toArray(floats);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        barChart.getXAxis().setTickLabelsVisible(false);
        barChart.getXAxis().setTickMarkVisible(false);
        barChart.getYAxis().setAutoRanging(false);
        final float[] floats = loadFromFile();
        dataList = FXCollections.observableArrayList();
//        for (int i = 0; i < floats.length; i++) {
//            dataList.add(new BarChart.Data(Integer.toString(i), 0));
//        }
        barChart.getData().add(new BarChart.Series(dataList));
        barChart.setAnimated(false);
        barChart.setOnMouseClicked(event -> {
            final int[] k = {0};
            final InputStream oneSample = Thread.currentThread().getContextClassLoader().getResourceAsStream("sample.bin");
            try {
                Processor
                        .from(new MagnitudeInputDataStream(new IQInputDataStream(oneSample)))
                        .preambleDetectionUsing(new AdsbDetectorsFactory(), 16)
                        .payloadDecodeUsing(new ManchesterDecoder())
                        .process(magnitude -> {
                            Platform.runLater(() -> {
//                                for (XYChart.Data data : dataList) {
//
//                                }
//                                dataList.set()
                                if (dataList.size() > 50) {
                                    for (int i = 0; i < dataList.size() - 1; i++) {
                                        final XYChart.Data data = dataList.get(i);
                                        //log.info("yv{}", data.getYValue());
                                        data.setYValue(dataList.get(i + 1).getYValue());
                                    }
                                    //log.info("{}", magnitude);
                                    dataList.get(dataList.size() - 1).setYValue(magnitude);
                                } else {
                                    //log.info("{}", magnitude);
                                    dataList.add(new BarChart.Data(Integer.toString(dataList.size() - 1), magnitude));
                                }
                                //dataList.get(dataList.size()).setYValue(magnitude);
//                                    dataList.remove(0);
//                                }
//                                dataList.add(new BarChart.Data(Integer.toString(dataList.size() - 1), magnitude));
                                //series.getData().add(new BarChart.Data(Integer.toString(k[0]++), magnitude));
                            });
                            try {
                                Thread.sleep(30);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
