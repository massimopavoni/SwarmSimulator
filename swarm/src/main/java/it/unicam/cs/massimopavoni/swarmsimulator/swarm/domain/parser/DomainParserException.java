package it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.parser;

/**
 * Exception for errors during domain parsing.
 */
public class DomainParserException extends Exception {
    /**
     * Domain parser exception constructor with message.
     *
     * @param message cause of the exception
     */
    public DomainParserException(String message) {
        super(message);
    }
}
