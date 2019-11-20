package sdv.gui.controller.control.sys;

import com.jfoenix.controls.JFXToggleButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import sdv.communication.Ping;
import sdv.communication.TCP.TCPCommunication;
import sdv.communication.UDP.UDPCommunication;
import sdv.functions.ClassStatus;
import sdv.functions.gamepad.GamepadAdapter;
import sdv.functions.gamepad.GamepadInput;
import sdv.functions.keyboard.KeyboardInput;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


/**
 * Controls the control system GUI screen.
 *
 * @author Andreas
 * @version 1.1
 * @since 18.11.2019, 23:45
 */
public class ControlSys implements PropertyChangeListener {
    // The IP of the micro controller
    private static final String SERVER_IP = "192.168.0.105";
    // The port for the GUI controller
    private static final int GUI_PORT = 8001;
    // The port for the web cam
    private static final int WEBCAM_PORT = 9000;
    // The port for the LiDAR
    private static final int LIDAR_PORT = 9002;

    // Interface for UDP communication.
    private UDPCommunication udpCommunication;
    // Interface for TCP communication.
    private TCPCommunication tcpCommunication;

    private Ping ping;

    // Handles button events.
    @FXML
    private JFXToggleButton manualMode;
    // GUI's ImageView, to display the images.
    @FXML
    private ImageView webCamImageView, overlayImageView;
    // GUI's ImageView, to display the images.
    @FXML
    private ScatterChart<Number, Number> LiDARScatterChart;
    // GUI's connected label.
    @FXML
    private Label connectedLabel, webCamLabel, LiDARLabel, serverResponseTimeLabel;
    // Indecates if gui is connected to server.
    @FXML
    private Button connectBtn;
    // Webcam buttons.
    @FXML
    private Button webCamStart, webCamStop;
    // Scatter chart buttons.
    @FXML
    private Button LiDARStart, LiDARStop;
    // Manual buttons and images
    @FXML
    private ImageView gamepadControllerImageView;
    @FXML
    private ImageView keyboardControllerImageView;
    @FXML
    private JFXToggleButton selectControllerInput;
    // GUI's manualSpeedSlider.
    @FXML
    private Slider manualSpeedSlider;
    // The manualSpeed given from the right trigger from the gamepad, 0-100
    private int manualSpeed = 0;
    // Keyboard inputs
    private KeyboardInput btnEvent;
    // The sine value of the left thumb stick
    private double turningDirection = 0;
    // The magnitude of the joystick from the gamepad. Further out, higher the manualSpeed, 0-100
    private double turningMagnitude = 0;
    // Stores the old speed
    private double oldManualSpeed = 0;


    /**
     * Using initialise instead of constructor since this is called after FXML fields are populated.
     */

    public void initialize() {
        this.udpCommunication = new UDPCommunication(SERVER_IP, WEBCAM_PORT, LIDAR_PORT);
        this.tcpCommunication = new TCPCommunication(SERVER_IP, GUI_PORT);

        this.btnEvent = new KeyboardInput();
        gamepad();

        // Disables all buttons not available until connection.
        doDisableBtn(true);
        doDisableManuelControl(true);
    }


    /**
     * Connects and listening on the gamepad connected
     */
    private void gamepad() {
        GamepadInput gamepadInput = new GamepadInput();
        gamepadInput.setLeftThumbDeadZone(0.1);

        gamepadInput.addGamepadListener(new GamepadAdapter() {

            public void start(boolean pressed) {
                if (connectBtn.getText().equals("Disconnect") && pressed && !manualMode.isSelected()) {
                    manualMode.setSelected(true);

                    selectControllerInput.setSelected(true);

                    doSelectManualMode(true);
                }
            }

            public void back(boolean pressed) {
                if (connectBtn.getText().equals("Disconnect") && pressed && manualMode.isSelected()) {
                    manualMode.setSelected(false);
                    doSelectManualMode(false);
                }
            }


            /**
             *  Listening the right trigger value on the gamepadInput
             *  The value is squared to create a more real-like experience.
             * @param value a value between 0..1
             */
            public void leftTrigger(double value) {
                if (manualMode.isSelected() && selectControllerInput.isSelected()) {
                    manualSpeed = (int) (value * value * -100);
                    sendManualMotorCommand();
                }
            }

            /**
             *  Listening the right trigger value on the gamepadInput
             *  The value is squared to create a more real-like experience.
             * @param value a value between 0..1
             */
            public void rightTrigger(double value) {
                if (manualMode.isSelected() && selectControllerInput.isSelected()) {
                    manualSpeed = (int) (value * value);
                    sendManualMotorCommand();
                }
            }

            /**
             * Listening the left thumb stick magnitude value on the gamepadInput
             * When the thumb stick is in the center, the value is 0.
             * When the thumb stick is the furthest out, the value is 1.
             * @param value a value between 0..1
             */
            public void leftThumbMagnitude(double value) {
                if (manualMode.isSelected() && selectControllerInput.isSelected()) {
                    turningMagnitude = value * 100;
                    sendManualMotorCommand();
                }
            }

            /**
             * Listening the left thumb stick direction on the gamepadInput.
             * Getting the sine value of the left thumb stick.
             * @param value a value between 0..359
             */
            public void leftThumbDirection(double value) {
                if (manualMode.isSelected() && selectControllerInput.isSelected()) {
                    double direction = Math.toRadians(value);
                    // A value from 0 to 1
                    turningDirection = Math.sin(direction);
                    sendManualMotorCommand();
                }
            }

            private void sendManualMotorCommand() {
                double turningFactor = Math.abs(turningMagnitude * turningDirection);
                double speedBoost = (100 - turningFactor) * (manualSpeed);
                double turingSpeed = turningMagnitude * turningDirection;

                if (oldManualSpeed != (turingSpeed + speedBoost)) {
                    if (manualSpeed >= 0) {
                        tcpCommunication.sendCommand("ManualMotorControl:" + (int) ((turingSpeed) + speedBoost) + ":" + (int) ((-turingSpeed) + speedBoost));
                    } else {
                        tcpCommunication.sendCommand("ManualMotorControl:" + (int) ((-turingSpeed) + speedBoost) + ":" + (int) ((turingSpeed) + speedBoost));
                    }
                    oldManualSpeed = turningMagnitude * turningDirection + speedBoost;
                }
            }
        });
    }


    @FXML
    private void doHandleSelectControllerInput() {
        if (this.selectControllerInput.isSelected()) {
            doDisableGamepadInput(false);
            doDisableKeyboardInput(true);
        } else {
            doDisableGamepadInput(true);
            doDisableKeyboardInput(false);
            doHandleSliderInput();
        }
    }

    /**
     * If connect is pressed, tries to connect ot the server.
     */
    @FXML
    private void doHandleConnectBtn() {
        if (this.connectBtn.getText().equals("Connect")) {
            this.tcpCommunication.connectToTcpServer(this);
            doStartPinging();
        } else {
            doDisconnect();
            this.manualMode.setSelected(false);
            doDisableManuelControl(true);
            doStopPinging();
        }
    }


    /**
     * Updates the GUI with changes happening in backend classes.
     *
     * @param evt Event fired.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Platform.runLater(
                () -> {
                    if (evt.getPropertyName().equals("TCPGUIClient")) {
                        if (evt.getNewValue().equals(ClassStatus.isConnected())) {
                            this.connectedLabel.setText("Connected");
                            this.connectBtn.setText("Disconnect");
                            doDisableBtn(false);
                            this.manualSpeedSlider.setDisable(false);
                            this.tcpCommunication.sendCommand("MotorCommand:Auto");
                        } else if (evt.getNewValue().equals(ClassStatus.isDisconnected())) {
                            this.connectedLabel.setText("Disconnected");
                            this.connectBtn.setText("Connect");
                            doDisconnect();
                            doStopPinging();
                            this.manualSpeedSlider.setDisable(true);
                        } else if (evt.getNewValue().equals(ClassStatus.isLoading())) {
                            this.connectedLabel.setText("Connecting...");
                            this.connectBtn.setText("Connect");
                        }

                    } else if (evt.getPropertyName().equals("UDPWebCamClient")) {
                        if (evt.getNewValue().equals(ClassStatus.isConnected())) {
                            this.webCamLabel.setText("Connected");
                        } else if (evt.getNewValue().equals(ClassStatus.isDisconnected())) {
                            this.webCamLabel.setText("Disconnected");
                        } else if (evt.getNewValue().equals(ClassStatus.isLoading())) {
                            this.webCamLabel.setText("Connecting...");
                        }

                    } else if (evt.getPropertyName().equals("UDPLiDARClient")) {
                        if (evt.getNewValue().equals(ClassStatus.isConnected())) {
                            this.LiDARLabel.setText("Connected");
                        } else if (evt.getNewValue().equals(ClassStatus.isDisconnected())) {
                            this.LiDARLabel.setText("Disconnected");
                        } else if (evt.getNewValue().equals(ClassStatus.isLoading())) {
                            this.LiDARLabel.setText("Connecting...");
                        }
                    }
                }
        );
    }


    /**
     * Handles what happens when the start video button is pressed.
     */
    @FXML
    private void doStartWebCam() {
        this.udpCommunication.doStartWebCam(this.webCamImageView, this);
        enableWebCamOverlay(true);
        this.webCamStart.setDisable(true);
        this.webCamStop.setDisable(false);
    }


    /**
     * Handles what happens when the stop video button is pressed.
     */
    @FXML
    private void doStopWebCam() {
        this.udpCommunication.doStopWebCam();
        enableWebCamOverlay(false);
        this.webCamStart.setDisable(false);
        this.webCamStop.setDisable(true);
    }


    /**
     * Starts the LiDAR scatter map.
     */
    @FXML
    private void doStartLiDAR() {
        this.udpCommunication.doStartLiDAR(this.LiDARScatterChart, this);
        this.LiDARStart.setDisable(true);
        this.LiDARStop.setDisable(false);
    }


    /**
     * Stops the LiDAR scatter map.
     */
    @FXML
    private void doStopLiDAR() {
        this.udpCommunication.doStopLiDAR();
        this.LiDARStart.setDisable(false);
        this.LiDARStop.setDisable(true);
    }


    /**
     * Send string if it is not "".
     *
     * @param event Key pressed or released event.
     */
    @FXML
    private void doHandleKeyInput(KeyEvent event) {
        if (this.manualMode.isSelected() && !this.selectControllerInput.isSelected()) {
            int[] speed = this.btnEvent.doHandleKeyEvent(event);
            if (!(speed == null) && (this.oldManualSpeed != speed[0])) {
                this.tcpCommunication.sendCommand("ManualMotorControl:" + speed[0] + ":" + speed[1]);
                this.oldManualSpeed = speed[0];
            }
        }
    }


    /**
     * If a change in value is detected, sends info to motor controller.
     */
    @FXML
    private void doHandleSliderInput() {
        int value = (int) this.manualSpeedSlider.getValue();
        this.tcpCommunication.sendCommand("Speed:" + value);
        this.btnEvent.setSpeed(value);
        this.manualSpeed = value;
    }


    /**
     * Handles what happens when manual mode is activated.
     */
    @FXML
    private void doHandleManualMode() {
        doSelectManualMode(this.manualMode.isSelected());
    }


    private void doSelectManualMode(boolean selected) {
        if (selected) {
            this.tcpCommunication.sendCommand("MotorControl:Manual");
            doDisableManuelControl(false);
            doHandleSelectControllerInput();
        } else {
            this.tcpCommunication.sendCommand("MotorControl:Auto");
            doDisableManuelControl(true);
        }
    }


    private void doDisableGamepadInput(boolean doDisable) {
        try {
            if (doDisable) {
                this.gamepadControllerImageView.setImage(new Image(new FileInputStream("resources/images/controller_off.png"), 100, 100, true, true));
            } else {
                this.gamepadControllerImageView.setImage(new Image(new FileInputStream("resources/images/controller_active.png"), 100, 100, true, true));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void doDisableKeyboardInput(boolean doDisable) {
        try {
            if (doDisable) {
                this.keyboardControllerImageView.setImage(new Image(new FileInputStream("resources/images/keyboard_off.png"), 100, 100, true, true));
            } else {
                this.keyboardControllerImageView.setImage(new Image(new FileInputStream("resources/images/keyboard_active.png"), 100, 100, true, true));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }
    }


    /**
     * Stops all open connections.
     */
    private void doDisconnect() {
        this.LiDARLabel.setText("Disconnected");
        this.webCamLabel.setText("Disconnected");
        this.connectedLabel.setText("Disconnected");
        this.tcpCommunication.closeSocket();
        doDisableBtn(true);
        doStopLiDAR();
        doStopWebCam();
        doStopPinging();
    }


    /**
     * Disables buttons in GUI.
     *
     * @param doDisable True to disable buttons, false to enable.
     */
    private void doDisableBtn(boolean doDisable) {
        this.webCamStart.setDisable(doDisable);
        this.webCamStop.setDisable(doDisable);
        this.LiDARStart.setDisable(doDisable);
        this.LiDARStop.setDisable(doDisable);
        this.manualMode.setDisable(doDisable);
    }


    private void doDisableManuelControl(boolean doDisable) {
        this.selectControllerInput.setDisable(doDisable);
        doDisableGamepadInput(doDisable);
        doDisableKeyboardInput(doDisable);
    }


    private void doStartPinging() {
        this.ping = new Ping(SERVER_IP, this.serverResponseTimeLabel);
        this.ping.setDaemon(true);
        this.ping.start();
    }


    private void doStopPinging() {
        this.ping.doStop();
    }


    private void enableWebCamOverlay(boolean enable) {
        try {
            if (enable) {
                this.overlayImageView.setImage(new Image(new FileInputStream("resources/overlay/overlay.png"), 480, 640, true, true));
            } else {
                this.overlayImageView.setImage(null);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}