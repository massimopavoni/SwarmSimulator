package it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Position;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Class defining an ellipse shape.
 */
public sealed class Ellipse implements Shape permits Circle {
    /**
     * Ellipse center position.
     */
    protected final Position center;
    /**
     * Ellipse radius.
     */
    protected final Position radius;

    /**
     * Constructor for an ellipse shape from its center and radius.
     *
     * @param center ellipse center
     * @param radius ellipse radius
     * @throws ShapeException if the radius is not positive
     */
    public Ellipse(Position center, Position radius) {
        if (!radius.isPositive())
            throw new ShapeException("An ellipse shape must have positive radius.");
        this.center = center;
        this.radius = radius;
    }

    /**
     * Center position getter.
     *
     * @return center position
     */
    public Position getCenter() {
        return center;
    }

    /**
     * Radius position getter.
     *
     * @return radius position
     */
    public Position getRadius() {
        return radius;
    }

    /**
     * Get ellipse center and radius coordinates.
     *
     * @return properties double array
     */
    @Override
    public double[] getProperties() {
        return new double[]{center.x(), center.y(), radius.x(), radius.y()};
    }

    /**
     * Checks if a position is contained in the ellipse shape.
     *
     * @param p position to check
     * @return true if the position is contained in the shape, false otherwise
     */
    @Override
    public boolean contains(Position p) {
        return SwarmUtils.compare(Math.pow((p.x() - center.x()) / radius.x(), 2) +
                Math.pow((p.y() - center.y()) / radius.y(), 2), 1) <= 0;
    }

    /**
     * Get a list of random positions contained in the ellipse shape.
     *
     * @param onBoundary if true, positions are generated on the ellipse shape boundary
     * @param amount     number of positions to generate
     * @return list of random positions
     */
    @Override
    public List<Position> getRandomPositions(boolean onBoundary, int amount) {
        if (onBoundary)
            return SwarmUtils.parallelize(() -> IntStream.range(0, amount).parallel()
                    .mapToObj(i -> {
                        double a = ThreadLocalRandom.current().nextDouble() * 2 * Math.PI;
                        return new Position(center.x() + radius.x() * Math.cos(a),
                                center.y() + radius.y() * Math.sin(a));
                    }).collect(toImmutableList()));
        else
            return SwarmUtils.parallelize(() -> IntStream.range(0, amount).parallel()
                    .mapToObj(i -> {
                        double r = Math.sqrt(ThreadLocalRandom.current().nextDouble()) * radius.x();
                        double a = ThreadLocalRandom.current().nextDouble() * 2 * Math.PI;
                        return new Position(center.x() + r * Math.cos(a),
                                center.y() + radius.y() / radius.x() * r * Math.sin(a));
                    }).collect(toImmutableList()));
    }
}
