package it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmException;

/**
 * Runtime exception for impossible domain creation.
 */
public class IllegalDomainException extends SwarmException {
    /**
     * Illegal domain exception constructor with message.
     *
     * @param message cause of the exception
     */
    public IllegalDomainException(String message) {
        super(message);
    }
}
