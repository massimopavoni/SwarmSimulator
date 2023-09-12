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

class RadiateTest {
    @BeforeAll
    static void setUp() throws HiveMindException {
        TestUtils.initializeProperties(RadiateTest.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() throws IOException {
        TestUtils.deleteProperties(RadiateTest.class.getSimpleName());
    }

    @Test
    void Radiate_illegalEcho() {
        AtomicReference<Exception> e = new AtomicReference<>();
        assertAll(
                () -> e.set(assertThrowsExactly(DirectiveException.class, () -> new Radiate("mr anderson"))),
                () -> assertTrue(e.get().getMessage().toLowerCase().contains("echo")));
    }

    @Test
    void Radiate_correct() {
        AtomicReference<Radiate> d = new AtomicReference<>();
        assertAll(
                () -> assertDoesNotThrow(() -> d.set(new Radiate("neo_ooo"))),
                () -> assertEquals("neo_ooo", d.get().echo()));
    }

    @Test
    void execute_radiate() throws DomainParserException, StrategyParserException {
        SwarmState swarmState = TestUtils.getNewDefaultTestSwarmState(256,
                ShapeType.CIRCLE, new double[]{0, 0, 20}, true);
        Radiate d = new Radiate("neo_ooo");
        Drone oneDrone = swarmState.swarm().get(0);
        assertAll(
                () -> assertDoesNotThrow(() -> d.execute(swarmState, oneDrone)),
                () -> assertTrue(oneDrone.isRadiating("neo_ooo")));
    }

}