package it.unicam.cs.massimopavoni.swarmsimulator.swarm.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

/**
 * Static class defining the swarm properties.
 */
public final class SwarmProperties {
    /**
     * Swarm folder location within the user's home directory.
     */
    private static final String SWARM_FOLDER =
            String.format("%s/.swarm/", System.getProperty("user.home"));
    /**
     * Custom swarm properties file name for user defined properties.
     */
    private static final String CUSTOM_SWARM_PROPERTIES_FILE_NAME = "swarmProperties.json";
    /**
     * Default swarm properties resource location if no custom file is found.
     */
    private static final String DEFAULT_SWARM_PROPERTIES_RESOURCE_LOCATION = "defaultSwarmProperties.json";
    /**
     * Tolerance for double comparisons.
     */
    private static double tolerance;
    /**
     * Maximum number of vertices for a polygon.
     */
    private static int maxPolygonVertices;
    /**
     * Pattern for signal names.
     */
    private static Pattern signalPattern;
    /**
     * Pattern for echo names.
     */
    private static Pattern echoPattern;

    /**
     * Static class constructor, private to prevent instantiation.
     *
     * @throws IllegalStateException if an attempt to instantiate the class is made
     */
    private SwarmProperties() {
        throw new IllegalStateException("Static class cannot be instantiated.");
    }

    public static void initialize() throws HiveMindException {
        if (!(Double.isFinite(tolerance) && tolerance >= 0))
            throw new SwarmException("Swarm properties tolerance must be finite and non-negative.");
        loadFile();
    }

    private static void loadFile() throws HiveMindException {
        InputStream swarmPropertiesStream;
        File customSwarmPropertiesFile = new File(SWARM_FOLDER + CUSTOM_SWARM_PROPERTIES_FILE_NAME);
        try {
            swarmPropertiesStream = new FileInputStream(customSwarmPropertiesFile);
        } catch (FileNotFoundException e) {
            try {
                Path swarmFolder = Path.of(SWARM_FOLDER);
                if (Files.notExists(swarmFolder))
                    Files.createDirectory(swarmFolder);
                swarmPropertiesStream = HiveMind.class.getResourceAsStream(DEFAULT_SWARM_PROPERTIES_RESOURCE_LOCATION);
                Files.copy(swarmPropertiesStream, swarmFolder.resolve(CUSTOM_SWARM_PROPERTIES_FILE_NAME));
            } catch (IOException ie) {
                throw new HiveMindException("Error while creating swarm folder or copying swarm properties file.", ie);
            }
        }
        parseProperties(swarmPropertiesStream);
    }

    private static void parseProperties(InputStream swarmPropertiesStream) throws HiveMindException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(swarmPropertiesStream);
            SwarmProperties.tolerance = root.get("tolerance").asDouble();
            SwarmProperties.maxPolygonVertices = root.get("maxPolygonVertices").asInt();
            SwarmProperties.signalPattern = Pattern.compile(root.get("signalRegex").asText());
            SwarmProperties.echoPattern = Pattern.compile(root.get("echoRegex").asText());
        } catch (IOException e) {
            throw new HiveMindException("Error while parsing swarm properties.", e);
        }
    }

    /**
     * Getter for tolerance.
     *
     * @return tolerance
     */
    public static double tolerance() {
        return tolerance;
    }

    /**
     * Getter for maximum number of vertices for a polygon.
     *
     * @return maximum number of vertices for a polygon
     */
    public static int maxPolygonVertices() {
        return maxPolygonVertices;
    }

    /**
     * Getter for signal pattern.
     *
     * @return signal pattern
     */
    public static Pattern signalPattern() {
        return signalPattern;
    }

    /**
     * Getter for echo pattern.
     *
     * @return echo pattern
     */
    public static Pattern echoPattern() {
        return echoPattern;
    }
}
