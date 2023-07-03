package it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmProperties;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Position;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class PolygonTest {
    static MockedStatic<SwarmProperties> swarmPropertiesMockedStatic;

    @BeforeAll
    static void setUp() throws IOException {
        swarmPropertiesMockedStatic = mockStatic(SwarmProperties.class);
        swarmPropertiesMockedStatic.when(SwarmProperties::tolerance).thenReturn(Math.pow(10, -12));
        swarmPropertiesMockedStatic.when(SwarmProperties::maxPolygonVertices).thenReturn(256);
    }

    @AfterAll
    static void tearDown() {
        swarmPropertiesMockedStatic.close();
    }

    @Test
    void Polygon_wrongNumberOfVertices() {
        Random random = new Random(13);
        List<Position> manyVertices = new ArrayList<>(Stream.generate(
                () -> new Position(random.nextDouble(), random.nextDouble())).limit(257).toList());
        assertAll(
                () -> assertThrowsExactly(ShapeException.class,
                        () -> new Polygon(new ArrayList<>(List.of(
                                new Position(0, 0),
                                new Position(1, 1))))),
                () -> assertThrowsExactly(ShapeException.class,
                        () -> new Polygon(new ArrayList<>(List.of(
                                new Position(0, 0),
                                new Position(1, 1),
                                new Position(0, 0),
                                new Position(1, 1),
                                new Position(0, 0))))),
                () -> assertThrowsExactly(ShapeException.class,
                        () -> new Polygon(manyVertices)));
    }

    @Test
    void Polygon_correctNumberOfVertices() {
        assertDoesNotThrow(() -> new Polygon(new ArrayList<>(List.of(
                new Position(0, 0),
                new Position(1, 1),
                new Position(1, 0)))));
    }

    @Test
    void getVertices() {
        Polygon p = new Polygon(new ArrayList<>(List.of(
                new Position(0, 0),
                new Position(1, 1),
                new Position(1, 0))));
        assertAll(
                () -> assertEquals(3, p.getVertices().size()),
                () -> assertThrowsExactly(UnsupportedOperationException.class,
                        () -> p.getVertices().add(new Position(2, 2))),
                () -> assertThrowsExactly(UnsupportedOperationException.class,
                        () -> p.getVertices().set(0, new Position(2, 2))));
    }

    // https://www.geogebra.org/classic/xyfj7wzm
    @Test
    void contains_vertex() {
        Polygon p = new Polygon(new ArrayList<>(List.of(
                new Position(0, 0),
                new Position(1, 1),
                new Position(1, 0))));
        assertAll(
                () -> assertFalse(p.contains(new Position(1.1, 1.1))),
                () -> assertTrue(p.contains(new Position(1.0000000000001, 1.0000000000001))));
    }

    // https://www.geogebra.org/classic/qa9xdtnu
    @Test
    void contains_edge() {
        Polygon p = new Polygon(new ArrayList<>(List.of(
                new Position(0, 0),
                new Position(1, 1),
                new Position(1, 0))));
        assertAll(
                () -> assertFalse(p.contains(new Position(0.5, 0.55))),
                () -> assertTrue(p.contains(new Position(0.5000000000001, 0.5000000000001))));
    }

    // https://www.geogebra.org/classic/kdgjn7tv
    @Test
    void contains_simpleConvex() {
        Polygon p = new Polygon(new ArrayList<>(List.of(
                new Position(0, 0),
                new Position(1, 1),
                new Position(1, 0))));
        assertAll(
                () -> assertFalse(p.contains(new Position(0.2, 0.8))),
                () -> assertTrue(p.contains(new Position(0.1, 0.01))),
                () -> assertTrue(p.contains(new Position(0.8, 0.000004))),
                () -> assertTrue(p.contains(new Position(0.7, 0.3))));
    }

    // https://www.geogebra.org/classic/zrwmzwpr
    @Test
    void contains_bigConvex() {
        Polygon p = new Polygon(new ArrayList<>(List.of(
                new Position(2, 5),
                new Position(5, 3),
                new Position(6, -1),
                new Position(3, -4),
                new Position(0, -5),
                new Position(-4, -3),
                new Position(-7, 0),
                new Position(-5, 4))));
        assertAll(
                () -> assertFalse(p.contains(new Position(-2, 6))),
                () -> assertTrue(p.contains(new Position(0, 0))),
                () -> assertTrue(p.contains(new Position(3, 3))),
                () -> assertTrue(p.contains(new Position(-5.2, -1.2))),
                () -> assertTrue(p.contains(new Position(-6.4, 0))),
                () -> assertTrue(p.contains(new Position(3.5, -2))),
                () -> assertTrue(p.contains(new Position(-1.3, -2.8))),
                () -> assertTrue(p.contains(new Position(-4.8, 3.1))));
    }

    // https://www.geogebra.org/classic/dveagz8b
    @Test
    void contains_simpleConcave() {
        Polygon p = new Polygon(new ArrayList<>(List.of(
                new Position(-4, 4),
                new Position(-3, -2),
                new Position(5, -6),
                new Position(0, 1),
                new Position(5, 4))));
        assertAll(
                () -> assertFalse(p.contains(new Position(3, -2))),
                () -> assertFalse(p.contains(new Position(1, 1))),
                () -> assertTrue(p.contains(new Position(-2, -1))),
                () -> assertTrue(p.contains(new Position(1.5, 3))),
                () -> assertTrue(p.contains(new Position(3.5, -4.5))));
    }

    // https://www.geogebra.org/classic/phubvbxd
    @Test
    void contains_bigConcave() {
        Polygon p = new Polygon(new ArrayList<>(List.of(
                new Position(2, 12),
                new Position(-2, 10),
                new Position(1, 9),
                new Position(-4, 8),
                new Position(-7, 3),
                new Position(-1, 5),
                new Position(-5, -1),
                new Position(0, 0),
                new Position(2, 3),
                new Position(3, 1),
                new Position(6, 6),
                new Position(1.25, 7))));
        assertAll(
                () -> assertFalse(p.contains(new Position(-1, 9))),
                () -> assertFalse(p.contains(new Position(1.6, 7.3))),
                () -> assertFalse(p.contains(new Position(-3, 3))),
                () -> assertFalse(p.contains(new Position(2, 2.5))),
                () -> assertTrue(p.contains(new Position(1.2, 9))),
                () -> assertTrue(p.contains(new Position(0.2, 10.4))),
                () -> assertTrue(p.contains(new Position(-6, 4))),
                () -> assertTrue(p.contains(new Position(-4, 0))),
                () -> assertTrue(p.contains(new Position(0, 4))),
                () -> assertTrue(p.contains(new Position(-3, 7))),
                () -> assertTrue(p.contains(new Position(5, 6))));
    }

    // https://www.geogebra.org/classic/qqe7fdqm
    @Test
    void contains_complex() {
        Polygon p = new Polygon(new ArrayList<>(List.of(
                new Position(0, 0),
                new Position(0, 3),
                new Position(3, 1),
                new Position(2, 4))));
        assertAll(
                () -> assertFalse(p.contains(new Position(1, 3))),
                () -> assertFalse(p.contains(new Position(1.3, 1.4))),
                () -> assertTrue(p.contains(new Position(0.5, 2.2))),
                () -> assertTrue(p.contains(new Position(2.3, 2))),
                () -> assertTrue(p.contains(new Position(1.2, 2.27))),
                () -> assertTrue(p.contains(new Position(1.125, 2.25))));
    }

    // https://www.geogebra.org/classic/rbjtzrsp
    @Test
    void contains_complexLoopHole() {
        Polygon p = new Polygon(new ArrayList<>(List.of(
                new Position(-4, 1),
                new Position(3, 2),
                new Position(1, 5),
                new Position(-1.5, 2.5),
                new Position(1, 3),
                new Position(-2, 5),
                new Position(-2, -1))));
        assertAll(
                () -> assertFalse(p.contains(new Position(-0.2, 3.2))),
                () -> assertFalse(p.contains(new Position(-0.5, 4.7))),
                () -> assertFalse(p.contains(new Position(-2.5, 2))),
                () -> assertFalse(p.contains(new Position(-1, 1))),
                () -> assertTrue(p.contains(new Position(-2.5, 1))),
                () -> assertTrue(p.contains(new Position(-1, 3.5))),
                () -> assertTrue(p.contains(new Position(0, 2))),
                () -> assertTrue(p.contains(new Position(1.4, 3.4))));
    }

    // https://www.geogebra.org/classic/nqxpfkmq
    @Test
    void contains_bigComplex() {
        Polygon p = new Polygon(new ArrayList<>(List.of(
                new Position(-15, -10),
                new Position(15, -10),
                new Position(7.5, 2.99),
                new Position(3.75, -3.505),
                new Position(11.25, -3.505),
                new Position(7.5, -10),
                new Position(3.75, -3.506),
                new Position(0, -10),
                new Position(-3.75, -3.506),
                new Position(-7.5, -10),
                new Position(-11.25, -3.505),
                new Position(-3.75, -3.505),
                new Position(-7.5, 2.99),
                new Position(7.5, 2.991),
                new Position(0, 15.98),
                new Position(-3.75, 9.486),
                new Position(3.75, 9.485),
                new Position(0, 2.99),
                new Position(-3.75, 9.485))));
        assertAll(
                () -> assertFalse(p.contains(new Position(0, 0))),
                () -> assertFalse(p.contains(new Position(7.5, -7.5))),
                () -> assertFalse(p.contains(new Position(-8, -5))),
                () -> assertFalse(p.contains(new Position(1, 8))),
                () -> assertTrue(p.contains(new Position(-12, -8))),
                () -> assertTrue(p.contains(new Position(-2, -9))),
                () -> assertTrue(p.contains(new Position(-8, 0))),
                () -> assertTrue(p.contains(new Position(11, -5))),
                () -> assertTrue(p.contains(new Position(7, -1))),
                () -> assertTrue(p.contains(new Position(3, -6))),
                () -> assertTrue(p.contains(new Position(1, 13))),
                () -> assertTrue(p.contains(new Position(4, 5))),
                () -> assertTrue(p.contains(new Position(-6, 4))));
    }
}