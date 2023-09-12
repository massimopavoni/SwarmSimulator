package it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmException;

/**
 * Runtime exception for region shapes.
 */
public class ShapeException extends SwarmException {
    /**
     * Shape exception constructor with message.
     *
     * @param message cause of the exception
     */
    public ShapeException(String message) {
        super(message);
    }
}
