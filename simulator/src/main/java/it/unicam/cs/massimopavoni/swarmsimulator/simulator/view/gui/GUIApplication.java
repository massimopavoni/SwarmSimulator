package it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui;

import it.unicam.cs.massimopavoni.swarmsimulator.simulator.Resources;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Class representing the JavaFX graphical user interface application.
 */
public final class GUIApplication extends Application {
    /**
     * Host services instance.
     */
    private static HostServices hostServices;

    /**
     * Main method for JavaFX application.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Set host services instance.
     *
     * @param hostServices application host services instance
     */
    private static void setHostServices(HostServices hostServices) {
        GUIApplication.hostServices = hostServices;
    }

    /**
     * Open URI in default browser.
     *
     * @param uri URI string
     */
    public static void openURI(String uri) {
        hostServices.showDocument(uri);
    }

    /**
     * Start method for JavaFX application.
     *
     * @param stage application primary stage
     * @throws IOException if FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        setHostServices(getHostServices());
        FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource("simulator.fxml"));
        GUIController controller = new GUIController(stage);
        fxmlLoader.setController(controller);
        stage.setScene(new Scene(fxmlLoader.load()));
        initializeStage(stage);
        stage.show();
        controller.completeInitialization();
    }

    /**
     * Initialize primary stage characteristics.
     *
     * @param stage primary stage
     */
    private void initializeStage(Stage stage) {
        stage.getIcons().add(Resources.LOGO);
        stage.sizeToScene();
        stage.setResizable(false);
        stage.setTitle(Resources.NAME);
    }

    /**
     * Stop method for JavaFX application.
     */
    @Override
    public void stop() {
        Thread.currentThread().interrupt();
        System.exit(0);
    }
}
