package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;

/**
 * Interface defining a directive factory.
 */
public interface DirectiveFactory {
    /**
     * Default creation method for a directive from parser directive and arguments.
     *
     * @param parserDirective type of the directive
     * @param args            arguments of the directive
     * @return the created directive
     * @throws IllegalArgumentException if the arguments could not be parsed
     * @throws DirectiveException       if the directive could not be created
     */
    default Directive createDirective(ParserDirective parserDirective, String[] args) {
        return switch (parserDirective) {
            case CONTINUE -> createContinue(args);
            case DOFOREVER -> createDoForever(args);
            case DONE -> createDone(args);
            case FOLLOW -> createFollow(args);
            case MOVE -> createMove(args);
            case REPEAT -> createRepeat(args);
            case SIGNAL -> createSignal(args);
            case STOP -> createStop(args);
            case UNSIGNAL -> createUnsignal(args);
            case UNTIL -> createUntil(args);
        };
    }

    /**
     * Creates a continue directive for the swarm strategy.
     *
     * @param args directive arguments
     * @return continue directive
     */
    Directive createContinue(String[] args);

    /**
     * Creates a do forever directive for the swarm strategy.
     *
     * @param args directive arguments
     * @return do forever directive
     */
    default Directive createDoForever(String[] args) {
        return createRepeat(args);
    }

    /**
     * Creates a done directive for the swarm strategy.
     *
     * @param args directive arguments
     * @return done directive
     */
    Directive createDone(String[] args);

    /**
     * Creates a follow directive for the swarm strategy.
     *
     * @param args directive arguments
     * @return follow directive
     */
    Directive createFollow(String[] args);

    /**
     * Creates a move directive for the swarm strategy.
     *
     * @param args directive arguments
     * @return move directive
     */
    Directive createMove(String[] args);

    /**
     * Creates a repeat directive for the swarm strategy.
     *
     * @param args directive arguments
     * @return repeat directive
     */
    Directive createRepeat(String[] args);

    /**
     * Creates a signal directive for the swarm strategy.
     *
     * @param args directive arguments
     * @return signal directive
     */
    Directive createSignal(String[] args);

    /**
     * Creates a stop directive for the swarm strategy.
     *
     * @param args directive arguments
     * @return stop directive
     */
    Directive createStop(String[] args);

    /**
     * Creates an unsignal directive for the swarm strategy.
     *
     * @param args directive arguments
     * @return unsignal directive
     */
    Directive createUnsignal(String[] args);

    /**
     * Creates an until directive for the swarm strategy.
     *
     * @param args directive arguments
     * @return until directive
     */
    Directive createUntil(String[] args);
}
