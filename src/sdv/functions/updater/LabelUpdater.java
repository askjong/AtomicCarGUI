package sdv.functions.updater;

import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 * Takes a String element and puts it to a JavaFx Label element.
 *
 * @author Andreas
 * @version X.X
 * @since 15.11.2019, 07:38
 */
public class LabelUpdater {
    // Label to send series to.
    private Label label;

    /**
     * Setts the Label.
     *
     * @param label The Label JavaFX element that is used in the GUI.
     */
    public LabelUpdater(Label label) {
        this.label = label;
    }

    /**
     * Takes a String and puts it into the Label element
     *
     * @param string The string to put in the Label.
     */
    public void updateLabel(String string) {
        Platform.runLater(() -> {
            this.label.setText(string);
        });
    }

    /**
     * Clears the Label
     */
    public void doClear() {
        Platform.runLater(() -> {
            this.label.setText("");
        });
    }
}
