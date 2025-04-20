package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.Drone;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.TestUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.HiveMindException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmState;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Position;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.parser.DomainParserException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.Circle;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.Rectangle;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.ShapeType;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.parser.StrategyParserException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class MoveTest {
    @BeforeAll
    static void setUp() throws HiveMindException {
        TestUtils.initializeProperties(MoveTest.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() throws IOException {
        TestUtils.deleteProperties(MoveTest.class.getSimpleName());
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
    void Move_correct() {
        AtomicReference<Move> d = new AtomicReference<>();
        assertAll(
                () -> assertDoesNotThrow(() -> d.set(new Move(new Position(5, 7), 2))),
                () -> assertTrue(new Position(5, 7).equalTo(d.get().destination())),
                () -> assertEquals(2, d.get().speed()));
    }

    @Test
    void Move_random() {
        AtomicReference<Move> d = new AtomicReference<>();
        assertAll(
                () -> assertDoesNotThrow(() -> d.set(new Move(
                        new Position(-2, -1), new Position(2, 1), 2))),
                () -> assertTrue(new Rectangle(new Position(0, 0), new Position(4, 2))
                        .contains(d.get().destination())),
                () -> assertEquals(2, d.get().speed()));
    }

    @Test
    void execute_move() throws DomainParserException, StrategyParserException {
        SwarmState swarmState = TestUtils.getNewDefaultTestSwarmState(256,
                ShapeType.CIRCLE, new double[]{0, 0, 20}, true);
        Drone oneDrone = swarmState.swarm().getFirst();
        Position start = new Position(5, 17);
        oneDrone.setDirection(oneDrone.position().directionTo(start));
        oneDrone.setSpeed(oneDrone.position().distanceTo(start));
        oneDrone.stepMove();
        Position destination = new Position(-44, 1);
        Position expectedDirection = oneDrone.position().directionTo(destination);
        double expectedSpeed = oneDrone.position().distanceTo(destination) / 2;
        Move d = new Move(destination, expectedSpeed);
        assertAll(
                () -> assertTrue(start.equalTo(oneDrone.position())),
                () -> assertDoesNotThrow(() -> d.execute(swarmState, oneDrone)),
                () -> assertTrue(expectedDirection.equalTo(oneDrone.direction())),
                () -> assertEquals(expectedSpeed, oneDrone.speed()),
                oneDrone::stepMove,
                () -> assertTrue(start.translate(expectedDirection.scale(expectedSpeed))
                        .equalTo(oneDrone.position())));
    }

    @Test
    void execute_moveRandom() throws DomainParserException, StrategyParserException {
        SwarmState swarmState = TestUtils.getNewDefaultTestSwarmState(256,
                ShapeType.CIRCLE, new double[]{0, 0, 20}, true);
        Drone oneDrone = swarmState.swarm().getFirst();
        Position start = new Position(4, 4);
        oneDrone.setDirection(oneDrone.position().directionTo(start));
        oneDrone.setSpeed(oneDrone.position().distanceTo(start));
        oneDrone.stepMove();
        Move d = new Move(new Position(-1, -1), new Position(9, 9), 2);
        assertAll(
                () -> assertTrue(start.equalTo(oneDrone.position())),
                () -> assertDoesNotThrow(() -> d.execute(swarmState, oneDrone)),
                () -> assertEquals(2, oneDrone.speed()),
                oneDrone::stepMove,
                () -> assertTrue(new Circle(new Position(4, 4), 2)
                        .contains(oneDrone.position())));
    }
}