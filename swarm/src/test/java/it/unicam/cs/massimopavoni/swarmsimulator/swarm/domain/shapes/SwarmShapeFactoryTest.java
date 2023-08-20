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
    ShapeFactory swarmShapeFactory = new SwarmShapeFactory();
    AtomicReference<Shape> shape = new AtomicReference<>();

    @BeforeAll
    static void setUp() throws HiveMindException {
        TestUtils.initializeProperties(SwarmShapeFactoryTest.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() throws IOException {
        TestUtils.deleteProperties(SwarmShapeFactoryTest.class.getSimpleName());
    }

    @Test
    void createShape_correctShapeCircle() {
        assertAll(
                () -> assertDoesNotThrow(
                        () -> shape.set(
                                swarmShapeFactory.createShape(ShapeType.CIRCLE, new double[]{0, 0, 1}))),
                () -> assertEquals(ShapeType.CIRCLE.getShapeClass(), shape.get().getClass()));
    }

    @Test
    void createShape_correctShapeEllipse() {
        assertAll(
                () -> assertDoesNotThrow(
                        () -> shape.set(
                                swarmShapeFactory.createShape(ShapeType.ELLIPSE, new double[]{0, 0, 1, 2}))),
                () -> assertEquals(ShapeType.ELLIPSE.getShapeClass(), shape.get().getClass()));
    }

    @Test
    void createShape_correctShapePolygon() {
        assertAll(
                () -> assertDoesNotThrow(
                        () -> shape.set(
                                swarmShapeFactory.createShape(ShapeType.POLYGON, new double[]{0, 0, 1, 1, 2, 2}))),
                () -> assertEquals(ShapeType.POLYGON.getShapeClass(), shape.get().getClass()));
    }

    @Test
    void createShape_correctShapeRectangle() {
        assertAll(
                () -> assertDoesNotThrow(
                        () -> shape.set(
                                swarmShapeFactory.createShape(ShapeType.RECTANGLE, new double[]{0, 0, 1, 2}))),
                () -> assertEquals(ShapeType.RECTANGLE.getShapeClass(), shape.get().getClass()));
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