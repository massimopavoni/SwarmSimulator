package it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.TestUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.HiveMindException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class SwarmShapeFactoryTest {
    SwarmShapeFactory swarmShapeFactory = new SwarmShapeFactory();

    @BeforeAll
    static void setUp() throws HiveMindException {
        TestUtils.initializeProperties(SwarmShapeFactoryTest.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() throws IOException {
        TestUtils.deleteProperties(SwarmShapeFactoryTest.class.getSimpleName());
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
                                swarmShapeFactory.createShape(ShapeType.CIRCLE, new double[]{0, 0, 1}))),
                () -> assertEquals(ShapeType.CIRCLE.getShapeClass(), circle.get().getClass()),
                () -> assertDoesNotThrow(
                        () -> ellipse.set(
                                swarmShapeFactory.createShape(ShapeType.ELLIPSE, new double[]{0, 0, 1, 2}))),
                () -> assertEquals(ShapeType.ELLIPSE.getShapeClass(), ellipse.get().getClass()),
                () -> assertDoesNotThrow(
                        () -> polygon.set(
                                swarmShapeFactory.createShape(ShapeType.POLYGON, new double[]{0, 0, 1, 1, 2, 2}))),
                () -> assertEquals(ShapeType.POLYGON.getShapeClass(), polygon.get().getClass()),
                () -> assertDoesNotThrow(
                        () -> rectangle.set(
                                swarmShapeFactory.createShape(ShapeType.RECTANGLE, new double[]{0, 0, 1, 2}))),
                () -> assertEquals(ShapeType.RECTANGLE.getShapeClass(), rectangle.get().getClass()));
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