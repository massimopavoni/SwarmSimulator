package it.unicam.cs.massimopavoni.swarmsimulator.simulator.view;

/**
 * Enum of available error types for views.
 */
public enum ErrorType {
    /**
     * Fatal error, severe enough to cause an application crash.
     */
    FATAL("Fatal error", true),
    /**
     * Normal error, can be fixed but often requires a reset.
     */
    NORMAL("Error", false),
    /**
     * Warning error, still caused by some problem, but minor.
     */
    WARNING("Warning", false);

    /**
     * String representation of the error type title.
     */
    private final String title;
    /**
     * Flag indicating if the error will cause an application crash.
     */
    private final boolean severe;

    /**
     * Constructor for an error type from title and severe flag.
     *
     * @param title  error type title
     * @param severe severe flag
     */
    ErrorType(String title, boolean severe) {
        this.title = title;
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
     * Getter for severe flag.
     *
     * @return severe flag
     */
    public boolean isSevere() {
        return severe;
    }
}
