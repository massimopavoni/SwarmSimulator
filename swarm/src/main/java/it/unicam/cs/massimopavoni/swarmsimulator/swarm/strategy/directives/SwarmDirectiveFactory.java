package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Position;

import java.util.function.IntPredicate;

/**
 * Swarm implementation of the directive factory.
 */
public class SwarmDirectiveFactory implements DirectiveFactory {
    /**
     * Creates a continue directive.
     *
     * @param args directive arguments
     * @return continue directive
     * @throws NumberFormatException if the number arguments could not be parsed
     * @throws DirectiveException    if the directive could not be created
     */
    @Override
    public Directive createContinue(String[] args) {
        acceptArgumentsOrThrow(args.length, l -> l == 1, "A continue directive requires 1 argument.");
        return new Continue(Integer.parseInt(args[0]));
    }

    /**
     * Creates a done directive.
     *
     * @param args directive arguments
     * @return done directive
     * @throws NumberFormatException if the number arguments could not be parsed
     * @throws DirectiveException    if the directive could not be created
     */
    @Override
    public Directive createDone(String[] args) {
        acceptArgumentsOrThrow(args.length, l -> l == 1, "A done directive requires 1 argument.");
        return new Done(Integer.parseInt(args[0]));
    }

    /**
     * Creates a follow directive.
     *
     * @param args directive arguments
     * @return follow directive
     * @throws NumberFormatException if the number arguments could not be parsed
     * @throws DirectiveException    if the directive could not be created
     */
    @Override
    public Directive createFollow(String[] args) {
        acceptArgumentsOrThrow(args.length, l -> l == 3, "A follow directive requires 3 arguments.");
        return new Follow(args[0], Double.parseDouble(args[1]), Double.parseDouble(args[2]));
    }

    /**
     * Creates a move directive.
     *
     * @param args directive arguments
     * @return move directive
     * @throws NumberFormatException if the number arguments could not be parsed
     * @throws DirectiveException    if the directive could not be created
     */
    @Override
    public Directive createMove(String[] args) {
        boolean random = args[0].equalsIgnoreCase("random");
        if (random)
            acceptArgumentsOrThrow(args.length, l -> l == 6, "A move random directive requires 5 arguments.");
        else
            acceptArgumentsOrThrow(args.length, l -> l == 3, "A move directive requires 3 arguments.");
        double[] doubles = SwarmUtils.toDoubleArray(args, random ? 1 : 0);
        if (random)
            return new Move(new Position(doubles[0], doubles[1]), new Position(doubles[2], doubles[3]), doubles[4]);
        else
            return new Move(new Position(doubles[0], doubles[1]), doubles[2]);
    }

    /**
     * Creates a repeat directive.
     *
     * @param args directive arguments
     * @return repeat directive
     * @throws NumberFormatException if the number arguments could not be parsed
     * @throws DirectiveException    if the directive could not be created
     */
    @Override
    public Directive createRepeat(String[] args) {
        acceptArgumentsOrThrow(args.length, l -> l == 2,
                "A repeat directive requires 2 arguments.");
        if (args[1].equalsIgnoreCase("forever"))
            return new Repeat(Integer.parseInt(args[0]));
        return new Repeat(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
    }

    /**
     * Creates a signal directive.
     *
     * @param args directive arguments
     * @return signal directive
     * @throws DirectiveException if the directive could not be created
     */
    @Override
    public Directive createSignal(String[] args) {
        acceptArgumentsOrThrow(args.length, l -> l == 1,
                "A radiate directive requires 1 arguments.");
        return new Radiate(args[0]);
    }

    /**
     * Creates a stop directive.
     *
     * @param args directive arguments
     * @return stop directive
     * @throws DirectiveException if the directive could not be created
     */
    @Override
    public Directive createStop(String[] args) {
        acceptArgumentsOrThrow(args.length, l -> l == 0,
                "A stop directive requires no arguments.");
        return new Stop();
    }

    /**
     * Creates an unsignal directive.
     *
     * @param args directive arguments
     * @return unsignal directive
     * @throws DirectiveException if the directive could not be created
     */
    @Override
    public Directive createUnsignal(String[] args) {
        acceptArgumentsOrThrow(args.length, l -> l == 1,
                "An absorb directive requires 1 arguments.");
        return new Absorb(args[0]);
    }

    /**
     * Creates an until directive.
     *
     * @param args directive arguments
     * @return until directive
     * @throws NumberFormatException if the number arguments could not be parsed
     * @throws DirectiveException    if the directive could not be created
     */
    @Override
    public Directive createUntil(String[] args) {
        acceptArgumentsOrThrow(args.length, l -> l == 2,
                "An until directive requires 2 arguments.");
        return new Until(Integer.parseInt(args[0]), args[1]);
    }

    /**
     * Checks the arguments passed to a directive factory method.
     *
     * @param length    number of arguments
     * @param predicate predicate to check the number of arguments
     * @param message   message to throw in case of error
     * @throws DirectiveException if the number of arguments is not valid
     */
    private void acceptArgumentsOrThrow(int length, IntPredicate predicate, String message) {
        if (!predicate.test(length))
            throw new DirectiveException(message);
    }
}
