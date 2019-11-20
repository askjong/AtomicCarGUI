package sdv.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sdv.functions.initDll.checkDllFiles;

/**
 * Start self driving car GUI.
 *
 * @author Andreas
 * @version 1.0
 * @since 17.11.2019, 08:19
 */
public class Main extends Application {
    // Scene title.
    public static String sceneTitle = "Autonomous car";


    // Sets and starts the scene.
    @Override
    public void start(Stage primaryStage) throws Exception {

        checkDllFiles.check();                                                                                                                  // Checking if the DLL files for the gamepad is present. If not then it will add them
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sdv/gui/scenes/Login.fxml"));                         // Loading the FXML file
        primaryStage.getIcons().add(new Image("icon/icon.png", 200, 200, false, true));   // Adding a application icon
        primaryStage.setTitle(sceneTitle);                                                                                                      // Setting the scene name
        primaryStage.setScene(new Scene(root, 700, 400));                                                                        // Setting the application size
        primaryStage.show();                                                                                                                    // Shows the application window
    }
}