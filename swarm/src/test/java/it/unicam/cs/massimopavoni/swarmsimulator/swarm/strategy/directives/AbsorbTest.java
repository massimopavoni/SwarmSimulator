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

import static org.junit.jupiter.api.Assertions.*;

class AbsorbTest {
    @BeforeAll
    static void setUp() throws HiveMindException {
        TestUtils.initializeProperties(AbsorbTest.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() throws IOException {
        TestUtils.deleteProperties(AbsorbTest.class.getSimpleName());
    }

    @Test
    void Absorb_illegalEcho() {
        AtomicReference<Exception> e = new AtomicReference<>();
        assertAll(
                () -> e.set(assertThrowsExactly(DirectiveException.class, () -> new Absorb("no luck"))),
                () -> assertTrue(e.get().getMessage().toLowerCase().contains("echo")));
    }

    @Test
    void Absorb_correct() {
        AtomicReference<Absorb> d = new AtomicReference<>();
        assertAll(
                () -> assertDoesNotThrow(() -> d.set(new Absorb("lucky13"))),
                () -> assertEquals("lucky13", d.get().echo()));
    }

    @Test
    void execute_absorb() throws DomainParserException, StrategyParserException {
        SwarmState swarmState = TestUtils.getNewDefaultTestSwarmState(256,
                ShapeType.CIRCLE, new double[]{0, 0, 20}, true);
        Absorb d = new Absorb("lucky13");
        Drone oneDrone = swarmState.swarm().getFirst();
        oneDrone.radiate("lucky13");
        assertAll(
                () -> assertDoesNotThrow(() -> d.execute(swarmState, oneDrone)),
                () -> assertFalse(oneDrone.isRadiating("lucky13")));
    }
}