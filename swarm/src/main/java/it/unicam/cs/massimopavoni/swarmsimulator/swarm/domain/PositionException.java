package it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmException;

/**
 * Runtime exception for positions inside the swarm's domain.
 */
public class PositionException extends SwarmException {
    /**
     * Position exception constructor with message.
     *
     * @param message cause of the exception
     */
    public PositionException(String message) {
        super(message);
    }
}
