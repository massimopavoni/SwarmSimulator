package it.unicam.cs.massimopavoni.swarmsimulator.swarm.core;

/**
 * Exception for hive mind errors.
 */
public class HiveMindException extends Exception {
    /**
     * Hive mind exception constructor with message and inner cause.
     *
     * @param message cause of the exception
     * @param cause   inner cause
     */
    public HiveMindException(String message, Throwable cause) {
        super(message, cause);
    }
}
