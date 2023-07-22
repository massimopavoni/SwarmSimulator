package it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Position;

import java.util.List;

/**
 * Interface defining a shape.
 */
public sealed interface Shape permits Circle, Ellipse, Polygon, Rectangle {
    /**
     * Get the properties of the shape as an array of doubles.
     *
     * @return properties double array
     */
    double[] getProperties();

    /**
     * Checks if a position is contained in the shape.
     *
     * @param p position to check
     * @return true if the position is contained in the shape, false otherwise
     */
    boolean contains(Position p);

    /**
     * Get a list of random positions contained in the shape.
     *
     * @param onBoundary if true, positions are generated on the shape boundary
     * @param amount number of positions to generate
     * @return list of random positions
     */
    List<Position> getRandomPositions(boolean onBoundary, int amount);
}
