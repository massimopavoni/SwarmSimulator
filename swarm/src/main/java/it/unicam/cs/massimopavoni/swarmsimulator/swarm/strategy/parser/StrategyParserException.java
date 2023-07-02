package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.parser;

/**
 * Exception for errors during strategy parsing.
 */
public class StrategyParserException extends Exception {
    /**
     * Strategy parser exception constructor with message.
     *
     * @param message cause of the exception
     */
    public StrategyParserException(String message) {
        super(message);
    }
}
