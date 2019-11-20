package sdv.gui.controller.login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sdv.gui.Main;

import static sdv.gui.Main.sceneTitle;

/**
 * Controls login screen, gets credentials and changes screen.
 *
 * @author Andreas
 * @version 1.0
 * @since 10.11.2019, 10:49
 */
public class Login {
    // Manages credentials.
    private Credentials credentials;
    // Username field.
    @FXML
    private JFXTextField usernameField;
    // Password field.
    @FXML
    private JFXPasswordField passwordField;
    // Message label.
    @FXML
    private Label warningLabel;
    // Title label.
    @FXML
    private Label titleLabel;

    /**
     * Creates an instance of credentials.
     */
    public void initialize() {
        this.credentials = new Credentials();
        this.titleLabel.setText(Main.sceneTitle);
    }

    /**
     * Handles login attempts, gets login info and compares using the Credentials class.
     *
     * @param event Trigger event, login button or enter press.
     */
    @FXML
    private void handleLoginBtn(ActionEvent event) {
        // Gets password and username from login screen.
        String username = this.usernameField.getText();
        String password = this.passwordField.getText();

        // Checks if the username is accepted, if it is, changes scene.
        if (this.credentials.doCheckCredentials(username, password)) {
            doChangeScene(event);
        } else {
            this.warningLabel.setText("Wrong login credentials");
        }
    }


    /**
     * Changes scene from Login to ControlSys.
     *
     * @param event User presses login button or enter.
     */
    private void doChangeScene(ActionEvent event) {
        // Gets the stage from the event.
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        try {
            // Gets the new root from FXML file.
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sdv/gui/scenes/ControlSys.fxml"));

            // scenes size.
            int width = 1250;
            int height = 725;

            // Centering the scene.
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - width) / 2);
            stage.setY((screenBounds.getHeight() - height) / 2);

            // Set and shows the new scene.
            stage.setTitle(sceneTitle);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
