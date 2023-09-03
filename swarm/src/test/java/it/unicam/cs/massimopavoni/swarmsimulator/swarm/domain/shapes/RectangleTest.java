package it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.TestUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.HiveMindException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmProperties;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Position;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class RectangleTest {
    @BeforeAll
    static void setUp() throws HiveMindException {
        TestUtils.initializeProperties(RectangleTest.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() throws IOException {
        TestUtils.deleteProperties(RectangleTest.class.getSimpleName());
    }

    @Test
    void Rectangle_degenerateOrNonPositiveWidthHeight() {
        assertAll(
                () -> assertThrowsExactly(ShapeException.class,
                        () -> new Rectangle(new Position(0, 0), new Position(0, 0))),
                () -> assertThrowsExactly(ShapeException.class,
                        () -> new Rectangle(new Position(0, 0), new Position(-1, 0))),
                () -> assertThrowsExactly(ShapeException.class,
                        () -> new Rectangle(new Position(0, 0), new Position(0, -2))),
                () -> assertThrowsExactly(ShapeException.class,
                        () -> new Rectangle(new Position(0, 0), new Position(-1, -2))));
    }

    @Test
    void Rectangle_nonDegenerateAndPositiveWidthHeight() {
        AtomicReference<Rectangle> r = new AtomicReference<>();
        assertAll(
                () -> assertDoesNotThrow(
                        () -> r.set(new Rectangle(new Position(0, 1), new Position(4, 3)))),
                () -> assertTrue(r.get().getCenter().equalTo(new Position(0, 1))),
                () -> assertTrue(r.get().getWidthHeight().equalTo(new Position(4, 3))),
                () -> assertTrue(r.get().getVertices().get(0).equalTo(new Position(-2, -0.5))),
                () -> assertTrue(r.get().getVertices().get(1).equalTo(new Position(2, -0.5))),
                () -> assertTrue(r.get().getVertices().get(2).equalTo(new Position(2, 2.5))),
                () -> assertTrue(r.get().getVertices().get(3).equalTo(new Position(-2, 2.5))));
    }

    @Test
    void getProperties() {
        Rectangle r = new Rectangle(new Position(0, 0), new Position(1, 2));
        assertArrayEquals(new double[]{0, 0, 1, 2}, r.getProperties());
    }

    // https://www.geogebra.org/classic/xm8asmya
    @Test
    void contains() {
        Rectangle r = new Rectangle(new Position(-0.5, -1.5), new Position(5, 13));
        assertAll(
                () -> assertFalse(r.contains(new Position(-3, 6))),
                () -> assertFalse(r.contains(new Position(1.9, -8.2))),
                () -> assertFalse(r.contains(new Position(-4, -3))),
                () -> assertFalse(r.contains(new Position(2.01, 0))),
                () -> assertTrue(r.contains(new Position(-2.2, 4))),
                () -> assertTrue(r.contains(new Position(1, -7.5))),
                () -> assertTrue(r.contains(new Position(-1.7, -5))),
                () -> assertTrue(r.contains(new Position(0.1, 2.4))));
    }

    @Test
    void getRandomPositions_onBoundary() {
        Rectangle r = new Rectangle(new Position(-3, 2), new Position(5, 7));
        List<Position> rps = r.getRandomPositions(true, SwarmProperties.maxDronesNumber());
        double xMin = r.getVertices().get(0).x();
        double xMax = r.getVertices().get(2).x();
        double yMin = r.getVertices().get(0).y();
        double yMax = r.getVertices().get(2).y();
        assertAll(
                () -> assertEquals(SwarmProperties.maxDronesNumber(), rps.size()),
                () -> assertTrue(SwarmUtils.parallelize(() -> rps.parallelStream()
                        .allMatch(rp -> (SwarmUtils.compare(rp.x(), xMin) >= 0 && SwarmUtils.compare(rp.x(), xMax) <= 0
                                && (SwarmUtils.compare(rp.y(), yMin) == 0 || SwarmUtils.compare(rp.y(), yMax) == 0))
                                || (SwarmUtils.compare(rp.y(), yMin) >= 0 && SwarmUtils.compare(rp.y(), yMax) <= 0 &&
                                (SwarmUtils.compare(rp.x(), xMin) == 0 || SwarmUtils.compare(rp.x(), xMax) == 0))))));
    }

    @Test
    void getRandomPositions_notOnBoundary() {
        Rectangle r = new Rectangle(new Position(-3, 2), new Position(5, 7));
        List<Position> rps = r.getRandomPositions(false, SwarmProperties.maxDronesNumber());
        assertAll(
                () -> assertEquals(SwarmProperties.maxDronesNumber(), rps.size()),
                () -> assertTrue(SwarmUtils.parallelize(() -> rps.parallelStream()
                        .allMatch(r::contains))));
    }

    @Test
    void getBoundingRectangle_correct() {
        Rectangle r = new Rectangle(new Position(-3, 2), new Position(5, 7));
        Rectangle br = r.getBoundingRectangle();
        assertAll(
                () -> assertTrue(br.getCenter().equalTo(new Position(-3, 2))),
                () -> assertTrue(br.getWidthHeight().equalTo(new Position(5, 7))),
                () -> assertTrue(br.vertices.get(0).equalTo(new Position(-5.5, -1.5))),
                () -> assertTrue(br.vertices.get(1).equalTo(new Position(-0.5, -1.5))),
                () -> assertTrue(br.vertices.get(2).equalTo(new Position(-0.5, 5.5))),
                () -> assertTrue(br.vertices.get(3).equalTo(new Position(-5.5, 5.5))));
    }
}