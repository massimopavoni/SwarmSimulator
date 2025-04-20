package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.TestUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.HiveMindException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class SwarmDirectiveFactoryTest {
    final DirectiveFactory swarmDirectiveFactory = new SwarmDirectiveFactory();
    final AtomicReference<Directive> directive = new AtomicReference<>();

    @BeforeAll
    static void setUp() throws HiveMindException {
        TestUtils.initializeProperties(SwarmDirectiveFactoryTest.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() throws IOException {
        TestUtils.deleteProperties(SwarmDirectiveFactoryTest.class.getSimpleName());
    }

    @Test
    void createDirective_correctDirectiveContinue() {
        assertAll(
                () -> assertDoesNotThrow(
                        () -> directive.set(
                                swarmDirectiveFactory.createDirective(ParserDirective.CONTINUE, new String[]{"8"}))),
                () -> assertEquals(DirectiveType.JUMP, directive.get().getType()),
                () -> assertEquals(ParserDirective.CONTINUE.getDirectiveClass(), directive.get().getClass()));
    }

    @Test
    void createDirective_correctDirectiveDoForever() {
        assertAll(
                () -> assertDoesNotThrow(
                        () -> directive.set(
                                swarmDirectiveFactory.createDirective(ParserDirective.DOFOREVER,
                                        new String[]{"10", "FoReVeR"}))),
                () -> assertEquals(DirectiveType.JUMP, directive.get().getType()),
                () -> assertEquals(ParserDirective.DOFOREVER.getDirectiveClass(), directive.get().getClass()));
    }

    @Test
    void createDirective_correctDirectiveDone() {
        assertAll(
                () -> assertDoesNotThrow(
                        () -> directive.set(
                                swarmDirectiveFactory.createDirective(ParserDirective.DONE, new String[]{"0"}))),
                () -> assertEquals(DirectiveType.JUMP, directive.get().getType()),
                () -> assertEquals(ParserDirective.DONE.getDirectiveClass(), directive.get().getClass()));
    }

    @Test
    void createDirective_correctDirectiveFollow() {
        assertAll(
                () -> assertDoesNotThrow(
                        () -> directive.set(
                                swarmDirectiveFactory.createDirective(ParserDirective.FOLLOW,
                                        new String[]{"echo", "100", "5"}))),
                () -> assertEquals(DirectiveType.MOVEMENT, directive.get().getType()),
                () -> assertEquals(ParserDirective.FOLLOW.getDirectiveClass(), directive.get().getClass()));
    }

    @Test
    void createDirective_correctDirectiveMoveRandom() {
        assertAll(
                () -> assertDoesNotThrow(
                        () -> directive.set(
                                swarmDirectiveFactory.createDirective(ParserDirective.MOVE,
                                        new String[]{"RANDOM", "1", "2", "3", "4", "5"}))),
                () -> assertEquals(DirectiveType.MOVEMENT, directive.get().getType()),
                () -> assertEquals(ParserDirective.MOVE.getDirectiveClass(), directive.get().getClass()));
    }

    @Test
    void createDirective_correctDirectiveMove() {
        assertAll(
                () -> assertDoesNotThrow(
                        () -> directive.set(
                                swarmDirectiveFactory.createDirective(ParserDirective.MOVE,
                                        new String[]{"1", "2", "5"}))),
                () -> assertEquals(DirectiveType.MOVEMENT, directive.get().getType()),
                () -> assertEquals(ParserDirective.MOVE.getDirectiveClass(), directive.get().getClass()));
    }

    @Test
    void createDirective_correctDirectiveRepeat() {
        assertAll(
                () -> assertDoesNotThrow(
                        () -> directive.set(
                                swarmDirectiveFactory.createDirective(ParserDirective.REPEAT,
                                        new String[]{"10", "8"}))),
                () -> assertEquals(DirectiveType.JUMP, directive.get().getType()),
                () -> assertEquals(ParserDirective.REPEAT.getDirectiveClass(), directive.get().getClass()));
    }

    @Test
    void createDirective_correctDirectiveSignal() {
        assertAll(
                () -> assertDoesNotThrow(
                        () -> directive.set(
                                swarmDirectiveFactory.createDirective(ParserDirective.SIGNAL, new String[]{"echo"}))),
                () -> assertEquals(DirectiveType.ECHO, directive.get().getType()),
                () -> assertEquals(ParserDirective.SIGNAL.getDirectiveClass(), directive.get().getClass()));
    }

    @Test
    void createDirective_correctDirectiveStop() {
        assertAll(
                () -> assertDoesNotThrow(
                        () -> directive.set(
                                swarmDirectiveFactory.createDirective(ParserDirective.STOP, new String[]{}))),
                () -> assertEquals(DirectiveType.MOVEMENT, directive.get().getType()),
                () -> assertEquals(ParserDirective.STOP.getDirectiveClass(), directive.get().getClass()));
    }

    @Test
    void createDirective_correctDirectiveUnsignal() {
        assertAll(
                () -> assertDoesNotThrow(
                        () -> directive.set(
                                swarmDirectiveFactory.createDirective(ParserDirective.UNSIGNAL, new String[]{"echo"}))),
                () -> assertEquals(DirectiveType.ECHO, directive.get().getType()),
                () -> assertEquals(ParserDirective.UNSIGNAL.getDirectiveClass(), directive.get().getClass()));
    }

    @Test
    void createDirective_correctDirectiveUntil() {
        assertAll(
                () -> assertDoesNotThrow(
                        () -> directive.set(
                                swarmDirectiveFactory.createDirective(ParserDirective.UNTIL,
                                        new String[]{"10", "signal"}))),
                () -> assertEquals(DirectiveType.JUMP, directive.get().getType()),
                () -> assertEquals(ParserDirective.UNTIL.getDirectiveClass(), directive.get().getClass()));
    }

    @Test
    void createContinue_invalidArguments() {
        assertAll(
                () -> assertThrowsExactly(DirectiveException.class,
                        () -> swarmDirectiveFactory.createContinue(new String[]{})),
                () -> assertThrowsExactly(DirectiveException.class,
                        () -> swarmDirectiveFactory.createContinue(new String[]{"8", "9"})),
                () -> assertThrowsExactly(NumberFormatException.class,
                        () -> swarmDirectiveFactory.createContinue(new String[]{"a"})));
    }

    @Test
    void createContinue_validArguments() {
        assertDoesNotThrow(() -> swarmDirectiveFactory.createContinue(new String[]{"8"}));
    }

    @Test
    void createDoForever_invalidArguments() {
        assertAll(
                () -> assertThrowsExactly(DirectiveException.class,
                        () -> swarmDirectiveFactory.createDoForever(new String[]{"10"})),
                () -> assertThrowsExactly(DirectiveException.class,
                        () -> swarmDirectiveFactory.createDoForever(new String[]{"10", "FoReVeR", "11"})),
                () -> assertThrowsExactly(NumberFormatException.class,
                        () -> swarmDirectiveFactory.createDoForever(new String[]{"a", "FoReVeR"})));
    }

    @Test
    void createDoForever_validArguments() {
        assertDoesNotThrow(() -> swarmDirectiveFactory.createDoForever(new String[]{"10", "FoReVeR"}));
    }

    @Test
    void createDone_invalidArguments() {
        assertAll(
                () -> assertThrowsExactly(DirectiveException.class,
                        () -> swarmDirectiveFactory.createDone(new String[]{})),
                () -> assertThrowsExactly(DirectiveException.class,
                        () -> swarmDirectiveFactory.createDone(new String[]{"0", "1"})),
                () -> assertThrowsExactly(NumberFormatException.class,
                        () -> swarmDirectiveFactory.createDone(new String[]{"a"})));
    }

    @Test
    void createDone_validArguments() {
        assertDoesNotThrow(() -> swarmDirectiveFactory.createDone(new String[]{"0"}));
    }

    @Test
    void createFollow_invalidArguments() {
        assertAll(
                () -> assertThrowsExactly(DirectiveException.class,
                        () -> swarmDirectiveFactory.createFollow(new String[]{"echo", "100"})),
                () -> assertThrowsExactly(DirectiveException.class,
                        () -> swarmDirectiveFactory.createFollow(new String[]{"echo", "100", "5", "6"})),
                () -> assertThrowsExactly(NumberFormatException.class,
                        () -> swarmDirectiveFactory.createFollow(new String[]{"echo", "a", "5"})),
                () -> assertThrowsExactly(NumberFormatException.class,
                        () -> swarmDirectiveFactory.createFollow(new String[]{"echo", "100", "a"})));
    }

    @Test
    void createFollow_validArguments() {
        assertDoesNotThrow(() -> swarmDirectiveFactory.createFollow(new String[]{"echo", "100", "5"}));
    }

    @Test
    void createMoveRandom_invalidArguments() {
        assertAll(
                () -> assertThrowsExactly(DirectiveException.class,
                        () -> swarmDirectiveFactory.createMove(new String[]{"RANDOM", "1", "2", "3", "4"})),
                () -> assertThrowsExactly(DirectiveException.class,
                        () -> swarmDirectiveFactory.createMove(new String[]{"RANDOM", "1", "2", "3", "4", "5", "6"})),
                () -> assertThrowsExactly(NumberFormatException.class,
                        () -> swarmDirectiveFactory.createMove(new String[]{"RANDOM", "a", "2", "3", "4", "5"})),
                () -> assertThrowsExactly(NumberFormatException.class,
                        () -> swarmDirectiveFactory.createMove(new String[]{"RANDOM", "1", "a", "3", "4", "5"})),
                () -> assertThrowsExactly(NumberFormatException.class,
                        () -> swarmDirectiveFactory.createMove(new String[]{"RANDOM", "1", "2", "a", "4", "5"})),
                () -> assertThrowsExactly(NumberFormatException.class,
                        () -> swarmDirectiveFactory.createMove(new String[]{"RANDOM", "1", "2", "3", "a", "5"})),
                () -> assertThrowsExactly(NumberFormatException.class,
                        () -> swarmDirectiveFactory.createMove(new String[]{"RANDOM", "1", "2", "3", "4", "a"})));
    }

    @Test
    void createMoveRandom_validArguments() {
        assertDoesNotThrow(() -> swarmDirectiveFactory.createMove(new String[]{"RANDOM", "1", "2", "3", "4", "5"}));
    }

    @Test
    void createMove_invalidArguments() {
        assertAll(
                () -> assertThrowsExactly(DirectiveException.class,
                        () -> swarmDirectiveFactory.createMove(new String[]{"1", "2"})),
                () -> assertThrowsExactly(DirectiveException.class,
                        () -> swarmDirectiveFactory.createMove(new String[]{"1", "2", "5", "6"})),
                () -> assertThrowsExactly(NumberFormatException.class,
                        () -> swarmDirectiveFactory.createMove(new String[]{"a", "2", "5"})),
                () -> assertThrowsExactly(NumberFormatException.class,
                        () -> swarmDirectiveFactory.createMove(new String[]{"1", "a", "5"})),
                () -> assertThrowsExactly(NumberFormatException.class,
                        () -> swarmDirectiveFactory.createMove(new String[]{"1", "2", "a"})));
    }

    @Test
    void createMove_validArguments() {
        assertDoesNotThrow(() -> swarmDirectiveFactory.createMove(new String[]{"1", "2", "5"}));
    }

    @Test
    void createRepeat_invalidArguments() {
        assertAll(
                () -> assertThrowsExactly(DirectiveException.class,
                        () -> swarmDirectiveFactory.createRepeat(new String[]{"10"})),
                () -> assertThrowsExactly(DirectiveException.class,
                        () -> swarmDirectiveFactory.createRepeat(new String[]{"10", "8", "9"})),
                () -> assertThrowsExactly(NumberFormatException.class,
                        () -> swarmDirectiveFactory.createRepeat(new String[]{"a", "8"})),
                () -> assertThrowsExactly(NumberFormatException.class,
                        () -> swarmDirectiveFactory.createRepeat(new String[]{"10", "a"})));
    }

    @Test
    void createRepeat_validArguments() {
        assertDoesNotThrow(() -> swarmDirectiveFactory.createRepeat(new String[]{"10", "8"}));
    }

    @Test
    void createSignal_invalidArguments() {
        assertAll(
                () -> assertThrowsExactly(DirectiveException.class,
                        () -> swarmDirectiveFactory.createSignal(new String[]{})),
                () -> assertThrowsExactly(DirectiveException.class,
                        () -> swarmDirectiveFactory.createSignal(new String[]{"echo", "another_echo"})));
    }

    @Test
    void createSignal_validArguments() {
        assertDoesNotThrow(() -> swarmDirectiveFactory.createSignal(new String[]{"echo"}));
    }

    @Test
    void createStop_invalidArguments() {
        assertThrowsExactly(DirectiveException.class,
                () -> swarmDirectiveFactory.createStop(new String[]{"a"}));
    }

    @Test
    void createStop_validArguments() {
        assertDoesNotThrow(() -> swarmDirectiveFactory.createStop(new String[]{}));
    }

    @Test
    void createUnsignal_invalidArguments() {
        assertAll(
                () -> assertThrowsExactly(DirectiveException.class,
                        () -> swarmDirectiveFactory.createUnsignal(new String[]{})),
                () -> assertThrowsExactly(DirectiveException.class,
                        () -> swarmDirectiveFactory.createUnsignal(new String[]{"echo", "another_echo"})));
    }

    @Test
    void createUnsignal_validArguments() {
        assertDoesNotThrow(() -> swarmDirectiveFactory.createUnsignal(new String[]{"echo"}));
    }

    @Test
    void createUntil_invalidArguments() {
        assertAll(
                () -> assertThrowsExactly(DirectiveException.class,
                        () -> swarmDirectiveFactory.createUntil(new String[]{"10"})),
                () -> assertThrowsExactly(DirectiveException.class,
                        () -> swarmDirectiveFactory.createUntil(new String[]{"10", "signal", "another_signal"})),
                () -> assertThrowsExactly(NumberFormatException.class,
                        () -> swarmDirectiveFactory.createUntil(new String[]{"a", "signal"})));
    }

    @Test
    void createUntil_validArguments() {
        assertDoesNotThrow(() -> swarmDirectiveFactory.createUntil(new String[]{"10", "signal"}));
    }
}