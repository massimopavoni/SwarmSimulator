package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.Drone;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.TestUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.HiveMindException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmState;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Position;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.parser.DomainParserException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.ShapeType;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.parser.StrategyParserException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class StopTest {
    @BeforeAll
    static void setUp() throws HiveMindException {
        TestUtils.initializeProperties(StopTest.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() throws IOException {
        TestUtils.deleteProperties(StopTest.class.getSimpleName());
    }

    @Test
    void Move_negativeSpeed() {
        AtomicReference<Exception> e = new AtomicReference<>();
        assertAll(
                () -> e.set(assertThrowsExactly(DirectiveException.class,
                        () -> new Move(new Position(5, 7), -80))),
                () -> assertTrue(e.get().getMessage().toLowerCase().contains("speed")));
    }

    @Test
    void execute_stop() throws DomainParserException, StrategyParserException {
        SwarmState swarmState = TestUtils.getNewTestSwarmState(256,
                ShapeType.CIRCLE, new double[]{0, 0, 20}, true);
        Drone oneDrone = swarmState.swarm().get(0);
        Position start = new Position(-3, 6);
        oneDrone.setDirection(oneDrone.position().directionTo(start));
        oneDrone.setSpeed(oneDrone.position().distanceTo(start));
        oneDrone.stepMove();
        Position destination = new Position(8, -4);
        Position direction = oneDrone.position().directionTo(destination);
        oneDrone.setDirection(direction);
        double speed = oneDrone.position().distanceTo(destination) / 2;
        oneDrone.setSpeed(speed);
        Stop d = new Stop();
        assertAll(
                () -> assertTrue(start.equalTo(oneDrone.position())),
                oneDrone::stepMove,
                () -> assertTrue(start.translate(direction.scale(speed)).equalTo(oneDrone.position())),
                () -> assertDoesNotThrow(() -> d.execute(swarmState, oneDrone)),
                () -> assertTrue(new Position(0, 0).equalTo(oneDrone.direction())),
                () -> assertEquals(0, oneDrone.speed()),
                oneDrone::stepMove,
                () -> assertTrue(start.translate(direction.scale(speed)).equalTo(oneDrone.position())));
    }
}