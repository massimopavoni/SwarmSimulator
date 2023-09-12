package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmException;

/**
 * Runtime exception for strategy.
 */
public class StrategyException extends SwarmException {
    /**
     * Strategy exception constructor with message.
     *
     * @param message cause of the exception
     */
    public StrategyException(String message) {
        super(message);
    }
}
