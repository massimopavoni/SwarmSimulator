package it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmException;

/**
 * Runtime exception for domain.
 */
public class DomainException extends SwarmException {
    /**
     * Domain exception constructor with message.
     *
     * @param message cause of the exception
     */
    public DomainException(String message) {
        super(message);
    }
}
