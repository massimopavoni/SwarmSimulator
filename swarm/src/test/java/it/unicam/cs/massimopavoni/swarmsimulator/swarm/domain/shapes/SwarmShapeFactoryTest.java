package it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmProperties;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class SwarmShapeFactoryTest {
    static MockedStatic<SwarmProperties> swarmPropertiesMockedStatic;
    SwarmShapeFactory swarmShapeFactory = new SwarmShapeFactory();

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
    void createShape_unavailableShape() {
        assertThrowsExactly(ShapeException.class,
                () -> swarmShapeFactory.createShape("unavailable", new double[]{0, 0, 1}));
    }

    @Test
    void createShape_correctShape() {
        AtomicReference<Shape> circle = new AtomicReference<>();
        AtomicReference<Shape> ellipse = new AtomicReference<>();
        AtomicReference<Shape> polygon = new AtomicReference<>();
        AtomicReference<Shape> rectangle = new AtomicReference<>();
        assertAll(
                () -> assertDoesNotThrow(
                        () -> circle.set(
                                swarmShapeFactory.createShape("ciRCle", new double[]{0, 0, 1}))),
                () -> assertEquals(ParserShape.CIRCLE.getShapeClass(), circle.get().getClass()),
                () -> assertDoesNotThrow(
                        () -> ellipse.set(
                                swarmShapeFactory.createShape("ELLipsE", new double[]{0, 0, 1, 2}))),
                () -> assertEquals(ParserShape.ELLIPSE.getShapeClass(), ellipse.get().getClass()),
                () -> assertDoesNotThrow(
                        () -> polygon.set(
                                swarmShapeFactory.createShape("PoLyGoN", new double[]{0, 0, 1, 1, 2, 2}))),
                () -> assertEquals(ParserShape.POLYGON.getShapeClass(), polygon.get().getClass()),
                () -> assertDoesNotThrow(
                        () -> rectangle.set(
                                swarmShapeFactory.createShape("RECTAngle", new double[]{0, 0, 1, 2}))),
                () -> assertEquals(ParserShape.RECTANGLE.getShapeClass(), rectangle.get().getClass()));
    }

    @Test
    void createCircle_invalidArguments() {
        assertAll(
                () -> assertThrowsExactly(ShapeException.class,
                        () -> swarmShapeFactory.createCircle(new double[]{0, 0})),
                () -> assertThrowsExactly(ShapeException.class,
                        () -> swarmShapeFactory.createCircle(new double[]{0, 0, 1, 1})));
    }

    @Test
    void createCircle_validArguments() {
        assertDoesNotThrow(() -> swarmShapeFactory.createCircle(new double[]{0, 0, 1}));
    }

    @Test
    void createEllipse_invalidArguments() {
        assertAll(
                () -> assertThrowsExactly(ShapeException.class,
                        () -> swarmShapeFactory.createEllipse(new double[]{0, 0, 1})),
                () -> assertThrowsExactly(ShapeException.class,
                        () -> swarmShapeFactory.createEllipse(new double[]{0, 0, 1, 2, 3})));
    }

    @Test
    void createEllipse_validArguments() {
        assertDoesNotThrow(() -> swarmShapeFactory.createEllipse(new double[]{0, 0, 1, 2}));
    }

    @Test
    void createPolygon_invalidArguments() {
        assertAll(
                () -> assertThrowsExactly(ShapeException.class,
                        () -> swarmShapeFactory.createPolygon(new double[]{0, 0, 1, 1})),
                () -> assertThrowsExactly(ShapeException.class,
                        () -> swarmShapeFactory.createPolygon(new double[]{0, 0, 1, 1, 2, 2, 3})));
    }

    @Test
    void createPolygon_validArguments() {
        assertAll(
                () -> assertDoesNotThrow(() -> swarmShapeFactory.createPolygon(new double[]{0, 0, 1, 1, 2, 2})),
                () -> assertDoesNotThrow(() -> swarmShapeFactory.createPolygon(new double[]{0, 0, 1, 1, 2, 2, 3, 3})));
    }

    @Test
    void createRectangle_invalidArguments() {
        assertAll(
                () -> assertThrowsExactly(ShapeException.class,
                        () -> swarmShapeFactory.createRectangle(new double[]{0, 0, 1})),
                () -> assertThrowsExactly(ShapeException.class,
                        () -> swarmShapeFactory.createRectangle(new double[]{0, 0, 1, 2, 3})));
    }

    @Test
    void createRectangle_validArguments() {
        assertDoesNotThrow(() -> swarmShapeFactory.createRectangle(new double[]{0, 0, 1, 2}));
    }
}