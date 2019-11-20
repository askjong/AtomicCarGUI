package sdv.functions.updater;

import javafx.application.Platform;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;


/**
 * Takes a XYChart.Series element and puts it to a JavaFx ScatterChart element.
 *
 * @author Andreas
 * @version 1.0
 * @since 17.11.2019, 12:19
 */
public class ScatterUpdater {
    // ScatterChart to send series to.
    private ScatterChart<Number, Number> scatterChartView;

    /**
     * Sets the ScatterChart element.
     *
     * @param scatterChart The ScatterChart JavaFX element that is used in the GUI.
     */
    public ScatterUpdater(ScatterChart<Number, Number> scatterChart) {
        this.scatterChartView = scatterChart;
    }

    /**
     * Takes the series and puts it into the ScatterChart element.
     *
     * @param series The series to put in the ScatterChart.
     */
    public void updateScatter(XYChart.Series<Number, Number> series) {
        Platform.runLater(() -> {
            doClear();
            this.scatterChartView.getData().add(series);
        });

    }

    /**
     * Clears the ScatterChart.
     */
    public void doClear() {
        this.scatterChartView.getData().remove(0, this.scatterChartView.getData().size());
    }
}
