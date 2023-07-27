package it.unicam.cs.massimopavoni.swarmsimulator.swarm;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.HiveMindException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SwarmUtilsTest {
    @BeforeAll
    static void setUp() throws HiveMindException {
        TestUtils.initializeProperties(SwarmUtilsTest.class.getSimpleName());
    }

    @AfterAll
    static void tearDown() throws IOException {
        TestUtils.deleteProperties(SwarmUtilsTest.class.getSimpleName());
    }

    @Test
    void compare_combinations() {
        assertAll(
                () -> assertEquals(0, SwarmUtils.compare(0, 0.000000000001)),
                () -> assertEquals(-1, SwarmUtils.compare(0, 0.000000000002)),
                () -> assertEquals(1, SwarmUtils.compare(0, -0.000000000002)));
    }

    @Test
    void isPositive_combinations() {
        assertAll(
                () -> assertFalse(SwarmUtils.isPositive(Double.NaN)),
                () -> assertFalse(SwarmUtils.isPositive(Double.NEGATIVE_INFINITY)),
                () -> assertFalse(SwarmUtils.isPositive(Double.POSITIVE_INFINITY)),
                () -> assertFalse(SwarmUtils.isPositive(-1)),
                () -> assertFalse(SwarmUtils.isPositive(0)),
                () -> assertTrue(SwarmUtils.isPositive(1)));
    }

    @Test
    void toDoubleArray_formatException() {
        assertThrowsExactly(NumberFormatException.class,
                () -> SwarmUtils.toDoubleArray(new String[]{"a", "b", "c"}, 0));
    }

    @Test
    void toDoubleArray_correct() {
        assertArrayEquals(new double[]{2, 3}, SwarmUtils.toDoubleArray(new String[]{"1", "2", "3"}, 1));
    }

    @Test
    void checkSignal_throw() {
        assertThrowsExactly(SwarmException.class,
                () -> SwarmUtils.checkSignal("ah ah", new SwarmException("Takeeeee oooooon meeeeee")));
    }

    @Test
    void checkSignal_goodCheck() {
        assertDoesNotThrow(() -> SwarmUtils.checkSignal("click", new SwarmException("Noice")));
    }

    @Test
    void checkEcho_throw() {
        assertThrowsExactly(SwarmException.class,
                () -> SwarmUtils.checkEcho("magnus-nepo", new SwarmException("Sads")));
    }

    @Test
    void checkEcho_goodCheck() {
        assertDoesNotThrow(() -> SwarmUtils.checkEcho("google_en_passant", new SwarmException("r/anarchychess")));
    }
}