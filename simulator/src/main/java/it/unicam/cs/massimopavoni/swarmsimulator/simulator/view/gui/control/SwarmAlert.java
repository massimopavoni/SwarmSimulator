package it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui.control;

import it.unicam.cs.massimopavoni.swarmsimulator.simulator.Resources;
import it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui.GUIApplication;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Class representing an alert dialog control.
 */
public final class SwarmAlert extends Alert {
    /**
     * Constructor for alert dialog with swarm simulator logo.
     *
     * @param alertType alert type
     */
    public SwarmAlert(AlertType alertType) {
        super(alertType);
        DialogPane dialogPane = getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(
                GUIApplication.class.getResource("css/alert.css")).toString());
        ((Stage) dialogPane.getScene().getWindow()).getIcons().add(Resources.LOGO);
        setTitle("");
    }
}
