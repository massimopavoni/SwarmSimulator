package it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmProperties;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.Circle;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.ShapeType;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.SwarmShapeFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class RegionTest {
    static MockedStatic<SwarmProperties> swarmPropertiesMockedStatic;
    SwarmShapeFactory swarmShapeFactory = new SwarmShapeFactory();

    @BeforeAll
    static void setUp() {
        swarmPropertiesMockedStatic = mockStatic(SwarmProperties.class);
        swarmPropertiesMockedStatic.when(SwarmProperties::tolerance).thenReturn(Math.pow(10, -12));
        swarmPropertiesMockedStatic.when(SwarmProperties::maxPolygonVertices).thenReturn(256);
        swarmPropertiesMockedStatic.when(SwarmProperties::signalPattern)
                .thenReturn(Pattern.compile("^[A-Za-z\\d_]+$"));
    }

    @AfterAll
    static void tearDown() {
        swarmPropertiesMockedStatic.close();
    }

    @Test
    void Region_noMatchSignal() {
        assertThrowsExactly(DomainException.class,
                () -> new Region("No luck with spaces, bro",
                        ShapeType.CIRCLE, swarmShapeFactory.createShape(ShapeType.CIRCLE, new double[]{0, 0, 1})));
    }

    @Test
    void Region_matchingSignal() {
        AtomicReference<Region> r = new AtomicReference<>();
        assertAll(
                () -> assertDoesNotThrow(
                        () -> r.set(new Region("I_are_programmer_I_make_computer_Beep_boop_Beep_beep_Boop",
                                ShapeType.CIRCLE,
                                swarmShapeFactory.createShape(ShapeType.CIRCLE, new double[]{0, 0, 1})))),
                () -> assertEquals("I_are_programmer_I_make_computer_Beep_boop_Beep_beep_Boop",
                        r.get().signal()),
                () -> assertEquals(ShapeType.CIRCLE, r.get().shapeType()),
                () -> assertTrue(((Circle) r.get().shape()).getCenter().equalTo(new Position(0, 0))),
                () -> assertTrue(((Circle) r.get().shape()).getRadius().equalTo(new Position(1, 1))));
    }
}