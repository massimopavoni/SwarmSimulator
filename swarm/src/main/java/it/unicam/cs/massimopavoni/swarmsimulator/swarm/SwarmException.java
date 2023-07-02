package it.unicam.cs.massimopavoni.swarmsimulator.swarm;

/**
 * Runtime exception for all swarm errors.
 */
public class SwarmException extends RuntimeException {
    /**
     * Swarm exception constructor with message.
     *
     * @param message cause of the exception
     */
    public SwarmException(String message) {
        super(message);
    }
}
