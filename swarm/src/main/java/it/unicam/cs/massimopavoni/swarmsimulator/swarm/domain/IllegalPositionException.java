package it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmException;

/**
 * Runtime exception for impossible positions inside the swarm's domain.
 */
public class IllegalPositionException extends SwarmException {
    /**
     * Illegal position exception constructor with message.
     *
     * @param message cause of the exception
     */
    public IllegalPositionException(String message) {
        super(message);
    }
}
