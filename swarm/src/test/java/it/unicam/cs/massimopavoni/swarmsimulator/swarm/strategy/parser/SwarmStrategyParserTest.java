package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.parser;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.TestUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.HiveMindException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.StrategyException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives.*;
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

class SwarmStrategyParserTest {
    SwarmStrategyParser swarmStrategyParser = new SwarmStrategyParser(new SwarmDirectiveFactory());
    AtomicReference<List<Directive>> strategy = new AtomicReference<>();

    @BeforeAll
    static void setUp() throws HiveMindException {
        TestUtils.initializeProperties(SwarmStrategyParserTest.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() throws IOException {
        TestUtils.deleteProperties(SwarmStrategyParserTest.class.getSimpleName());
    }

    @Test
    void parseStrategy_fromFile() {
        parseStrategy_fromSomething(
                () -> strategy.set(swarmStrategyParser.parseStrategy(new File(
                        Objects.requireNonNull(SwarmStrategyParserTest.class.getClassLoader().getResource(
                                "it/unicam/cs/massimopavoni/swarmsimulator/swarm/strategy/parser/" +
                                        "testStrategy.swarm"
                        )).getPath()))));
    }

    @Test
    void parseStrategy_fromPathErrorReading() {
        AtomicReference<Exception> e = new AtomicReference<>();
        assertAll(
                () -> e.set(assertThrowsExactly(StrategyParserException.class,
                        () -> swarmStrategyParser.parseStrategy(Path.of("nonexistent")))),
                () -> assertInstanceOf(IOException.class, e.get().getCause()));
    }

    @Test
    void parseStrategy_fromPath() {
        parseStrategy_fromSomething(
                () -> strategy.set(swarmStrategyParser.parseStrategy(Path.of(
                        Objects.requireNonNull(SwarmStrategyParserTest.class.getClassLoader().getResource(
                                "it/unicam/cs/massimopavoni/swarmsimulator/swarm/strategy/parser/" +
                                        "testStrategy.swarm"
                        )).getPath()))));
    }

    @Test
    void parseStrategy_notEnoughArguments() {
        AtomicReference<Exception> e = new AtomicReference<>();
        assertAll(
                () -> e.set(assertThrowsExactly(StrategyParserException.class,
                        () -> swarmStrategyParser.parseStrategy("""
                                                                
                                DONE
                                STOP"""))),
                () -> assertTrue(e.get().getMessage().toLowerCase().contains("line 0")),
                () -> assertEquals(DirectiveException.class, e.get().getCause().getClass()),
                () -> assertTrue(e.get().getCause().getMessage().toLowerCase().contains("argument")));
    }

    @Test
    void parseStrategy_inconsistentLoops() {
        AtomicReference<Exception> e = new AtomicReference<>();
        assertAll(
                () -> e.set(assertThrowsExactly(StrategyParserException.class,
                        () -> swarmStrategyParser.parseStrategy("""
                                REPEAT 64
                                UNTIL some_signal_lol
                                FOLLOW umberto 100 5
                                CONTINUE 4
                                DONE
                                STOP"""))),
                () -> assertTrue(e.get().getMessage().toLowerCase().contains("line 5")),
                () -> assertEquals(StrategyException.class, e.get().getCause().getClass()),
                () -> assertTrue(e.get().getCause().getMessage().toLowerCase().contains("loop")));
    }

    @Test
    void parseStrategy_missingStop() {
        AtomicReference<Exception> e = new AtomicReference<>();
        assertAll(
                () -> e.set(assertThrowsExactly(StrategyParserException.class,
                        () -> swarmStrategyParser.parseStrategy("""
                                DO FOREVER
                                SIGNAL pop
                                UNSIGNAL pop
                                DONE"""))),
                () -> assertTrue(e.get().getMessage().toLowerCase().contains("line 3")),
                () -> assertEquals(StrategyException.class, e.get().getCause().getClass()),
                () -> assertTrue(e.get().getCause().getMessage().toLowerCase().contains("stop")));
    }

    @Test
    void parseStrategy_unexpectedDone() {
        AtomicReference<Exception> e = new AtomicReference<>();
        assertAll(
                () -> e.set(assertThrowsExactly(StrategyParserException.class,
                        () -> swarmStrategyParser.parseStrategy("""
                                SIGNAL start
                                DO FOREVER
                                MOVE 2 2 5
                                DONE
                                DONE
                                STOP"""))),
                () -> assertTrue(e.get().getMessage().toLowerCase().contains("line 4")),
                () -> assertEquals(StrategyException.class, e.get().getCause().getClass()),
                () -> assertTrue(e.get().getCause().getMessage().toLowerCase().contains("loop")));
    }

    @Test
    void parseStrategy_illegalArgument() {
        AtomicReference<Exception> e = new AtomicReference<>();
        assertAll(
                () -> e.set(assertThrowsExactly(StrategyParserException.class,
                        () -> swarmStrategyParser.parseStrategy("""
                                SIGNAL eheh
                                REPEAT a
                                DONE
                                STOP"""))),
                () -> assertTrue(e.get().getMessage().toLowerCase().contains("line 2")),
                () -> assertInstanceOf(IllegalArgumentException.class, e.get().getCause()));
    }

    @Test
    void parseStrategy_fromString() {
        parseStrategy_fromSomething(
                () -> strategy.set(swarmStrategyParser.parseStrategy("""
                        UNTIL first_shape
                        FOLLOW in_first_shape 100 5
                        CONTINUE 10
                        DONE
                        SIGNAL in_first_shape
                        STOP""")));
    }

    @Test
    void parseStrategy_nestedLoops() {
        assertAll(
                () -> assertDoesNotThrow(
                        () -> strategy.set(swarmStrategyParser.parseStrategy("""
                                SIGNAL go
                                DO FOREVER
                                REPEAT 7
                                UNTIL some_loco
                                MOVE RANDOM -10 -10 10 10 666
                                CONTINUE 9
                                DONE
                                MOVE 0 0 10
                                CONTINUE 60
                                DONE
                                DONE
                                UNSIGNAL go
                                STOP"""))),
                () -> assertEquals(
                        List.of(Radiate.class, Repeat.class, Repeat.class, Until.class,
                                Move.class, Continue.class, Done.class, Move.class,
                                Continue.class, Done.class, Done.class, Absorb.class, Stop.class),
                        strategy.get().stream().map(Directive::getClass).toList()),
                () -> assertEquals(13, strategy.get().size()),
                () -> assertEquals(11, ((Repeat) strategy.get().get(1)).jumpIndex()),
                () -> assertEquals(1, ((Done) strategy.get().get(10)).jumpIndex()),
                () -> assertEquals(10, ((Repeat) strategy.get().get(2)).jumpIndex()),
                () -> assertEquals(2, ((Done) strategy.get().get(9)).jumpIndex()),
                () -> assertEquals(7, ((Until) strategy.get().get(3)).jumpIndex()),
                () -> assertEquals(3, ((Done) strategy.get().get(6)).jumpIndex()));
    }

    @Test
    void parseStrategy_nestedMoreLoops() {
        assertAll(
                () -> assertDoesNotThrow(
                        () -> strategy.set(swarmStrategyParser.parseStrategy("""
                                SIGNAL go
                                DO FOREVER
                                REPEAT 7
                                UNTIL some_loco
                                MOVE RANDOM -10 -10 10 10 666
                                CONTINUE 9
                                DONE
                                MOVE 0 0 10
                                CONTINUE 60
                                DONE
                                REPEAT 256
                                MOVE RANDOM -1 1 1 1 20
                                CONTINUE 7
                                DONE
                                DONE
                                UNSIGNAL go
                                STOP"""))),
                () -> assertEquals(
                        List.of(Radiate.class, Repeat.class, Repeat.class, Until.class, Move.class,
                                Continue.class, Done.class, Move.class, Continue.class,
                                Done.class, Repeat.class, Move.class, Continue.class,
                                Done.class, Done.class, Absorb.class, Stop.class),
                        strategy.get().stream().map(Directive::getClass).toList()),
                () -> assertEquals(17, strategy.get().size()),
                () -> assertEquals(15, ((Repeat) strategy.get().get(1)).jumpIndex()),
                () -> assertEquals(1, ((Done) strategy.get().get(14)).jumpIndex()),
                () -> assertEquals(10, ((Repeat) strategy.get().get(2)).jumpIndex()),
                () -> assertEquals(2, ((Done) strategy.get().get(9)).jumpIndex()),
                () -> assertEquals(7, ((Until) strategy.get().get(3)).jumpIndex()),
                () -> assertEquals(3, ((Done) strategy.get().get(6)).jumpIndex()),
                () -> assertEquals(14, ((Repeat) strategy.get().get(10)).jumpIndex()),
                () -> assertEquals(10, ((Done) strategy.get().get(13)).jumpIndex()));
    }

    void parseStrategy_fromSomething(Executable something) {
        assertAll(
                () -> assertDoesNotThrow(something),
                () -> assertEquals(6, strategy.get().size()),
                () -> assertEquals(
                        List.of(Until.class, Follow.class, Continue.class, Done.class, Radiate.class, Stop.class),
                        strategy.get().stream().map(Directive::getClass).toList()),
                () -> assertEquals(4, ((Until) strategy.get().get(0)).jumpIndex()),
                () -> assertEquals("first_shape", ((Until) strategy.get().get(0)).signal()),
                () -> assertEquals("in_first_shape", ((Follow) strategy.get().get(1)).echo()),
                () -> assertEquals(100, ((Follow) strategy.get().get(1)).range()),
                () -> assertEquals(5, ((Follow) strategy.get().get(1)).speed()),
                () -> assertEquals(10, ((Continue) strategy.get().get(2)).jumpLimit()),
                () -> assertEquals(0, ((Done) strategy.get().get(3)).jumpIndex()),
                () -> assertEquals("in_first_shape", ((Radiate) strategy.get().get(4)).echo()));
    }
}