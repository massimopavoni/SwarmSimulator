package it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.TestUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.HiveMindException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmProperties;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {
    @BeforeAll
    static void setUp() throws HiveMindException {
        TestUtils.initializeProperties(PositionTest.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() throws IOException {
        TestUtils.deleteProperties(PositionTest.class.getSimpleName());
    }

    @Test
    void Position_nonFinite() {
        assertAll(
                () -> assertThrowsExactly(PositionException.class,
                        () -> new Position(Double.POSITIVE_INFINITY, 0)),
                () -> assertThrowsExactly(PositionException.class,
                        () -> new Position(0, Double.POSITIVE_INFINITY)),
                () -> assertThrowsExactly(PositionException.class,
                        () -> new Position(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)),
                () -> assertThrowsExactly(PositionException.class,
                        () -> new Position(Double.NEGATIVE_INFINITY, 0)),
                () -> assertThrowsExactly(PositionException.class,
                        () -> new Position(0, Double.NEGATIVE_INFINITY)),
                () -> assertThrowsExactly(PositionException.class,
                        () -> new Position(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY)),
                () -> assertThrowsExactly(PositionException.class,
                        () -> new Position(Double.NaN, 0)),
                () -> assertThrowsExactly(PositionException.class,
                        () -> new Position(0, Double.NaN)),
                () -> assertThrowsExactly(PositionException.class,
                        () -> new Position(Double.NaN, Double.NaN)));
    }

    @Test
    void Position_finite() {
        Position p = new Position(1, 2);
        assertAll(
                () -> assertEquals(1, p.x()),
                () -> assertEquals(2, p.y()));
    }

    @Test
    void Position_fromPosition() {
        Position p = new Position(8, -4);
        Position pp = new Position(p);
        assertAll(
                () -> assertEquals(8, pp.x()),
                () -> assertEquals(-4, pp.y()));
    }

    @Test
    void random_normal() {
        Position llp = new Position(-1, -2);
        Position urp = new Position(3, 4);
        Position p = Position.random(llp, urp);
        assertAll(
                () -> assertTrue(SwarmUtils.compare(p.x(), -1) >= 0 && SwarmUtils.compare(p.x(), 3) <= 0),
                () -> assertTrue(SwarmUtils.compare(p.y(), -2) >= 0 && SwarmUtils.compare(p.y(), 4) <= 0));
    }

    @Test
    void random_inverted() {
        Position llp = new Position(3, 4);
        Position urp = new Position(-1, -2);
        Position p = Position.random(llp, urp);
        assertAll(
                () -> assertTrue(SwarmUtils.compare(p.x(), -1) >= 0 && SwarmUtils.compare(p.x(), 3) <= 0),
                () -> assertTrue(SwarmUtils.compare(p.y(), -2) >= 0 && SwarmUtils.compare(p.y(), 4) <= 0));
    }

    @Test
    void random_equal() {
        Position llp0 = new Position(1, 2);
        Position urp0 = new Position(1, 3);
        Position llp1 = new Position(1, 2);
        Position urp1 = new Position(2, 2);
        Position llp2 = new Position(1, 2);
        Position urp2 = new Position(1, 2);
        Position p0 = Position.random(llp0, urp0);
        Position p1 = Position.random(llp1, urp1);
        Position p2 = Position.random(llp2, urp2);
        assertAll(
                () -> assertTrue(SwarmUtils.compare(p0.x(), 1 - SwarmProperties.tolerance()) >= 0 &&
                        SwarmUtils.compare(p0.x(), 1 + SwarmProperties.tolerance()) <= 0),
                () -> assertTrue(SwarmUtils.compare(p0.y(), 2) >= 0 && SwarmUtils.compare(p0.y(), 3) <= 0),
                () -> assertTrue(SwarmUtils.compare(p1.x(), 1) >= 0 && SwarmUtils.compare(p1.x(), 2) <= 0),
                () -> assertTrue(SwarmUtils.compare(p1.y(), 2 - SwarmProperties.tolerance()) >= 0 &&
                        SwarmUtils.compare(p1.y(), 2 + SwarmProperties.tolerance()) <= 0),
                () -> assertTrue(SwarmUtils.compare(p2.x(), 1 - SwarmProperties.tolerance()) >= 0 &&
                        SwarmUtils.compare(p2.x(), 1 + SwarmProperties.tolerance()) <= 0),
                () -> assertTrue(SwarmUtils.compare(p2.y(), 2 - SwarmProperties.tolerance()) >= 0 &&
                        SwarmUtils.compare(p2.y(), 2 + SwarmProperties.tolerance()) <= 0));
    }

    @Test
    void averageOf() {
        assertAll(
                () -> assertTrue(Position.averageOf(Collections.emptyList()).equalTo(new Position(0, 0))),
                () -> assertTrue(Position.averageOf(List.of(
                        new Position(3, 2),
                        new Position(-4, 8),
                        new Position(1, -10),
                        new Position(-2, -5),
                        new Position(9, 6))).equalTo(new Position(1.4, 0.2))));
    }

    @Test
    void isPositive() {
        assertAll(
                () -> assertTrue(new Position(1, 1).isPositive()),
                () -> assertFalse(new Position(0, 0).isPositive()),
                () -> assertFalse(new Position(0, 1).isPositive()),
                () -> assertFalse(new Position(-1, 1).isPositive()),
                () -> assertFalse(new Position(-1, 0).isPositive()),
                () -> assertFalse(new Position(-1, -1).isPositive()),
                () -> assertFalse(new Position(0, -1).isPositive()),
                () -> assertFalse(new Position(1, -1).isPositive()),
                () -> assertFalse(new Position(1, 0).isPositive()));
    }

    @Test
    void equalTo() {
        Position p = new Position(-2, 3);
        assertAll(
                () -> assertFalse(p.equalTo(new Position(-2.000000000001, 2.999999999998))),
                () -> assertFalse(p.equalTo(new Position(-2.0000000000009, 2.999999999998))),
                () -> assertFalse(p.equalTo(new Position(-2.000000000001, 2.9999999999999))),
                () -> assertTrue(p.equalTo(new Position(-2.0000000000009, 2.9999999999999))));
    }

    @Test
    void translate() {
        Position p = new Position(3, 4);
        Position dp = new Position(6, Math.sqrt(7));
        Position pd = p.translate(dp);
        assertTrue(pd.equalTo(new Position(9, 6.64575131106459059050162)));
    }

    @Test
    void scale_nonFinite() {
        Position p = new Position(7, -2);
        assertAll(
                () -> assertThrowsExactly(PositionException.class,
                        () -> p.scale(Double.POSITIVE_INFINITY)),
                () -> assertThrowsExactly(PositionException.class,
                        () -> p.scale(Double.NEGATIVE_INFINITY)),
                () -> assertThrowsExactly(PositionException.class,
                        () -> p.scale(Double.NaN)));
    }

    @Test
    void scale_finite() {
        Position p = new Position(3.5, -0.4);
        Position ps = p.scale(Math.sqrt(23.765));
        assertTrue(ps.equalTo(new Position(17.0622756395505461896230, -1.94997435880577670738548)));
    }

    @Test
    void distanceTo() {
        Position p0 = new Position(-1, 5);
        Position p1 = new Position(8.547, 1.333);
        assertEquals(0, SwarmUtils.compare(p0.distanceTo(p1), 10.2270278184817704151750));
    }

    @Test
    void manhattanDistanceTo() {
        Position p0 = new Position(4, -26);
        Position p1 = new Position(-222, -88);
        assertEquals(0, SwarmUtils.compare(p0.manhattanDistanceTo(p1), 288));
    }

    @Test
    void directionTo_same() {
        Position p0 = new Position(2, 9);
        Position p1 = new Position(2, 9);
        Position pt = p0.directionTo(p1);
        assertTrue(pt.equalTo(new Position(0, 0)));
    }

    @Test
    void directionTo_different() {
        Position p0 = new Position(2, 9);
        Position p1 = new Position(-8.7, -0.5);
        Position pt = p0.directionTo(p1);
        assertTrue(pt.equalTo(new Position(-0.747794778251355629849493, -0.663929943307278362950484)));
    }
}