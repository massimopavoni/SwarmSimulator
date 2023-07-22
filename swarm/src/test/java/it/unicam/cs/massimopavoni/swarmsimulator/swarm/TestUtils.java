package it.unicam.cs.massimopavoni.swarmsimulator.swarm;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.HiveMindException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class TestUtils {
    public static void initializeProperties(String testName) throws HiveMindException {
        SwarmProperties.initialize(SwarmProperties.DEFAULT_SWARM_FOLDER + "test/" + testName + "/");
    }

    public static void deleteProperties(String testName) throws IOException {
        String testSwarmFolder = SwarmProperties.DEFAULT_SWARM_FOLDER + "test/";
        Files.deleteIfExists(Path.of(testSwarmFolder + testName + "/" +
                SwarmProperties.CUSTOM_SWARM_PROPERTIES_FILE_NAME));
        Files.deleteIfExists(Path.of(testSwarmFolder + testName + "/"));
        Files.deleteIfExists(Path.of(testSwarmFolder));
    }
}
