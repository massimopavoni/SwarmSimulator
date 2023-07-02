package it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.MathUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmProperties;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Position;

import java.awt.geom.Line2D;
import java.util.List;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Class defining a polygon shape.
 */
public class Polygon implements Shape {
    /**
     * List of vertices' positions.
     */
    protected final List<Position> vertices;
    /**
     * Array of vertices' x coordinates.
     */
    private final double[] xVertices;
    /**
     * Array of vertices' y coordinates.
     */
    private final double[] yVertices;

    /**
     * Constructor for a polygon shape from a list of vertices' positions.
     *
     * @param vertices list of vertices' positions
     * @throws ShapeException if the number of vertices is less than 3 or more than
     *                        the number allowed by the swarm properties file,
     *                        or if the polygon is self-intersecting
     */
    public Polygon(List<Position> vertices) {
        this.vertices = vertices.stream().distinct().collect(toImmutableList());
        if (vertices.size() < 3 || vertices.size() > SwarmProperties.maxPolygonVertices())
            throw new ShapeException(String.format("A polygon shape must have between 3 and %d vertices.",
                    SwarmProperties.maxPolygonVertices()));
        this.xVertices = new double[vertices.size()];
        this.yVertices = new double[vertices.size()];
        this.vertices.forEach(v -> {
            xVertices[this.vertices.indexOf(v)] = v.x();
            yVertices[this.vertices.indexOf(v)] = v.y();
        });
        if (isSelfIntersectingPolygon())
            throw new ShapeException("A polygon shape cannot have self-intersections.");
    }

    /**
     * List of vertices' positions getter.
     *
     * @return list of vertices' positions
     */
    public List<Position> getVertices() {
        return vertices;
    }

    /**
     * Array of vertices' x coordinates getter.
     *
     * @return vertices' x coordinates
     */
    public double[] getXVertices() {
        return xVertices;
    }

    /**
     * Array of vertices' y coordinates getter.
     *
     * @return vertices' y coordinates
     */
    public double[] getYVertices() {
        return yVertices;
    }

    /**
     * Checks if a polygon shape is self-intersecting.
     * Brute-force approach, used only given the limits that the library imposes on region shapes.
     *
     * @return true if the shape is self-intersecting, false otherwise
     */
    private boolean isSelfIntersectingPolygon() {
        for (int i = 0; i < xVertices.length; i++) {
            for (int j = i + 1; j < xVertices.length; j++) {
                if (Line2D.linesIntersect(xVertices[i], yVertices[i], xVertices[i + 1], yVertices[i + 1],
                        xVertices[j], yVertices[j], xVertices[j + 1], yVertices[j + 1]))
                    return true;
            }
        }
        return false;
    }

    /**
     * Checks if a position is contained in the polygon shape.
     * See: <a href="https://wrfranklin.org/Research/Short_Notes/pnpoly.html">pnpoly</a>
     *
     * @param p position to check
     * @return true if the position is contained in the shape, false otherwise
     */
    @Override
    public boolean contains(Position p) {
        int i;
        int j;
        boolean result = false;
        for (i = 0, j = xVertices.length - 1; i < xVertices.length; j = i++) {
            if (MathUtils.compare(yVertices[i], p.y()) > 0 != MathUtils.compare(yVertices[j], p.y()) > 0 &&
                    MathUtils.compare(p.x(), (xVertices[j] - xVertices[i]) *
                            (p.y() - yVertices[i]) / (yVertices[j] - yVertices[i]) + xVertices[i]) < 0)
                result = !result;
        }
        return result;
    }
}
