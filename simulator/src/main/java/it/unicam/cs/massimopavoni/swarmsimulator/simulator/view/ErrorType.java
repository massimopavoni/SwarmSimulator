package it.unicam.cs.massimopavoni.swarmsimulator.simulator.view;

import javafx.scene.control.Alert.AlertType;

/**
 * Enum of available error types for views.
 */
public enum ErrorType {
    /**
     * Fatal error, severe enough to cause an application crash.
     */
    FATAL("Fatal error", AlertType.ERROR, true),
    /**
     * Normal error, can be fixed but often requires a reset.
     */
    ERROR("Error", AlertType.ERROR, false),
    /**
     * Warning error, still caused by some problem, but minor.
     */
    WARNING("Warning", AlertType.WARNING, false);

    /**
     * String representation of the error type title.
     */
    private final String title;
    /**
     * Alert type for the error, mainly used for GUI alert dialogs.
     */
    private final AlertType alertType;
    /**
     * Flag indicating if the error will cause an application crash.
     */
    private final boolean severe;

    /**
     * Constructor for an error type from title and severe flag.
     *
     * @param title  error type title
     * @param alertType alert type
     * @param severe severe flag
     */
    ErrorType(String title, AlertType alertType, boolean severe) {
        this.title = title;
        this.alertType = alertType;
        this.severe = severe;
    }

    /**
     * Getter for error type title.
     *
     * @return error type title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter for alert type.
     * @return alert type
     */
    public AlertType getAlertType() {
        return alertType;
    }

    /**
     * Getter for severe flag.
     *
     * @return severe flag
     */
    public boolean isSevere() {
        return severe;
    }
}
