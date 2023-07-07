package it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Position;

/**
 * Class defining an ellipse shape.
 */
public class Ellipse implements Shape {
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
}
