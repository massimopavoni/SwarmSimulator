package it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes;

/**
 * Interface defining a shape factory.
 */
public interface ShapeFactory {
    /**
     * Default creation method for a shape from its name and arguments.
     *
     * @param shapeName name of the shape
     * @param args      arguments of the shape
     * @return the created shape
     */
    default Shape createShape(String shapeName, double[] args) {
        ParserShape parserShape = ParserShape.fromString(shapeName.toLowerCase())
                .orElseThrow(() -> new ShapeException("Specified shape is unavailable."));
        return switch (parserShape) {
            case CIRCLE -> createCircle(args);
            case ELLIPSE -> createEllipse(args);
            case POLYGON -> createPolygon(args);
            case RECTANGLE -> createRectangle(args);
        };
    }

    /**
     * Creates a circle shape.
     *
     * @param args shape arguments
     * @return circle shape
     */
    Shape createCircle(double[] args);

    /**
     * Creates an ellipse shape.
     *
     * @param args shape arguments
     * @return ellipse shape
     */
    Shape createEllipse(double[] args);

    /**
     * Creates a polygon shape.
     *
     * @param args shape arguments
     * @return polygon shape
     */
    Shape createPolygon(double[] args);

    /**
     * Creates a rectangle shape.
     *
     * @param args shape arguments
     * @return rectangle shape
     */
    Shape createRectangle(double[] args);
}
