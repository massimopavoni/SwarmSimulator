package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.Drone;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.TestUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.HiveMindException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmState;
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
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class RepeatTest {
    @BeforeAll
    static void setUp() throws HiveMindException {
        TestUtils.initializeProperties(RepeatTest.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() throws IOException {
        TestUtils.deleteProperties(RepeatTest.class.getSimpleName());
    }

    @Test
    void Repeat_normal() {
        AtomicReference<Repeat> d = new AtomicReference<>();
        assertAll(
                () -> assertDoesNotThrow(() -> d.set(new Repeat(5, 3))),
                () -> assertEquals(5, d.get().jumpIndex()),
                () -> assertEquals(3, d.get().jumpLimit()));
    }

    @Test
    void Repeat_forever() {
        AtomicReference<Repeat> d = new AtomicReference<>();
        assertAll(
                () -> assertDoesNotThrow(() -> d.set(new Repeat(7))),
                () -> assertEquals(7, d.get().jumpIndex()),
                () -> assertEquals(0, d.get().jumpLimit()));
    }

    @Test
    void execute_repeat() throws DomainParserException, StrategyParserException {
        ShapeFactory shapeFactory = new SwarmShapeFactory();
        SwarmState.initializeParsers(shapeFactory, null);
        SwarmState swarmState = new SwarmState(new File(
                Objects.requireNonNull(RepeatTest.class.getClassLoader().getResource(
                        "it/unicam/cs/massimopavoni/swarmsimulator/swarm/domain/parser/" +
                                "testDomain.swarm")).getPath()),
                new File(Objects.requireNonNull(RepeatTest.class.getClassLoader().getResource(
                        "it/unicam/cs/massimopavoni/swarmsimulator/swarm/strategy/parser/" +
                                "testStrategy.swarm")).getPath()),
                256, shapeFactory.createShape(
                ShapeType.CIRCLE, new double[]{0, 0, 20}), true);
        Repeat d = new Repeat(5, 3);
        Drone oneDrone = swarmState.swarm().get(0);
        oneDrone.setCurrentDirective(3);
        assertAll(
                () -> assertEquals(0, oneDrone.jumpCounter(3)),
                () -> assertDoesNotThrow(() -> d.execute(swarmState, oneDrone)),
                () -> assertEquals(4, oneDrone.currentDirective()),
                () -> assertEquals(1, oneDrone.jumpCounter(3)),
                () -> oneDrone.incrementJumpCounter(3),
                () -> oneDrone.setCurrentDirective(3),
                () -> assertDoesNotThrow(() -> d.execute(swarmState, oneDrone)),
                () -> assertEquals(5, oneDrone.currentDirective()),
                () -> assertEquals(2, oneDrone.jumpCounter(3)));
    }

    @Test
    void execute_repeatForever() throws DomainParserException, StrategyParserException {
        ShapeFactory shapeFactory = new SwarmShapeFactory();
        SwarmState.initializeParsers(shapeFactory, null);
        SwarmState swarmState = new SwarmState(new File(
                Objects.requireNonNull(RepeatTest.class.getClassLoader().getResource(
                        "it/unicam/cs/massimopavoni/swarmsimulator/swarm/domain/parser/" +
                                "testDomain.swarm")).getPath()),
                new File(Objects.requireNonNull(RepeatTest.class.getClassLoader().getResource(
                        "it/unicam/cs/massimopavoni/swarmsimulator/swarm/strategy/parser/" +
                                "testStrategy.swarm")).getPath()),
                256, shapeFactory.createShape(
                ShapeType.CIRCLE, new double[]{0, 0, 20}), true);
        Repeat d = new Repeat(5);
        Drone oneDrone = swarmState.swarm().get(0);
        oneDrone.setCurrentDirective(3);
        assertAll(
                () -> assertEquals(0, oneDrone.jumpCounter(3)),
                () -> assertDoesNotThrow(() -> d.execute(swarmState, oneDrone)),
                () -> assertEquals(4, oneDrone.currentDirective()),
                () -> assertEquals(1, oneDrone.jumpCounter(3)),
                () -> IntStream.range(0, 8192).forEach(i -> oneDrone.incrementJumpCounter(3)),
                () -> oneDrone.setCurrentDirective(3),
                () -> assertDoesNotThrow(() -> d.execute(swarmState, oneDrone)),
                () -> assertEquals(4, oneDrone.currentDirective()),
                () -> assertEquals(8194, oneDrone.jumpCounter(3)));
    }
}