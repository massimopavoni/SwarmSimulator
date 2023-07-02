package it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Enum of available parser shapes to define the swarm's domain.
 */
public enum ParserShape {
    /**
     * <p>
     * Circle shape.
     * </p>
     * <p>
     * Takes two arguments that represent the center and the radius of the circle.
     * It is a mutation of the ellipse shape.
     * </p>
     */
    CIRCLE("circle", Circle.class),
    /**
     * <p>
     * Ellipse shape.
     * </p>
     * <p>
     * Takes two arguments that represent the center and the two radii of the ellipse.
     * </p>
     */
    ELLIPSE("ellipse", Ellipse.class),
    /**
     * <p>
     * Polygon shape.
     * </p>
     * <p>
     * Takes a list of arguments that represent the vertices of the polygon.
     * </p>
     */
    POLYGON("polygon", Polygon.class),
    /**
     * <p>
     * Rectangle shape.
     * </p>
     * <p>
     * Takes two arguments that represent the center and the two dimensions of the rectangle.
     * It is a mutation of the polygon shape.
     * </p>
     */
    RECTANGLE("rectangle", Rectangle.class);

    /**
     * String representation of the shape to be parsed.
     */
    private final String shapeName;

    /**
     * Class of the corresponding shape to be created.
     */
    private final Class<? extends Shape> shapeClass;

    /**
     * Constructor for a parser shape from string representation and corresponding shape class.
     *
     * @param shapeName  shape string representation
     * @param shapeClass shape class
     */
    ParserShape(String shapeName, Class<? extends Shape> shapeClass) {
        this.shapeName = shapeName;
        this.shapeClass = shapeClass;
    }

    /**
     * Selects the corresponding shape from a string.
     *
     * @param shapeName shape string representation
     * @return an optional with the shape, if found
     */
    public static Optional<ParserShape> fromString(String shapeName) {
        return Stream.of(ParserShape.values())
                .filter(ps -> ps.shapeName.equals(shapeName))
                .findFirst();
    }

    /**
     * Corresponding shape class getter.
     *
     * @return shape class
     */
    public Class<? extends Shape> getShapeClass() {
        return shapeClass;
    }
}
