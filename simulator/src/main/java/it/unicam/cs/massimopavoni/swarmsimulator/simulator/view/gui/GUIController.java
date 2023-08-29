package it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui;

import it.unicam.cs.massimopavoni.swarmsimulator.simulator.Resources;
import it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.ErrorType;
import it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui.control.SwarmAbout;
import it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui.control.SwarmAlert;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.HiveMind;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmState;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.ShapeFactory;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.SwarmShapeFactory;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives.SwarmDirectiveFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class representing the gui view, and acting as main controller for JavaFX application.
 */
public final class GUIController implements Initializable {
    /**
     * Main application stage.
     */
    private final Stage stage;
    /**
     * Alert dialog for errors.
     */
    private final SwarmAlert errorAlert;
    /**
     * Application about dialog.
     */
    private final SwarmAbout aboutDialog;
    /**
     * Shape factory instance.
     */
    private final ShapeFactory shapeFactory;
    /**
     * Swarm hive mind instance.
     */
    private HiveMind hiveMind;
    /**
     * File menu item for author account.
     */
    @FXML
    private MenuItem helpAuthor;
    /**
     * Swarm chart for simulation.
     */
    @FXML
    private XYChart<Double, Double> swarmChart;

    /**
     * Constructor for gui, initializing shape and directive factories, application stage and application controls.
     *
     * @param stage main application stage
     * @throws IOException if FXML files of additional windows cannot be loaded
     */
    GUIController(Stage stage) throws IOException {
        this.stage = stage;
        this.stage.setOnShowing(event -> onShowing());
        errorAlert = new SwarmAlert(AlertType.ERROR);
        shapeFactory = new SwarmShapeFactory();
        aboutDialog = new SwarmAbout();
        SwarmState.initializeParsers(shapeFactory, new SwarmDirectiveFactory());
    }

    /**
     * On showing event handler for main application stage.
     */
    private void onShowing() {
        errorAlert.initOwner(stage);
        aboutDialog.initOwner(stage);
    }

    /**
     * Controller initialization method.
     *
     * @param location  url location
     * @param resources resource bundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        helpAuthor.setText(Resources.AUTHOR);
    }

    /**
     * Show error alert message from exception.
     *
     * @param errorType type of error encountered
     * @param exception exception encountered
     */
    private void showErrorAlert(ErrorType errorType, Exception exception) {
        errorAlert.setHeaderText(errorType.getTitle());
        errorAlert.setContentText(
                exception.getCause() == null ? exception.getMessage() :
                        String.format("%s%n%nCaused by:%n%s",
                                exception.getMessage(), exception.getCause().getMessage()));
        errorAlert.showAndWait();
        if (errorType.isSevere())
            System.exit(1);
    }

    /**
     * File menu quit action event handler.
     */
    @FXML
    private void fileQuit() {
        stage.close();
    }

    /**
     * Help menu GitHub action event handler.
     */
    @FXML
    private void helpGitHub() {
        GUIApplication.openURI(Resources.GITHUB);
    }

    /**
     * Help menu author action event handler.
     */
    @FXML
    private void helpAuthor() {
        GUIApplication.openURI(Resources.ACCOUNT);
    }

    /**
     * Help menu license action event handler.
     */
    @FXML
    private void helpLicense() {
        GUIApplication.openURI(Resources.LICENSE);
    }

    /**
     * Help menu about action event handler.
     */
    @FXML
    private void helpAbout() {
        aboutDialog.showAndWait();
    }
}
