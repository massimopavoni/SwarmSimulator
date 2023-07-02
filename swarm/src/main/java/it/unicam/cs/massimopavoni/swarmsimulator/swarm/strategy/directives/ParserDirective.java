package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Enum of available parser directives to compose the swarm strategy.
 */
public enum ParserDirective {
    /**
     * <p>
     * Directive for continuation of previous execution.
     * </p>
     * <p>
     * Takes one argument that represents a number of time units for which
     * execution of previous directive shall continue.
     * </p>
     */
    CONTINUE("continue"),
    /**
     * <p>
     * Loop directive for infinite execution of a loop body.
     * </p>
     * <p>
     * Takes no arguments, but must be closed by a done directive.
     * This directive is actually a mutation of the repeat directive,
     * but it is parsed with a different syntax.
     * </p>
     */
    DOFOREVER("do forever"),
    /**
     * <p>
     * Loop directive for closing a loop body.
     * </p>
     * <p>
     * Takes no arguments.
     * </p>
     */
    DONE("done"),
    /**
     * <p>
     * Movement directive for following a certain echo, radiated by other drones.
     * </p>
     * <p>
     * Takes three arguments specifying the echo, the maximum distance within it can be felt
     * and the speed at which the drone will move towards it.
     * If no echo is found, the drone will aimlessly wander in a random direction.
     * </p>
     */
    FOLLOW("follow"),
    /**
     * <p>
     * Default movement directive.
     * </p>
     * <p>
     * Takes three arguments specifying the direction the drone will move towards and its speed.
     * A mutation of this directive presents the literal "random" after "move" and takes five arguments instead,
     * specifying a range of directions from which to choose a random one, and a speed.
     * </p>
     */
    MOVE("move"),
    /**
     * <p>
     * Loop directive for finite execution of a loop body.
     * </p>
     * <p>
     * Takes one argument specifying the amount of times the loop body must be executed.
     * Must be closed by a done directive.
     * </p>
     */
    REPEAT("repeat"),
    /**
     * <p>
     * Echo radiation directive.
     * </p>
     * <p>
     * Takes one argument specifying the echo to radiate.
     * </p>
     */
    SIGNAL("signal"),
    /**
     * <p>
     * Special movement directive which also identifies the termination of a drone's life.
     * </p>
     * <p>
     * Takes no arguments.
     * A drone that reaches this directive cannot move anymore, nor can it execute any other directive,
     * but it continues to radiate its current echoes, to provide guidance for the swarm.
     * </p>
     */
    STOP("stop"),
    /**
     * <p>
     * Echo radiation dual directive.
     * </p>
     * <p>
     * Takes one argument specifying the echo of which radiation must cease.
     * This is actually only a way of simplifying the writing of the swarm strategy,
     * since echoes radiation acts as a simple toggle.
     * </p>
     */
    UNSIGNAL("unsignal"),
    /**
     * <p>
     * Loop directive for conditional execution of a loop body.
     * </p>
     * <p>
     * Takes one argument specifying the signal that must be sensed by the drone to exit the loop.
     * Must be closed by a done directive.
     * Signals can only be emitted by region beacons within the swarm's domain.
     * </p>
     */
    UNTIL("until");

    /**
     * String representation of the directive to be parsed.
     */
    private final String directiveName;

    /**
     * Constructor for a parser directive from string representation.
     *
     * @param directiveName directive string representation
     */
    ParserDirective(String directiveName) {
        this.directiveName = directiveName;
    }

    /**
     * Selects the corresponding directive from a strategy line.
     *
     * @param line line to be parsed
     * @return an optional with the directive, if found
     */
    public static Optional<ParserDirective> fromLine(String line) {
        return Stream.of(ParserDirective.values()).filter(d -> d.isDirectiveOf(line)).findFirst();
    }


    /**
     * Checks if a line contains this directive.
     *
     * @param line line to be parsed
     * @return true if line starts with this directive, false otherwise
     */
    private boolean isDirectiveOf(String line) {
        return line.startsWith(directiveName);
    }
}
