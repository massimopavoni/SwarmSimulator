package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.Drone;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.TestUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.HiveMindException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmState;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Position;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.parser.DomainParserException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.ShapeFactory;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.ShapeType;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.SwarmShapeFactory;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.parser.StrategyParserException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class UntilTest {
    @BeforeAll
    static void setUp() throws HiveMindException {
        TestUtils.initializeProperties(UntilTest.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() throws IOException {
        TestUtils.deleteProperties(UntilTest.class.getSimpleName());
    }

    @Test
    void Until_throw() {
        AtomicReference<Exception> e = new AtomicReference<>();
        assertAll(
                () -> e.set(assertThrowsExactly(DirectiveException.class,
                        () -> new Until(9, "no luck"))),
                () -> assertTrue(e.get().getMessage().toLowerCase().contains("signal")));
    }

    @Test
    void Until_correct() {
        AtomicReference<Until> d = new AtomicReference<>();
        assertAll(
                () -> assertDoesNotThrow(() -> d.set(new Until(9, "lucky13"))),
                () -> assertEquals(9, d.get().jumpIndex()),
                () -> assertEquals("lucky13", d.get().signal()));
    }

    @Test
    void execute_until() throws DomainParserException, StrategyParserException {
        ShapeFactory shapeFactory = new SwarmShapeFactory();
        SwarmState.initializeParsers(shapeFactory, null);
        SwarmState swarmState = new SwarmState(new File(
                Objects.requireNonNull(UntilTest.class.getClassLoader().getResource(
                        "it/unicam/cs/massimopavoni/swarmsimulator/swarm/domain/parser/" +
                                "testDomain.swarm")).getPath()),
                new File(Objects.requireNonNull(UntilTest.class.getClassLoader().getResource(
                        "it/unicam/cs/massimopavoni/swarmsimulator/swarm/strategy/parser/" +
                                "testStrategy.swarm")).getPath()),
                256, shapeFactory.createShape(
                ShapeType.CIRCLE, new double[]{0, 0, 20}), true);
        Until d = new Until(9, "first_shape");
        Drone oneDrone = swarmState.swarm().get(0);
        oneDrone.setCurrentDirective(3);
        assertAll(
                () -> assertEquals(0, oneDrone.jumpCounter(3)),
                () -> assertDoesNotThrow(() -> d.execute(swarmState, oneDrone)),
                () -> assertEquals(4, oneDrone.currentDirective()),
                () -> assertEquals(1, oneDrone.jumpCounter(3)),
                () -> oneDrone.setDirection(oneDrone.position().directionTo(new Position(1, 2))),
                () -> oneDrone.setSpeed(oneDrone.position().distanceTo(new Position(1, 2))),
                oneDrone::stepMove,
                () -> oneDrone.setCurrentDirective(3),
                () -> assertDoesNotThrow(() -> d.execute(swarmState, oneDrone)),
                () -> assertEquals(9, oneDrone.currentDirective()),
                () -> assertEquals(1, oneDrone.jumpCounter(3)));
    }
}