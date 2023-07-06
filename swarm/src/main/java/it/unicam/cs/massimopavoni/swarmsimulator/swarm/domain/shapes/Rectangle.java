package it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Class defining a rectangle shape.
 */
public class Rectangle extends Polygon implements Shape {
    /**
     * Rectangle center position.
     */
    protected final Position center;
    /**
     * Rectangle width and height.
     */
    protected final Position widthHeight;

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
}
