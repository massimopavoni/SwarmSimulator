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

class CircleTest {
    static MockedStatic<SwarmProperties> swarmPropertiesMockedStatic;

    @BeforeAll
    static void setUp() {
        swarmPropertiesMockedStatic = mockStatic(SwarmProperties.class);
        swarmPropertiesMockedStatic.when(SwarmProperties::tolerance).thenReturn(Math.pow(10, -12));
    }

    @AfterAll
    static void tearDown() {
        swarmPropertiesMockedStatic.close();
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
}