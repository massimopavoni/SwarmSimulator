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
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class FollowTest {
    @BeforeAll
    static void setUp() throws HiveMindException {
        TestUtils.initializeProperties(FollowTest.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() throws IOException {
        TestUtils.deleteProperties(FollowTest.class.getSimpleName());
    }

    @Test
    void Follow_illegalEcho() {
        AtomicReference<Exception> e = new AtomicReference<>();
        assertAll(
                () -> e.set(assertThrowsExactly(DirectiveException.class,
                        () -> new Follow("no luck", 100, 5))),
                () -> assertTrue(e.get().getMessage().toLowerCase().contains("echo")),
                () -> e.set(assertThrowsExactly(DirectiveException.class,
                        () -> new Follow("lucky13", -100, 5))),
                () -> assertTrue(e.get().getMessage().toLowerCase().contains("range")),
                () -> e.set(assertThrowsExactly(DirectiveException.class,
                        () -> new Follow("lucky13", 100, -5))),
                () -> assertTrue(e.get().getMessage().toLowerCase().contains("speed")),
                () -> e.set(assertThrowsExactly(DirectiveException.class,
                        () -> new Follow("lucky13", -100, -5))),
                () -> assertTrue(e.get().getMessage().toLowerCase().contains("range") &&
                        e.get().getMessage().toLowerCase().contains("speed")));
    }

    @Test
    void Follow_correct() {
        AtomicReference<Follow> d = new AtomicReference<>();
        assertAll(
                () -> assertDoesNotThrow(() -> d.set(new Follow("lucky13", 100, 5))),
                () -> assertEquals("lucky13", d.get().echo()),
                () -> assertEquals(100, d.get().range()),
                () -> assertEquals(5, d.get().speed()));
    }

    @Test
    void execute_follow() throws DomainParserException, StrategyParserException {
        SwarmState swarmState = TestUtils.getNewDefaultTestSwarmState(256,
                ShapeType.CIRCLE, new double[]{0, 0, 100}, true);
        Follow d = new Follow("follow_me_origin", 200, Math.sqrt(50));
        Drone oneDrone = swarmState.swarm().get(0);
        Position start = new Position(30, 30);
        oneDrone.setDirection(oneDrone.position().directionTo(start));
        oneDrone.setSpeed(oneDrone.position().distanceTo(start));
        oneDrone.stepMove();
        Drone[] followDrones = new Drone[]{swarmState.swarm().get(1), swarmState.swarm().get(2),
                swarmState.swarm().get(3), swarmState.swarm().get(4), swarmState.swarm().get(5),
                swarmState.swarm().get(6), swarmState.swarm().get(7)};
        Position[] followDronePositions = new Position[]{new Position(-30, -25), new Position(20, 10),
                new Position(-10, -5), new Position(15, 20), new Position(5, 0),
                new Position(45, -1), new Position(-77, 99)};
        for (int i = 0; i < 7; i++) {
            followDrones[i].setDirection(followDrones[i].position().directionTo(followDronePositions[i]));
            followDrones[i].setSpeed(followDrones[i].position().distanceTo(followDronePositions[i]));
            followDrones[i].stepMove();
            if (i < 5)
                followDrones[i].radiate("follow_me_origin");
        }
        followDrones[1].terminateLife();
        followDrones[4].terminateLife();
        Drone scoundrel = swarmState.swarm().get(128);
        scoundrel.setDirection(scoundrel.position().directionTo(new Position(400, 400)));
        scoundrel.setSpeed(scoundrel.position().distanceTo(new Position(400, 400)));
        scoundrel.stepMove();
        scoundrel.radiate("follow_me_origin");
        assertAll(
                () -> assertTrue(new Position(0, 0).equalTo(Position.averageOf(
                        Arrays.stream(Arrays.copyOf(followDronePositions, 5)).toList()))),
                () -> assertTrue(start.equalTo(oneDrone.position())),
                () -> assertDoesNotThrow(() -> d.execute(swarmState, oneDrone)),
                () -> assertTrue(new Position(-Math.sqrt(0.5), -Math.sqrt(0.5)).equalTo(oneDrone.direction())),
                () -> assertEquals(Math.sqrt(50), oneDrone.speed()),
                oneDrone::stepMove,
                () -> assertTrue(new Position(25, 25).equalTo(oneDrone.position())));
    }

    @Test
    void execute_followRandom() throws DomainParserException, StrategyParserException {
        SwarmState swarmState = TestUtils.getNewDefaultTestSwarmState(256,
                ShapeType.CIRCLE, new double[]{0, 0, 100}, true);
        Follow d = new Follow("follow_me_origin", 200, Math.sqrt(50));
        Drone oneDrone = swarmState.swarm().get(0);
        Position start = new Position(30, 30);
        oneDrone.setDirection(oneDrone.position().directionTo(start));
        oneDrone.setSpeed(oneDrone.position().distanceTo(start));
        oneDrone.stepMove();
        Drone[] followDrones = new Drone[]{swarmState.swarm().get(1), swarmState.swarm().get(2),
                swarmState.swarm().get(3), swarmState.swarm().get(4), swarmState.swarm().get(5),
                swarmState.swarm().get(6), swarmState.swarm().get(7)};
        Position[] followDronePositions = new Position[]{new Position(-30, -25), new Position(20, 10),
                new Position(-10, -5), new Position(15, 20), new Position(5, 0),
                new Position(45, -1), new Position(-77, 99)};
        for (int i = 0; i < 7; i++) {
            followDrones[i].setDirection(followDrones[i].position().directionTo(followDronePositions[i]));
            followDrones[i].setSpeed(followDrones[i].position().distanceTo(followDronePositions[i]));
            followDrones[i].stepMove();
            if (i < 5)
                followDrones[i].radiate("follow_me_origin_typolol");
        }
        followDrones[1].terminateLife();
        followDrones[4].terminateLife();
        Drone scoundrel = swarmState.swarm().get(128);
        scoundrel.setDirection(scoundrel.position().directionTo(new Position(400, 400)));
        scoundrel.setSpeed(scoundrel.position().distanceTo(new Position(400, 400)));
        scoundrel.stepMove();
        scoundrel.radiate("follow_me_origin");
        assertAll(
                () -> assertTrue(new Position(0, 0).equalTo(Position.averageOf(
                        Arrays.stream(Arrays.copyOf(followDronePositions, 5)).toList()))),
                () -> assertTrue(start.equalTo(oneDrone.position())),
                () -> assertDoesNotThrow(() -> d.execute(swarmState, oneDrone)),
                oneDrone::stepMove,
                () -> assertFalse(new Position(25, 25).equalTo(oneDrone.position())));
    }
}