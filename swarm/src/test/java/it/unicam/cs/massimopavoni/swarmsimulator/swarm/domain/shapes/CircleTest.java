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

class CircleTest {
    @BeforeAll
    static void setUp() throws HiveMindException {
        TestUtils.initializeProperties(CircleTest.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() throws IOException {
        TestUtils.deleteProperties(CircleTest.class.getSimpleName());
    }

    @Test
    void Circle_nonPositiveRadius() {
        assertAll(
                () -> assertThrowsExactly(ShapeException.class,
                        () -> new Circle(new Position(0, 0), 0)),
                () -> assertThrowsExactly(ShapeException.class,
                        () -> new Circle(new Position(0, 0), -1)));
    }

    @Test
    void Circle_positiveRadius() {
        AtomicReference<Circle> c = new AtomicReference<>();
        assertAll(
                () -> assertDoesNotThrow(() -> c.set(new Circle(new Position(1, 1), 4))),
                () -> assertTrue(c.get().getCenter().equalTo(new Position(1, 1))),
                () -> assertTrue(c.get().getRadius().equalTo(new Position(4, 4))));
    }

    @Test
    void getProperties() {
        Circle c = new Circle(new Position(1, 1), 2);
        assertArrayEquals(new double[]{1, 1, 2}, c.getProperties());
    }

    // https://www.geogebra.org/classic/jhuhv7dx
    @Test
    void contains() {
        Circle c = new Circle(new Position(-3, -5), 6);
        assertAll(
                () -> assertFalse(c.contains(new Position(-8, -1))),
                () -> assertFalse(c.contains(new Position(-2, -11))),
                () -> assertFalse(c.contains(new Position(2.4, -2))),
                () -> assertTrue(c.contains(new Position(-3, 1))),
                () -> assertTrue(c.contains(new Position(-8, -8))),
                () -> assertTrue(c.contains(new Position(0, -6))));
    }

    @Test
    void getRandomPositions_onBoundary() {
        Circle c = new Circle(new Position(1, 2), 10);
        List<Position> rps = c.getRandomPositions(true, SwarmProperties.maxDronesNumber());
        double radius = c.getRadius().x();
        assertAll(
                () -> assertEquals(SwarmProperties.maxDronesNumber(), rps.size()),
                () -> assertTrue(SwarmUtils.parallelize(() -> rps.parallelStream()
                        .allMatch(rp -> SwarmUtils.compare(rp.distanceTo(c.getCenter()), radius) == 0))));
    }

    @Test
    void getRandomPositions_notOnBoundary() {
        Circle c = new Circle(new Position(1, 2), 10);
        List<Position> rps = c.getRandomPositions(false, SwarmProperties.maxDronesNumber());
        assertAll(
                () -> assertEquals(SwarmProperties.maxDronesNumber(), rps.size()),
                () -> assertTrue(SwarmUtils.parallelize(() -> rps.parallelStream()
                        .allMatch(c::contains))));
    }

    @Test
    void getBoundingRectangle_correct() {
        Circle c = new Circle(new Position(1, 2), 10);
        Rectangle br = c.getBoundingRectangle();
        assertAll(
                () -> assertTrue(br.getCenter().equalTo(new Position(1, 2))),
                () -> assertTrue(br.getWidthHeight().equalTo(new Position(20, 20))));
    }
}