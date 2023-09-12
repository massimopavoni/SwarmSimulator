package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.parser;

/**
 * Exception for errors during strategy parsing.
 */
public class StrategyParserException extends Exception {
    /**
     * Strategy parser exception constructor with message and inner cause.
     *
     * @param message cause of the exception
     * @param cause   inner cause
     */
    public StrategyParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
