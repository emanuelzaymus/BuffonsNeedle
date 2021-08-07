package sk.emanuelzaymus.buffonsneedle;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("SpellCheckingInspection")
public class BuffonsNeedleApp extends Application {

    private static List<Pair<Number, Number>> piEstimations;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Buffon's Needle");

        final var xAxis = new NumberAxis();
        xAxis.setLabel("Number of Throws");
        final var yAxis = new NumberAxis();
        yAxis.setLabel("Pi");

        final var lineChart = new LineChart<>(xAxis, yAxis);

        lineChart.setTitle("Pi Estimation");

        var series = new XYChart.Series<Number, Number>();
        series.setName("Throws of a needle");

        final var dataList = piEstimations.stream()
                .map(x -> new XYChart.Data<>(x.getKey(), x.getValue()))
                .collect(Collectors.toList());

        series.getData().addAll(dataList);

        lineChart.scaleShapeProperty();
        Scene scene = new Scene(lineChart, 800, 600);
        lineChart.getData().add(series);
        lineChart.setCreateSymbols(false);

        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(piEstimations.stream().map(Pair::getValue).min(Comparator.comparing(a -> new BigDecimal(a.toString()))).orElseThrow().doubleValue());
        yAxis.setUpperBound(piEstimations.stream().map(Pair::getValue).max(Comparator.comparing(a -> new BigDecimal(a.toString()))).orElseThrow().doubleValue());
        yAxis.setTickUnit(0.0001);
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(piEstimations.get(0).getKey().doubleValue());
        xAxis.setUpperBound(piEstimations.get(piEstimations.size() - 1).getKey().doubleValue());
        xAxis.setTickUnit(1_000_000);

        /* https://stackoverflow.com/questions/14357515/javafx-close-window-on-pressing-esc */
        stage.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
            if (KeyCode.ESCAPE == event.getCode()) {
                stage.close();
            }
        });

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        final var bn = new BuffonsNeedle(5, 3, 10_000_000, 0.3, 1000);
        bn.estimatePi();
        piEstimations = bn.getPiEstimations();

        System.out.println("Pi Estimation: " + bn.getPiResult());

        launch(args);
    }

}
