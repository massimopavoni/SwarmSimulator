package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmException;

/**
 * Runtime exception for strategy directives.
 */
public class DirectiveException extends SwarmException {
    /**
     * Directive exception constructor with message.
     *
     * @param message cause of the exception
     */
    public DirectiveException(String message) {
        super(message);
    }
}
