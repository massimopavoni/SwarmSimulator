package it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmProperties;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Position;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class RectangleTest {
    static MockedStatic<SwarmProperties> swarmPropertiesMockedStatic;

    @BeforeAll
    static void setUp() {
        swarmPropertiesMockedStatic = mockStatic(SwarmProperties.class);
        swarmPropertiesMockedStatic.when(SwarmProperties::tolerance).thenReturn(Math.pow(10, -12));
        swarmPropertiesMockedStatic.when(SwarmProperties::maxPolygonVertices).thenReturn(256);
    }

    @AfterAll
    static void tearDown() {
        swarmPropertiesMockedStatic.close();
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
}