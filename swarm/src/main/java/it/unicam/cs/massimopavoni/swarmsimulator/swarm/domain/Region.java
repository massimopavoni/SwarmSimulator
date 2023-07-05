package it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmProperties;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.Shape;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.ShapeType;

/**
 * Class defining a region inside the swarm's domain.
 *
 * @param signal    emitted by the region's beacon
 * @param shapeType type of the shape of the region
 * @param shape     defining the region itself (essentially identifies the area of signal emission of the beacon)
 */
public record Region(String signal, ShapeType shapeType, Shape shape) {
    /**
     * Constructor for a domain region with a given signal and shape.
     *
     * @param signal    emitted by the region's beacon
     * @param shapeType type of the shape of the region
     * @param shape     defining the region itself (essentially identifies the area of signal emission of the beacon)
     * @throws DomainException if the signal does not match the pattern
     */
    public Region {
        if (!SwarmProperties.signalPattern().matcher(signal).matches())
            throw new DomainException("A domain region's signal must match " +
                    "the pattern defined in the swarm properties file.");
    }
}
