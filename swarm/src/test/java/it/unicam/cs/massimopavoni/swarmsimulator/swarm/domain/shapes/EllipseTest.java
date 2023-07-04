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

class EllipseTest {
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
}