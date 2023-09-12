package it.unicam.cs.massimopavoni.swarmsimulator.swarm;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.HiveMindException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Position;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class DroneTest {
    @BeforeAll
    static void setUp() throws HiveMindException {
        TestUtils.initializeProperties(DroneTest.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() throws IOException {
        TestUtils.deleteProperties(DroneTest.class.getSimpleName());
    }

    @Test
    void Drone_constructor() {
        AtomicReference<Drone> d = new AtomicReference<>();
        assertAll(
                () -> assertDoesNotThrow(() -> d.set(new Drone(new Position(0, 0), List.of(0, 4, 9)))),
                () -> assertTrue(d.get().isAlive()),
                () -> assertEquals(0, d.get().currentDirective()),
                () -> assertEquals(0, d.get().jumpCounter(0)),
                () -> assertEquals(0, d.get().jumpCounter(4)),
                () -> assertEquals(0, d.get().jumpCounter(9)),
                () -> assertTrue(new Position(0, 0).equalTo(d.get().position())),
                () -> assertTrue(new Position(0, 0).equalTo(d.get().direction())),
                () -> assertEquals(0, SwarmUtils.compare(0.0, d.get().speed())));
    }

    @Test
    void terminateLife_death() {
        Drone d = new Drone(new Position(0, 0), List.of(0, 4, 9));
        d.setDirection(d.position().directionTo(new Position(1, 1)));
        d.setSpeed(1);
        assertAll(
                () -> assertDoesNotThrow(d::terminateLife),
                () -> assertFalse(d.isAlive()),
                () -> assertTrue(new Position(0, 0).equalTo(d.direction())),
                () -> assertEquals(0, SwarmUtils.compare(0, d.speed())));
    }

    @Test
    void setCurrentDirective_set() {
        Drone d = new Drone(new Position(0, 0), List.of(0, 4, 9));
        assertAll(
                () -> assertDoesNotThrow(() -> d.setCurrentDirective(3)),
                () -> assertEquals(3, d.currentDirective()));
    }

    @Test
    void incrementCurrentDirective_increment() {
        Drone d = new Drone(new Position(0, 0), List.of(0, 4, 9));
        assertAll(
                () -> assertDoesNotThrow(d::incrementCurrentDirective),
                () -> assertEquals(1, d.currentDirective()));
    }

    @Test
    void jumpCounter_notContains() {
        Drone d = new Drone(new Position(0, 0), List.of(0, 4, 9));
        assertThrowsExactly(SwarmException.class, () -> d.jumpCounter(1));
    }

    @Test
    void increaseJumpCounter_notContains() {
        Drone d = new Drone(new Position(0, 0), List.of(0, 4, 9));
        assertThrowsExactly(SwarmException.class, () -> d.increaseJumpCounter(1));
    }

    @Test
    void increaseJumpCounter_increment() {
        Drone d = new Drone(new Position(0, 0), List.of(0, 4, 9));
        assertAll(
                () -> assertDoesNotThrow(() -> d.increaseJumpCounter(4)),
                () -> assertEquals(0, d.jumpCounter(0)),
                () -> assertEquals(1, d.jumpCounter(4)),
                () -> assertEquals(0, d.jumpCounter(9)));
    }

    @Test
    void resetJumpCounter_notContains() {
        Drone d = new Drone(new Position(0, 0), List.of(0, 4, 9));
        assertThrowsExactly(SwarmException.class, () -> d.resetJumpCounter(1));
    }

    @Test
    void resetJumpCounter_increment() {
        Drone d = new Drone(new Position(0, 0), List.of(0, 4, 9));
        d.increaseJumpCounter(4);
        assertAll(
                () -> assertEquals(1, d.jumpCounter(4)),
                () -> assertDoesNotThrow(() -> d.resetJumpCounter(4)),
                () -> assertEquals(0, d.jumpCounter(4)));
    }

    @Test
    void setDirection_set() {
        Drone d = new Drone(new Position(0, 0), List.of(0, 4, 9));
        assertAll(
                () -> assertDoesNotThrow(() -> d.setDirection(d.position().directionTo(new Position(1, 1)))),
                () -> assertTrue(new Position(1 / Math.sqrt(2), 1 / Math.sqrt(2)).equalTo(d.direction())));
    }

    @Test
    void setSpeed_set() {
        Drone d = new Drone(new Position(0, 0), List.of(0, 4, 9));
        assertAll(
                () -> assertDoesNotThrow(() -> d.setSpeed(1)),
                () -> assertEquals(0, SwarmUtils.compare(1, d.speed())));
    }

    @Test
    void radiate_notGoodEcho() {
        AtomicReference<Exception> e = new AtomicReference<>();
        Drone d = new Drone(new Position(0, 0), List.of(0, 4, 9));
        assertAll(
                () -> e.set(assertThrowsExactly(SwarmException.class, () -> d.radiate("no good"))),
                () -> assertTrue(e.get().getMessage().toLowerCase().contains("echo")));
    }

    @Test
    void absorb_notGoodEcho() {
        AtomicReference<Exception> e = new AtomicReference<>();
        Drone d = new Drone(new Position(0, 0), List.of(0, 4, 9));
        assertAll(
                () -> e.set(assertThrowsExactly(SwarmException.class, () -> d.absorb("no good"))),
                () -> assertTrue(e.get().getMessage().toLowerCase().contains("echo")));
    }

    @Test
    void radiateAndAbsorb_sequence() {
        Drone d = new Drone(new Position(0, 0), List.of(0, 4, 9));
        assertAll(
                () -> assertFalse(d.isRadiating("first") || d.isRadiating("second")),
                () -> assertDoesNotThrow(() -> d.radiate("first")),
                () -> assertTrue(d.isRadiating("first") && !d.isRadiating("second")),
                () -> assertDoesNotThrow(() -> d.radiate("first")),
                () -> assertDoesNotThrow(() -> d.radiate("second")),
                () -> assertTrue(d.isRadiating("first") && d.isRadiating("second")),
                () -> assertDoesNotThrow(() -> d.absorb("first")),
                () -> assertTrue(!d.isRadiating("first") && d.isRadiating("second")),
                () -> assertDoesNotThrow(() -> d.absorb("first")),
                () -> assertDoesNotThrow(() -> d.absorb("second")),
                () -> assertFalse(d.isRadiating("first") || d.isRadiating("second")));
    }

    @Test
    void stepMove_correct() {
        Drone d = new Drone(new Position(0, 0), List.of(0, 4, 9));
        d.setDirection(d.position().directionTo(new Position(1, 1)));
        d.setSpeed(1);
        assertAll(
                () -> assertDoesNotThrow(d::stepMove),
                () -> assertTrue(new Position(1 / Math.sqrt(2), 1 / Math.sqrt(2)).equalTo(d.position())));
    }

    @Test
    void stepMove_multiple() {
        Drone d = new Drone(new Position(0, 0), List.of(0, 4, 9));
        d.setDirection(d.position().directionTo(new Position(1, 1)));
        d.setSpeed(1);
        d.stepMove();
        d.stepMove();
        d.stepMove();
        assertTrue(new Position(3 / Math.sqrt(2), 3 / Math.sqrt(2)).equalTo(d.position()));
    }
}