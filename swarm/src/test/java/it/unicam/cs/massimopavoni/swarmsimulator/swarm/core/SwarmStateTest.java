package it.unicam.cs.massimopavoni.swarmsimulator.swarm.core;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.Drone;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.TestUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Region;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.Circle;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.ShapeFactory;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.ShapeType;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.SwarmShapeFactory;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class SwarmStateTest {
    final AtomicReference<SwarmState> swarmState = new AtomicReference<>();
    final ShapeFactory shapeFactory = new SwarmShapeFactory();

    @BeforeAll
    static void setUp() throws HiveMindException {
        TestUtils.initializeProperties(SwarmStateTest.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() throws IOException {
        TestUtils.deleteProperties(SwarmStateTest.class.getSimpleName());
    }

    @Test
    void initializeParsers() {
        DirectiveFactory directiveFactory = new SwarmDirectiveFactory();
        assertAll(
                () -> assertDoesNotThrow(() -> SwarmState.initializeParsers(shapeFactory, directiveFactory)),
                () -> assertDoesNotThrow(() -> SwarmState.initializeParsers(shapeFactory, null)),
                () -> assertDoesNotThrow(() -> SwarmState.initializeParsers(null, directiveFactory)),
                () -> assertDoesNotThrow(() -> SwarmState.initializeParsers(null, null)));
    }

    @Test
    void SwarmState_notEnoughDrones() {
        AtomicReference<Exception> e = new AtomicReference<>();
        SwarmState.initializeParsers(shapeFactory, null);
        assertAll(
                () -> e.set(assertThrowsExactly(SwarmException.class,
                        () -> swarmState.set(new SwarmState(new File(
                                Objects.requireNonNull(SwarmStateTest.class.getClassLoader().getResource(
                                        "it/unicam/cs/massimopavoni/swarmsimulator/swarm/domain/parser/" +
                                                "testDomain.swarm")).getPath()),
                                new File(Objects.requireNonNull(SwarmStateTest.class.getClassLoader().getResource(
                                        "it/unicam/cs/massimopavoni/swarmsimulator/swarm/strategy/parser/" +
                                                "testStrategy.swarm")).getPath()),
                                0, shapeFactory.createShape(
                                ShapeType.CIRCLE, new double[]{0, 0, 20}), true)))),
                () -> assertTrue(e.get().getMessage().toLowerCase().contains("drone")));
    }

    @Test
    void SwarmState_tooManyDrones() {
        AtomicReference<Exception> e = new AtomicReference<>();
        SwarmState.initializeParsers(shapeFactory, null);
        assertAll(
                () -> e.set(assertThrowsExactly(SwarmException.class,
                        () -> swarmState.set(new SwarmState(new File(
                                Objects.requireNonNull(SwarmStateTest.class.getClassLoader().getResource(
                                        "it/unicam/cs/massimopavoni/swarmsimulator/swarm/domain/parser/" +
                                                "testDomain.swarm")).getPath()),
                                new File(Objects.requireNonNull(SwarmStateTest.class.getClassLoader().getResource(
                                        "it/unicam/cs/massimopavoni/swarmsimulator/swarm/strategy/parser/" +
                                                "testStrategy.swarm")).getPath()),
                                65537, shapeFactory.createShape(
                                ShapeType.CIRCLE, new double[]{0, 0, 20}), true)))),
                () -> assertTrue(e.get().getMessage().toLowerCase().contains("drone")));
    }

    @Test
    void SwarmState_fromFile() {
        SwarmState.initializeParsers(shapeFactory, null);
        SwarmState_fromSomething(
                () -> swarmState.set(new SwarmState(new File(
                        Objects.requireNonNull(SwarmStateTest.class.getClassLoader().getResource(
                                "it/unicam/cs/massimopavoni/swarmsimulator/swarm/domain/parser/" +
                                        "testDomain.swarm")).getPath()),
                        new File(Objects.requireNonNull(SwarmStateTest.class.getClassLoader().getResource(
                                "it/unicam/cs/massimopavoni/swarmsimulator/swarm/strategy/parser/" +
                                        "testStrategy.swarm")).getPath()),
                        256, shapeFactory.createShape(
                        ShapeType.CIRCLE, new double[]{0, 0, 20}), true)));
    }

    @Test
    void SwarmState_fromPath() {
        SwarmState.initializeParsers(shapeFactory, null);
        SwarmState_fromSomething(
                () -> swarmState.set(new SwarmState(Path.of(
                        Objects.requireNonNull(SwarmStateTest.class.getClassLoader().getResource(
                                "it/unicam/cs/massimopavoni/swarmsimulator/swarm/domain/parser/" +
                                        "testDomain.swarm")).getPath()),
                        Path.of(Objects.requireNonNull(SwarmStateTest.class.getClassLoader().getResource(
                                "it/unicam/cs/massimopavoni/swarmsimulator/swarm/strategy/parser/" +
                                        "testStrategy.swarm")).getPath()),
                        256, shapeFactory.createShape(
                        ShapeType.CIRCLE, new double[]{0, 0, 20}), true)));
    }

    @Test
    void SwarmState_fromString() {
        SwarmState.initializeParsers(shapeFactory, null);
        SwarmState_fromSomething(
                () -> swarmState.set(new SwarmState("""
                        first_shape CIRCLE 1 2 3
                        second_shape ELLIPSE 4 5 6 7
                        third_shape POLYGON 8 9 10 11 12 13 14 15 16 17
                        fourth_shape RECTANGLE 18 19 20 21""",
                        """
                                UNTIL first_shape
                                FOLLOW in_first_shape 100 5
                                CONTINUE 10
                                DONE
                                SIGNAL in_first_shape
                                STOP""",
                        256, shapeFactory.createShape(
                        ShapeType.CIRCLE, new double[]{0, 0, 20}), true)));
    }

    void SwarmState_fromSomething(Executable something) {
        Circle spawnCircle = (Circle) shapeFactory.createShape(ShapeType.CIRCLE, new double[]{0, 0, 20});
        AtomicReference<Drone> oneDrone = new AtomicReference<>();
        assertAll(
                () -> assertDoesNotThrow(something),
                () -> assertEquals(
                        List.of(ShapeType.CIRCLE, ShapeType.ELLIPSE, ShapeType.POLYGON, ShapeType.RECTANGLE),
                        swarmState.get().domain().stream().map(Region::shapeType).toList()),
                () -> assertEquals(
                        List.of(Until.class, Follow.class, Continue.class, Done.class, Radiate.class, Stop.class),
                        swarmState.get().strategy().stream().map(Directive::getClass).toList()),
                () -> assertTrue(swarmState.get().swarm().stream()
                        .allMatch(d -> SwarmUtils.compare(d.position()
                                .distanceTo(spawnCircle.getCenter()), spawnCircle.getRadius().x()) == 0)),
                () -> oneDrone.set(swarmState.get().swarm().getFirst()),
                () -> assertEquals(0, oneDrone.get().jumpCounter(0)),
                () -> assertThrowsExactly(SwarmException.class, () -> oneDrone.get().jumpCounter(1)),
                () -> assertEquals(0, oneDrone.get().jumpCounter(2)),
                () -> assertEquals(0, oneDrone.get().jumpCounter(3)),
                () -> assertThrowsExactly(SwarmException.class, () -> oneDrone.get().jumpCounter(4)),
                () -> assertThrowsExactly(SwarmException.class, () -> oneDrone.get().jumpCounter(5)),
                () -> assertArrayEquals(new double[]{0, 0, 20}, swarmState.get().spawnShape().getProperties()));
    }
}