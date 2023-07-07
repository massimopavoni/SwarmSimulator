package it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmProperties;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Position;

import java.util.List;
import java.util.stream.IntStream;

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
     * Constructor for a polygon shape from a list of vertices' positions.
     *
     * @param vertices list of vertices' positions
     * @throws ShapeException if the number of vertices is less than 3 or more than
     *                        the number allowed by the swarm properties file,
     *                        or if the polygon is self-intersecting
     */
    public Polygon(List<Position> vertices) {
        this.vertices = uniqueVertices(vertices).stream().collect(toImmutableList());
        if (this.vertices.size() < 3 || this.vertices.size() > SwarmProperties.maxPolygonVertices())
            throw new ShapeException(String.format("A polygon shape must have between 3 and %d distinct vertices.",
                    SwarmProperties.maxPolygonVertices()));
    }

    /**
     * Take unique vertices from the list of vertices.
     * Stream approaches or sets cannot be used because of the fuzzy nature of positions equality (isEqual method),
     * and resulting impossible coherent transitive equals and hashCode implementations.
     *
     * @param vertices list of vertices
     * @return immutable list of unique vertices
     */
    private List<Position> uniqueVertices(List<Position> vertices) {
        int i;
        for (i = 0; i < vertices.size(); i++) {
            for (int j = i + 1; j < vertices.size(); j++) {
                if (vertices.get(i).equalTo(vertices.get(j))) {
                    vertices.remove(i--);
                    break;
                }
            }
        }
        return vertices;
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
     * Get polygon vertices coordinates.
     *
     * @return properties double array
     */
    @Override
    public double[] getProperties() {
        return IntStream.iterate(0, i -> i + 1).limit(vertices.size() * 2L)
                .mapToDouble(i -> i % 2 == 0 ?
                        vertices.get(i / 2).x()
                        : vertices.get(i / 2).y()).toArray();
    }

    /**
     * Checks if a position is contained in the polygon shape.
     * Uses even-odd vector graphics rule.
     *
     * @param p position to check
     * @return true if the position is contained in the shape, false otherwise
     */
    @Override
    public boolean contains(Position p) {
        boolean result = false;
        int j = vertices.size() - 1;
        Position ip;
        Position jp;
        for (int i = 0; i < vertices.size(); i++) {
            ip = vertices.get(i);
            if (p.equalTo(ip))
                return true;
            jp = vertices.get(j);
            if (SwarmUtils.compare(ip.y(), p.y()) > 0 != SwarmUtils.compare(jp.y(), p.y()) > 0) {
                double slope = (p.x() - ip.x()) * (jp.y() - ip.y()) - (p.y() - ip.y()) * (jp.x() - ip.x());
                if (SwarmUtils.compare(slope, 0) == 0)
                    return true;
                if (SwarmUtils.compare(slope, 0) < 0 != SwarmUtils.compare(jp.y(), ip.y()) < 0)
                    result = !result;
            }
            j = i;
        }
        return result;
    }
}
