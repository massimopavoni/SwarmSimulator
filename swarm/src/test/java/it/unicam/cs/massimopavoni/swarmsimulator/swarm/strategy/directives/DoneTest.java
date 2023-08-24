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

import static org.junit.jupiter.api.Assertions.*;

class DoneTest {
    @BeforeAll
    static void setUp() throws HiveMindException {
        TestUtils.initializeProperties(DoneTest.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() throws IOException {
        TestUtils.deleteProperties(DoneTest.class.getSimpleName());
    }

    @Test
    void Done() {
        AtomicReference<Done> d = new AtomicReference<>();
        assertAll(
                () -> assertDoesNotThrow(() -> d.set(new Done(1))),
                () -> assertEquals(1, d.get().jumpIndex()));
    }

    @Test
    void execute_done() throws DomainParserException, StrategyParserException {
        ShapeFactory shapeFactory = new SwarmShapeFactory();
        SwarmState.initializeParsers(shapeFactory, null);
        SwarmState swarmState = new SwarmState(new File(
                Objects.requireNonNull(DoneTest.class.getClassLoader().getResource(
                        "it/unicam/cs/massimopavoni/swarmsimulator/swarm/domain/parser/" +
                                "testDomain.swarm")).getPath()),
                new File(Objects.requireNonNull(DoneTest.class.getClassLoader().getResource(
                        "it/unicam/cs/massimopavoni/swarmsimulator/swarm/strategy/parser/" +
                                "testStrategy.swarm")).getPath()),
                256, shapeFactory.createShape(
                ShapeType.CIRCLE, new double[]{0, 0, 20}), true);
        Done d = new Done(1);
        Drone oneDrone = swarmState.swarm().get(0);
        oneDrone.setCurrentDirective(13);
        assertAll(
                () -> assertDoesNotThrow(() -> d.execute(swarmState, oneDrone)),
                () -> assertEquals(1, oneDrone.currentDirective()));
    }
}