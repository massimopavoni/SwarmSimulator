package it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.parser;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.TestUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.HiveMindException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.DomainException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Region;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.ShapeException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.ShapeType;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.SwarmShapeFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class SwarmDomainParserTest {
    DomainParser swarmDomainParser = new SwarmDomainParser(new SwarmShapeFactory());
    AtomicReference<List<Region>> domain = new AtomicReference<>();

    @BeforeAll
    static void setUp() throws HiveMindException {
        TestUtils.initializeProperties(SwarmDomainParserTest.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() throws IOException {
        TestUtils.deleteProperties(SwarmDomainParserTest.class.getSimpleName());
    }

    @Test
    void parseDomain_fromFile() {
        parseDomain_fromSomething(
                () -> domain.set(swarmDomainParser.parseDomain(new File(
                        Objects.requireNonNull(SwarmDomainParserTest.class.getClassLoader().getResource(
                                "it/unicam/cs/massimopavoni/swarmsimulator/swarm/domain/parser/" +
                                        "testDomain.swarm"
                        )).getPath()))));
    }

    @Test
    void parseDomain_fromPathErrorReading() {
        AtomicReference<Exception> e = new AtomicReference<>();
        assertAll(
                () -> e.set(assertThrowsExactly(DomainParserException.class,
                        () -> swarmDomainParser.parseDomain(Path.of("nonexistent")))),
                () -> assertInstanceOf(IOException.class, e.get().getCause()));
    }

    @Test
    void parseDomain_fromPath() {
        parseDomain_fromSomething(
                () -> domain.set(swarmDomainParser.parseDomain(Path.of(
                        Objects.requireNonNull(SwarmDomainParserTest.class.getClassLoader().getResource(
                                "it/unicam/cs/massimopavoni/swarmsimulator/swarm/domain/parser/" +
                                        "testDomain.swarm"
                        )).getPath()))));
    }

    @Test
    void parseDomain_tooManyRegions() {
        AtomicReference<Exception> e = new AtomicReference<>();
        assertAll(
                () -> e.set(assertThrowsExactly(DomainParserException.class,
                        () -> swarmDomainParser.parseDomain("""
                                first_shape
                                second_shape
                                third_shape
                                fourth_shape
                                fifth_shape"""))),
                () -> assertEquals(DomainException.class, e.get().getCause().getClass()));
    }

    @Test
    void parseDomain_notEnoughArguments() {
        AtomicReference<Exception> e = new AtomicReference<>();
        assertAll(
                () -> e.set(assertThrowsExactly(DomainParserException.class,
                        () -> swarmDomainParser.parseDomain("""
                                first_shape"""))),
                () -> assertEquals(DomainException.class, e.get().getCause().getClass()),
                () -> assertTrue(e.get().getMessage().toLowerCase().contains("line 0")));
    }

    @Test
    void parseDomain_unavailableShape() {
        AtomicReference<Exception> e = new AtomicReference<>();
        assertAll(
                () -> e.set(assertThrowsExactly(DomainParserException.class,
                        () -> swarmDomainParser.parseDomain("""
                                unavailable_shape ImPoSsIbLeShApE 1 2 3 4"""))),
                () -> assertEquals(ShapeException.class, e.get().getCause().getClass()));
    }

    @Test
    void parseDomain_invalidSignal() {
        AtomicReference<Exception> e = new AtomicReference<>();
        assertAll(
                () -> e.set(assertThrowsExactly(DomainParserException.class,
                        () -> swarmDomainParser.parseDomain("""
                                #1_shape CIRCLE 1 2 3"""))),
                () -> assertEquals(DomainException.class, e.get().getCause().getClass()));
    }

    @Test
    void parseDomain_nonDoubleArguments() {
        AtomicReference<Exception> e = new AtomicReference<>();
        assertAll(
                () -> e.set(assertThrowsExactly(DomainParserException.class,
                        () -> swarmDomainParser.parseDomain("""
                                first_shape Circle 1 2 t"""))),
                () -> assertInstanceOf(IllegalArgumentException.class, e.get().getCause()));
    }

    @Test
    void parseDomain_invalidShapeArguments() {
        AtomicReference<Exception> e = new AtomicReference<>();
        assertAll(
                () -> e.set(assertThrowsExactly(DomainParserException.class,
                        () -> swarmDomainParser.parseDomain("""
                                first_shape CIRCLE 1 2 3 4"""))),
                () -> assertEquals(ShapeException.class, e.get().getCause().getClass()));
    }

    @Test
    void parseDomain_empty() {
        assertAll(
                () -> assertDoesNotThrow(() -> domain.set(swarmDomainParser.parseDomain(""))),
                () -> assertEquals(0, domain.get().size()));
    }

    @Test
    void parseDomain_fromString() {
        parseDomain_fromSomething(
                () -> domain.set(swarmDomainParser.parseDomain("""
                        first_shape CIRCLE 1 2 3
                        second_shape ELLIPSE 4 5 6 7
                        third_shape POLYGON 8 9 10 11 12 13 14 15 16 17
                        fourth_shape RECTANGLE 18 19 20 21""")));
    }

    void parseDomain_fromSomething(Executable something) {
        assertAll(
                () -> assertDoesNotThrow(something),
                () -> assertEquals(4, domain.get().size()),
                () -> assertEquals(
                        List.of("first_shape", "second_shape", "third_shape", "fourth_shape"),
                        domain.get().stream().map(Region::signal).toList()),
                () -> assertEquals(
                        List.of(ShapeType.CIRCLE, ShapeType.ELLIPSE, ShapeType.POLYGON, ShapeType.RECTANGLE),
                        domain.get().stream().map(Region::shapeType).toList()),
                () -> assertArrayEquals(
                        new double[]{1, 2, 3},
                        domain.get().get(0).shape().getProperties()));
    }
}