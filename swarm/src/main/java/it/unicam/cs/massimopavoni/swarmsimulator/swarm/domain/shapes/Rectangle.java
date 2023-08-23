package it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Class defining a rectangle shape.
 */
public final class Rectangle extends Polygon implements Shape {
    /**
     * Rectangle center position.
     */
    private final Position center;
    /**
     * Rectangle width and height.
     */
    private final Position widthHeight;

    /**
     * Constructor for a rectangle shape from a list of vertices' positions.
     *
     * @param center      rectangle center
     * @param widthHeight rectangle width and height
     */
    public Rectangle(Position center, Position widthHeight) {
        super(new ArrayList<>(List.of(
                new Position(center.x() - widthHeight.x() / 2, center.y() - widthHeight.y() / 2),
                new Position(center.x() + widthHeight.x() / 2, center.y() - widthHeight.y() / 2),
                new Position(center.x() + widthHeight.x() / 2, center.y() + widthHeight.y() / 2),
                new Position(center.x() - widthHeight.x() / 2, center.y() + widthHeight.y() / 2))));
        if (!widthHeight.isPositive())
            throw new ShapeException("A rectangle shape must have positive width and height.");
        this.center = center;
        this.widthHeight = widthHeight;
    }

    /**
     * Rectangle center position getter.
     *
     * @return rectangle center position
     */
    public Position getCenter() {
        return center;
    }

    /**
     * Rectangle width and height getter.
     *
     * @return rectangle width and height
     */
    public Position getWidthHeight() {
        return widthHeight;
    }

    /**
     * Get rectangle center and width and height coordinates.
     *
     * @return properties double array
     */
    @Override
    public double[] getProperties() {
        return new double[]{center.x(), center.y(), widthHeight.x(), widthHeight.y()};
    }

    /**
     * Checks if a position is contained in the rectangle shape.
     *
     * @param p position to check
     * @return true if the position is contained in the shape, false otherwise
     */
    @Override
    public boolean contains(Position p) {
        return SwarmUtils.compare(p.x(), vertices.get(0).x()) >= 0 &&
                SwarmUtils.compare(p.x(), vertices.get(2).x()) <= 0 &&
                SwarmUtils.compare(p.y(), vertices.get(0).y()) >= 0 &&
                SwarmUtils.compare(p.y(), vertices.get(2).y()) <= 0;
    }

    /**
     * Get a list of random positions contained in the rectangle shape.
     *
     * @param onBoundary if true, positions are generated on the rectangle shape boundary
     * @param amount     number of positions to generate
     * @return list of random positions
     */
    @Override
    public List<Position> getRandomPositions(boolean onBoundary, int amount) {
        if (onBoundary)
            return SwarmUtils.parallelize(() -> IntStream.range(0, amount).parallel()
                    .mapToObj(i -> getRandomPositionOnBoundary()).collect(toImmutableList()));
        else
            return SwarmUtils.parallelize(() -> IntStream.range(0, amount).parallel()
                    .mapToObj(i -> Position.random(vertices.get(0), vertices.get(2)))
                    .collect(toImmutableList()));
    }

    /**
     * Get a random position on the rectangle shape boundary.
     *
     * @return random position
     */
    private Position getRandomPositionOnBoundary() {
        return switch (ThreadLocalRandom.current().nextInt(4)) {
            case 0 -> new Position(
                    ThreadLocalRandom.current().nextDouble(vertices.get(0).x(), vertices.get(2).x()),
                    vertices.get(0).y());
            case 1 -> new Position(
                    vertices.get(2).x(),
                    ThreadLocalRandom.current().nextDouble(vertices.get(0).y(), vertices.get(2).y()));
            case 2 -> new Position(
                    ThreadLocalRandom.current().nextDouble(vertices.get(0).x(), vertices.get(2).x()),
                    vertices.get(2).y());
            case 3 -> new Position(
                    vertices.get(0).x(),
                    ThreadLocalRandom.current().nextDouble(vertices.get(0).y(), vertices.get(2).y()));
            default -> throw new ShapeException("Invalid random edge value");
        };
    }
}
