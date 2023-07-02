package it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Position;

/**
 * Interface defining a shape.
 */
public interface Shape {
    /**
     * Checks if a position is contained in the shape.
     *
     * @param p position to check
     * @return true if the position is contained in the shape, false otherwise
     */
    boolean contains(Position p);
}
