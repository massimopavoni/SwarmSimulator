package it.unicam.cs.massimopavoni.swarmsimulator.swarm.core;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.TestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class SwarmPropertiesTest {
    @BeforeAll
    static void setUp() throws IOException {
        Files.deleteIfExists(Path.of(SwarmProperties.DEFAULT_SWARM_FOLDER +
                SwarmProperties.DEFAULT_SWARM_PROPERTIES_FILE_NAME));
    }

    @Test
    void initialize_default() throws IOException {
        Path customFile = Path.of(SwarmProperties.DEFAULT_SWARM_FOLDER +
                SwarmProperties.DEFAULT_SWARM_PROPERTIES_FILE_NAME);
        assertAll(
                () -> assertDoesNotThrow(() -> SwarmProperties.initialize()),
                () -> assertTrue(Files.exists(customFile)),
                () -> assertEquals(Math.pow(10, -9), SwarmProperties.tolerance()),
                () -> assertEquals(8, SwarmProperties.parallelism()),
                () -> assertEquals(256, SwarmProperties.maxPolygonVertices()),
                () -> assertEquals(4, SwarmProperties.maxDomainRegions()),
                () -> assertEquals(Math.pow(2, 16), SwarmProperties.maxDronesNumber()),
                () -> assertEquals("^[A-Za-z\\d_]+$", SwarmProperties.signalPattern().pattern()),
                () -> assertEquals("^[A-Za-z\\d_]+$", SwarmProperties.echoPattern().toString()));
        Files.delete(customFile);
        SwarmProperties.reset();
    }

    @Test
    void initialize_doNotDoTwice() throws IOException {
        String testSwarmFolder = SwarmProperties.DEFAULT_SWARM_FOLDER +
                "test/" + SwarmPropertiesTest.class.getSimpleName() + "/";
        assertAll(
                () -> assertDoesNotThrow(
                        () -> SwarmProperties.initialize(testSwarmFolder, SwarmProperties.DEFAULT_SWARM_PROPERTIES_FILE_NAME)),
                () -> assertEquals(8, SwarmProperties.parallelism()),
                () -> assertDoesNotThrow(
                        () -> SwarmProperties.initialize(testSwarmFolder, SwarmProperties.DEFAULT_SWARM_PROPERTIES_FILE_NAME)),
                () -> assertEquals(8, SwarmProperties.parallelism()));
        TestUtils.deleteProperties(SwarmPropertiesTest.class.getSimpleName());
    }

    @Test
    void initialize_alreadyPresent() {
        String testFileName = SwarmProperties.DEFAULT_SWARM_PROPERTIES_RESOURCE_LOCATION;
        String testSwarmFolder = Objects.requireNonNull(HiveMind.class.getResource(
                        SwarmProperties.DEFAULT_SWARM_PROPERTIES_RESOURCE_LOCATION)).getPath()
                .replace(testFileName, "");
        assertAll(
                () -> assertDoesNotThrow(() -> SwarmProperties.initialize(testSwarmFolder, testFileName)),
                () -> assertEquals(8, SwarmProperties.parallelism()));
        SwarmProperties.reset();
    }

    @Test
    void initialize_illegalSwarmFolder() {
        String testFileName = "/";
        String testSwarmFolder = "/";
        AtomicReference<Throwable> e = new AtomicReference<>();
        assertAll(
                () -> e.set(assertThrowsExactly(HiveMindException.class,
                        () -> SwarmProperties.initialize(testSwarmFolder, testFileName))),
                () -> assertInstanceOf(IOException.class, e.get().getCause()),
                () -> assertTrue(e.get().getMessage().toLowerCase().contains("folder")));
        SwarmProperties.reset();
    }

    @Test
    void initialize_borkedNotJson() {
        String testFileName = "borkedNotJsonSwarmProperties.test";
        String testSwarmFolder = Objects.requireNonNull(HiveMind.class.getResource(
                        SwarmProperties.DEFAULT_SWARM_PROPERTIES_RESOURCE_LOCATION)).getPath()
                .replace(SwarmProperties.DEFAULT_SWARM_PROPERTIES_RESOURCE_LOCATION, "");
        AtomicReference<Throwable> e = new AtomicReference<>();
        assertAll(
                () -> e.set(assertThrowsExactly(HiveMindException.class,
                        () -> SwarmProperties.initialize(testSwarmFolder, testFileName))),
                () -> assertInstanceOf(IOException.class, e.get().getCause()));
        SwarmProperties.reset();
    }

    @Test
    void initialize_borkedNull() {
        String testFileName = "borkedNullSwarmProperties.json";
        String testSwarmFolder = Objects.requireNonNull(HiveMind.class.getResource(
                        SwarmProperties.DEFAULT_SWARM_PROPERTIES_RESOURCE_LOCATION)).getPath()
                .replace(SwarmProperties.DEFAULT_SWARM_PROPERTIES_RESOURCE_LOCATION, "");
        AtomicReference<Throwable> e = new AtomicReference<>();
        assertAll(
                () -> e.set(assertThrowsExactly(HiveMindException.class,
                        () -> SwarmProperties.initialize(testSwarmFolder, testFileName))),
                () -> assertInstanceOf(IllegalArgumentException.class, e.get().getCause()));
        SwarmProperties.reset();
    }

    @ParameterizedTest
    @CsvSource({"0,tolerance", "1,tolerance", "2,parallelism", "3,parallelism", "4,vertices", "5,vertices",
            "6,regions", "7,regions", "8,drones", "9,drones", "10,pattern", "11,pattern", "12,pattern"})
    void initialize_borkedInvalid(String testFileNumber, String containsCheck) {
        String testFileName = String.format("borkedInvalidSwarmProperties%s.json", testFileNumber);
        String testSwarmFolder = Objects.requireNonNull(HiveMind.class.getResource(
                        SwarmProperties.DEFAULT_SWARM_PROPERTIES_RESOURCE_LOCATION)).getPath()
                .replace(SwarmProperties.DEFAULT_SWARM_PROPERTIES_RESOURCE_LOCATION, "");
        AtomicReference<Throwable> e = new AtomicReference<>();
        assertAll(
                () -> e.set(assertThrowsExactly(HiveMindException.class,
                        () -> SwarmProperties.initialize(testSwarmFolder, testFileName))),
                () -> assertInstanceOf(SwarmException.class, e.get().getCause()),
                () -> assertTrue(e.get().getCause().getMessage().toLowerCase().contains(containsCheck)));
        SwarmProperties.reset();
    }
}