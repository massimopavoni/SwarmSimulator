package it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Position;

/**
 * Class defining a circle shape.
 */
public class Circle extends Ellipse implements Shape {
    /**
     * Constructor for a circle shape from its center and radius.
     *
     * @param center circle center
     * @param radius circle radius
     * @throws ShapeException if the radius is not finite and positive
     */
    public Circle(Position center, double radius) {
        super(center, new Position(radius, radius));
    }

    /**
     * Get circle center and radius coordinates.
     *
     * @return properties double array
     */
    @Override
    public double[] getProperties() {
        return new double[]{center.x(), center.y(), radius.x()};
    }

    /**
     * Checks if a position is contained in the circle shape.
     *
     * @param p position to check
     * @return true if the position is contained in the shape, false otherwise
     */
    @Override
    public boolean contains(Position p) {
        return SwarmUtils.compare(p.distanceTo(center), radius.x()) <= 0;
    }
}
