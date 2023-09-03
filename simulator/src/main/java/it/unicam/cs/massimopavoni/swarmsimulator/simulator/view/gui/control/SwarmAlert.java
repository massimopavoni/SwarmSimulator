package it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui.control;

import it.unicam.cs.massimopavoni.swarmsimulator.simulator.Resources;
import it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.ErrorType;
import it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui.GUIApplication;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Region;
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

    /**
     * Show blocking error alert message from message and/or exception.
     *
     * @param errorType type of error encountered
     * @param message   message to show
     * @param exception exception encountered
     */
    public void showAndWait(ErrorType errorType, String message, Exception exception) {
        setAlertType(errorType.getAlertType());
        setHeaderText(errorType.getTitle());
        setContentText(String.format("%s%n%n%s", message,
                exception.getCause() == null ? exception.getMessage() :
                String.format("%s%n%nCaused by:%n%s",
                        exception.getMessage(), exception.getCause().getMessage())));
        // Workaround for alert dialog not resizing to fit content text
        getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        showAndWait();
        if (errorType.isSevere())
            System.exit(1);
    }
}
