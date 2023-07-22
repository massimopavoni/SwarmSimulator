package it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes;

import java.util.stream.Stream;

/**
 * Enum of available shapes to define the swarm's domain.
 */
public enum ShapeType {
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
     * String representation of the shape type.
     */
    private final String shapeName;
    /**
     * Class of the corresponding shape to be created.
     */
    private final Class<? extends Shape> shapeClass;

    /**
     * Constructor for a shape type from string representation and corresponding shape class.
     *
     * @param shapeName  shape string representation
     * @param shapeClass shape class
     */
    ShapeType(String shapeName, Class<? extends Shape> shapeClass) {
        this.shapeName = shapeName;
        this.shapeClass = shapeClass;
    }

    /**
     * Selects the corresponding shape from a string.
     *
     * @param shapeName shape string representation
     * @return shape type
     * @throws ShapeException if a shape for the provided shape name is not found
     */
    public static ShapeType fromString(String shapeName) {
        return Stream.of(ShapeType.values())
                .filter(st -> st.shapeName.equals(shapeName))
                .findFirst()
                .orElseThrow(() -> new ShapeException("Specified shape is unavailable."));
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
