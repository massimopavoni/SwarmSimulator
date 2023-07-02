package it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Position;

import java.util.function.IntPredicate;
import java.util.stream.IntStream;

/**
 * Swarm implementation of the shape factory.
 */
public final class SwarmShapeFactory implements ShapeFactory {
    /**
     * Creates a circle shape for the swarm's domain.
     *
     * @param args shape arguments
     * @return circle shape
     */
    @Override
    public Shape createCircle(double[] args) {
        checkArgumentsOrThrow(args.length, l -> l == 3, "A circle shape requires 3 arguments.");
        return new Circle(new Position(args[0], args[1]), args[2]);
    }

    /**
     * Creates an ellipse shape for the swarm's domain.
     *
     * @param args shape arguments
     * @return ellipse shape
     */
    @Override
    public Shape createEllipse(double[] args) {
        checkArgumentsOrThrow(args.length, l -> l == 4, "An ellipse shape requires 4 arguments.");
        return new Ellipse(new Position(args[0], args[1]), new Position(args[2], args[3]));
    }

    /**
     * Creates a polygon shape for the swarm's domain.
     *
     * @param args shape arguments
     * @return polygon shape
     */
    @Override
    public Shape createPolygon(double[] args) {
        checkArgumentsOrThrow(args.length, l -> l >= 6 && l % 2 == 0,
                "A polygon shape requires at least 6 arguments and an even number of arguments.");
        return new Polygon(IntStream.iterate(0, i -> i + 2).limit(args.length / 2)
                .mapToObj(i -> new Position(args[i], args[i + 1])).toList());
    }

    /**
     * Creates a rectangle shape for the swarm's domain.
     *
     * @param args shape arguments
     * @return rectangle shape
     */
    @Override
    public Shape createRectangle(double[] args) {
        checkArgumentsOrThrow(args.length, l -> l == 4, "A rectangle shape requires 4 arguments.");
        return new Rectangle(new Position(args[0], args[1]), new Position(args[2], args[3]));
    }


    /**
     * Checks the arguments passed to a shape factory method.
     *
     * @param length    number of arguments
     * @param predicate predicate to check the number of arguments
     * @param message   message to throw in case of error
     * @throws ShapeException if the number of arguments is not valid
     */
    private void checkArgumentsOrThrow(int length, IntPredicate predicate, String message) {
        if (!predicate.test(length))
            throw new ShapeException(message);
    }
}
