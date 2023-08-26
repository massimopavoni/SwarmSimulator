package it.unicam.cs.massimopavoni.swarmsimulator.swarm.core;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.Drone;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.TestUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Position;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.parser.DomainParserException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.Circle;
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

class HiveMindTest {
    @BeforeAll
    static void setUp() throws HiveMindException {
        TestUtils.initializeProperties(HiveMindTest.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() throws IOException {
        TestUtils.deleteProperties(HiveMindTest.class.getSimpleName());
    }

    @Test
    void HiveMind_correct() {
        AtomicReference<HiveMind> h = new AtomicReference<>();
        assertAll(
                () -> assertDoesNotThrow(() -> h.set(getHiveMind(256))),
                () -> assertEquals(1, h.get().state().domain().size()),
                () -> assertEquals(6, h.get().state().strategy().size()),
                () -> assertEquals(256, h.get().state().swarm().size()),
                () -> assertEquals(0, h.get().stepCount()));
    }

    @Test
    void swarmStep_justOne() throws DomainParserException, StrategyParserException, HiveMindException {
        Circle circle = new Circle(new Position(0, 0), 20);
        HiveMind hm = getHiveMind(256);
        assertAll(
                () -> assertDoesNotThrow(hm::swarmStep),
                () -> assertEquals(1, hm.stepCount()),
                () -> assertTrue(SwarmUtils.parallelize(() -> hm.state().swarm()
                        .parallelStream()
                        .allMatch(d -> SwarmUtils.compare(d.position()
                                .distanceTo(circle.getCenter()), circle.getRadius().x()) == 0
                                && d.jumpCounter(0) == 1
                                && d.currentDirective() == 1))));
    }

    @Test
    void swarmStep_multiple() throws DomainParserException, StrategyParserException, HiveMindException {
        Position np = new Position(0, 0);
        Circle circle = new Circle(new Position(0, 0), 20);
        HiveMind hm = getHiveMind(256);
        assertAll(
                () -> assertDoesNotThrow(() -> IntStream.range(0, 5).forEach(i -> hm.swarmStep())),
                () -> assertEquals(5, hm.stepCount()),
                () -> assertTrue(SwarmUtils.parallelize(() -> hm.state().swarm()
                        .parallelStream()
                        .allMatch(d -> !d.direction().equalTo(np) && d.speed() != 0 && SwarmUtils.compare(d.position()
                                .distanceTo(circle.getCenter()), circle.getRadius().x()) != 0
                                && d.jumpCounter(0) == 2 && d.currentDirective() == 2))));
    }

    @Test
    void swarmStep_lotsUntilOneDead() throws DomainParserException, StrategyParserException, HiveMindException {
        Circle circle = new Circle(new Position(1, 2), 3);
        HiveMind hm = getHiveMind(256);
        assertAll(
                () -> assertDoesNotThrow(() -> IntStream.range(0, 100).forEach(i -> hm.swarmStep())),
                () -> assertEquals(100, hm.stepCount()),
                () -> assertTrue(SwarmUtils.parallelize(() -> hm.state().swarm()
                        .parallelStream()
                        .anyMatch(d -> d.isRadiating("in_shape") && !d.isAlive()))),
                () -> assertTrue(circle.contains(Position.averageOf(SwarmUtils.parallelize(() -> hm.state().swarm()
                        .parallelStream()
                        .filter(d -> d.isRadiating("in_shape"))
                        .map(Drone::position).toList())))));
    }

    @Test
    void swarmStep_lotsUntilAllDead() throws DomainParserException, StrategyParserException, HiveMindException {
        Circle circle = new Circle(new Position(1, 2), 3);
        HiveMind hm = getHiveMind(512);
        assertAll(
                () -> assertDoesNotThrow(() -> IntStream.range(0, 4000).forEach(i -> hm.swarmStep())),
                () -> assertEquals(4000, hm.stepCount()),
                () -> assertFalse(hm.isSwarmAlive()),
                () -> assertTrue(hm.swarmLifeDuration() <= 1000),
                () -> assertTrue(SwarmUtils.parallelize(() -> hm.state().swarm()
                        .parallelStream()
                        .allMatch(d -> circle.contains(d.position()) && d.isRadiating("in_shape")
                                && !d.isAlive()))),
                () -> assertTrue(circle.contains(Position.averageOf(SwarmUtils.parallelize(() -> hm.state().swarm()
                        .parallelStream()
                        .filter(d -> d.isRadiating("in_shape"))
                        .map(Drone::position).toList())))));
    }

    HiveMind getHiveMind(int dronesNumber) throws DomainParserException, StrategyParserException, HiveMindException {
        ShapeFactory shapeFactory = new SwarmShapeFactory();
        SwarmState.initializeParsers(shapeFactory, null);
        return new HiveMind(new SwarmState(new File(
                Objects.requireNonNull(HiveMindTest.class.getClassLoader().getResource(
                        "it/unicam/cs/massimopavoni/swarmsimulator/swarm/core/" +
                                "testDomain.swarm")).getPath()),
                new File(Objects.requireNonNull(HiveMindTest.class.getClassLoader().getResource(
                        "it/unicam/cs/massimopavoni/swarmsimulator/swarm/core/" +
                                "testStrategy.swarm")).getPath()),
                dronesNumber,
                shapeFactory.createShape(ShapeType.CIRCLE, new double[]{0, 0, 20}), true));
    }
}