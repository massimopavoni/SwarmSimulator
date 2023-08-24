package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.Drone;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.TestUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.HiveMindException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmState;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.parser.DomainParserException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.ShapeType;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.parser.StrategyParserException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class ContinueTest {
    @BeforeAll
    static void setUp() throws HiveMindException {
        TestUtils.initializeProperties(ContinueTest.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() throws IOException {
        TestUtils.deleteProperties(ContinueTest.class.getSimpleName());
    }

    @Test
    void Continue() {
        AtomicReference<Continue> d = new AtomicReference<>();
        assertAll(
                () -> assertDoesNotThrow(() -> d.set(new Continue(8))),
                () -> assertEquals(8, d.get().jumpLimit()));
    }

    @Test
    void execute_continue() throws DomainParserException, StrategyParserException {
        SwarmState swarmState = TestUtils.getNewTestSwarmState(256,
                ShapeType.CIRCLE, new double[]{0, 0, 20}, true);
        Continue d = new Continue(8);
        Drone oneDrone = swarmState.swarm().get(0);
        oneDrone.setCurrentDirective(2);
        assertAll(
                () -> assertEquals(0, oneDrone.jumpCounter(2)),
                () -> assertDoesNotThrow(() -> d.execute(swarmState, oneDrone)),
                () -> assertEquals(2, oneDrone.currentDirective()),
                () -> assertEquals(1, oneDrone.jumpCounter(2)),
                () -> IntStream.range(0, 6).forEach(i -> oneDrone.incrementJumpCounter(2)),
                () -> assertDoesNotThrow(() -> d.execute(swarmState, oneDrone)),
                () -> assertEquals(3, oneDrone.currentDirective()),
                () -> assertEquals(7, oneDrone.jumpCounter(2)));
    }
}