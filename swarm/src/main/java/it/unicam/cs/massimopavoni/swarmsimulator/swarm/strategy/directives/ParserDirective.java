package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;

import java.util.stream.Stream;

/**
 * Enum of available parser directives to compose the swarm strategy.
 */
public enum ParserDirective {
    /**
     * <p>
     * Jump directive for continuation of previous movement.
     * </p>
     * <p>
     * Takes one argument that represents a number of time units for which
     * execution of previous movement shall continue.
     * </p>
     */
    CONTINUE("continue", Continue.class),
    /**
     * <p>
     * Jump directive for infinite execution of a loop body.
     * </p>
     * <p>
     * Takes no arguments, but must be closed by a done directive.
     * This directive is actually a mutation of the repeat directive,
     * but it is parsed with a different syntax.
     * </p>
     */
    DOFOREVER("do forever", Repeat.class),
    /**
     * <p>
     * Jump directive for closing a loop body.
     * </p>
     * <p>
     * Takes no arguments.
     * </p>
     */
    DONE("done", Done.class),
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
    FOLLOW("follow", Follow.class),
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
    MOVE("move", Move.class),
    /**
     * <p>
     * Jump directive for finite execution of a loop body.
     * </p>
     * <p>
     * Takes one argument specifying the amount of times the loop body must be executed.
     * Must be closed by a done directive.
     * </p>
     */
    REPEAT("repeat", Repeat.class),
    /**
     * <p>
     * Echo radiation directive.
     * </p>
     * <p>
     * Takes one argument specifying the echo to radiate.
     * </p>
     */
    SIGNAL("signal", Radiate.class),
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
    STOP("stop", Stop.class),
    /**
     * <p>
     * Echo absorption directive.
     * </p>
     * <p>
     * Takes one argument specifying the echo of which radiation must cease.
     * </p>
     */
    UNSIGNAL("unsignal", Absorb.class),
    /**
     * <p>
     * Jump directive for conditional execution of a loop body.
     * </p>
     * <p>
     * Takes one argument specifying the signal that must be sensed by the drone to exit the loop.
     * Must be closed by a done directive.
     * Signals can only be emitted by region beacons within the swarm's domain.
     * </p>
     */
    UNTIL("until", Until.class);

    /**
     * String representation of the directive type.
     */
    private final String directiveName;
    /**
     * Class of the corresponding directive to be created.
     */
    private final Class<? extends Directive> directiveClass;

    /**
     * Constructor for a directive type from string representation.
     *
     * @param directiveName  directive string representation
     * @param directiveClass directive class
     */
    ParserDirective(String directiveName, Class<? extends Directive> directiveClass) {
        this.directiveName = directiveName;
        this.directiveClass = directiveClass;
    }

    /**
     * Selects the corresponding directive given a line.
     *
     * @param line line containing the directive string representation
     * @return directive type
     * @throws DirectiveException if a directive for the provided directive name is not found
     */
    public static ParserDirective fromLine(String line) {
        return Stream.of(ParserDirective.values())
                .filter(pd -> line.startsWith(pd.directiveName))
                .findFirst()
                .orElseThrow(() -> new DirectiveException("Specified directive is unavailable."));
    }

    /**
     * Corresponding directive class getter.
     *
     * @return shape class
     */
    public Class<? extends Directive> getDirectiveClass() {
        return directiveClass;
    }
}
