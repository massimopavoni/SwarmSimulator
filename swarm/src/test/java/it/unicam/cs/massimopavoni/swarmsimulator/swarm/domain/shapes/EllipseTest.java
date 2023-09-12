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

class EllipseTest {
    @BeforeAll
    static void setUp() throws HiveMindException {
        TestUtils.initializeProperties(EllipseTest.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() throws IOException {
        TestUtils.deleteProperties(EllipseTest.class.getSimpleName());
    }

    @Test
    void Ellipse_nonPositiveRadius() {
        assertAll(
                () -> assertThrowsExactly(ShapeException.class,
                        () -> new Ellipse(new Position(0, 0), new Position(0, 0))),
                () -> assertThrowsExactly(ShapeException.class,
                        () -> new Ellipse(new Position(0, 0), new Position(-1, 0))));
    }

    @Test
    void Ellipse_positiveRadius() {
        AtomicReference<Ellipse> e = new AtomicReference<>();
        assertAll(
                () -> assertDoesNotThrow(
                        () -> e.set(new Ellipse(new Position(0, 0), new Position(1, 2)))),
                () -> assertTrue(e.get().getCenter().equalTo(new Position(0, 0))),
                () -> assertTrue(e.get().getRadius().equalTo(new Position(1, 2))));
    }

    @Test
    void getProperties() {
        Ellipse e = new Ellipse(new Position(1, 1), new Position(1, 2));
        assertArrayEquals(new double[]{1, 1, 1, 2}, e.getProperties());
    }

    // https://www.geogebra.org/classic/hgtvksw7
    @Test
    void contains() {
        Ellipse e = new Ellipse(new Position(3, 2), new Position(2.4, 1.2));
        assertAll(
                () -> assertFalse(e.contains(new Position(1, 3))),
                () -> assertFalse(e.contains(new Position(5.5, 1.7))),
                () -> assertFalse(e.contains(new Position(2.4, 0.8))),
                () -> assertTrue(e.contains(new Position(3.5, 3.1))),
                () -> assertTrue(e.contains(new Position(2, 2))),
                () -> assertTrue(e.contains(new Position(4, 1))));
    }

    @Test
    void getRandomPositions_onBoundary() {
        Ellipse e = new Ellipse(new Position(1, 2), new Position(3, 9));
        List<Position> rps = e.getRandomPositions(true, SwarmProperties.maxDronesNumber());
        double centerX = e.center.x();
        double radiusX = e.radius.x();
        double centerY = e.center.y();
        double radiusY = e.radius.y();
        assertAll(
                () -> assertEquals(SwarmProperties.maxDronesNumber(), rps.size()),
                () -> assertTrue(SwarmUtils.parallelize(() -> rps.parallelStream()
                        .allMatch(rp -> SwarmUtils.compare(Math.pow((rp.x() - centerX) / radiusX, 2) +
                                Math.pow((rp.y() - centerY) / radiusY, 2), 1) == 0))));
    }

    @Test
    void getRandomPositions_notOnBoundary() {
        Ellipse e = new Ellipse(new Position(1, 2), new Position(3, 9));
        List<Position> rps = e.getRandomPositions(false, SwarmProperties.maxDronesNumber());
        assertAll(
                () -> assertEquals(SwarmProperties.maxDronesNumber(), rps.size()),
                () -> assertTrue(SwarmUtils.parallelize(() -> rps.parallelStream()
                        .allMatch(e::contains))));
    }

    @Test
    void getBoundingRectangle_correct() {
        Ellipse e = new Ellipse(new Position(1, 2), new Position(3, 9));
        Rectangle br = e.getBoundingRectangle();
        assertAll(
                () -> assertTrue(br.getCenter().equalTo(new Position(1, 2))),
                () -> assertTrue(br.getWidthHeight().equalTo(new Position(6, 18))));
    }
}