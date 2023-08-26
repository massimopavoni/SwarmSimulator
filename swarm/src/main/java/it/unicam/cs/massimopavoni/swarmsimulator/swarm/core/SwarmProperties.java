package it.unicam.cs.massimopavoni.swarmsimulator.swarm.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Static class defining the swarm properties.
 */
public final class SwarmProperties {
    /**
     * Default swarm folder location within the user's home directory.
     */
    public static final String DEFAULT_SWARM_FOLDER =
            String.format("%s/.swarm/", System.getProperty("user.home"));
    /**
     * Default custom swarm properties file name for user defined properties.
     */
    public static final String DEFAULT_SWARM_PROPERTIES_FILE_NAME = "swarmProperties.json";
    /**
     * Default swarm properties resource location if no custom file is found.
     */
    public static final String DEFAULT_SWARM_PROPERTIES_RESOURCE_LOCATION = "defaultSwarmProperties.json";
    /**
     * Tolerance for double comparisons.
     */
    private static double tolerance;
    /**
     * Parallelism level for parallel swarm operations.
     */
    private static int parallelism;
    /**
     * Maximum number of vertices for a polygon.
     */
    private static int maxPolygonVertices;
    /**
     * Maximum number of regions in the domain.
     */
    private static int maxDomainRegions;
    /**
     * Maximum number of drones in the swarm.
     */
    private static int maxDronesNumber;
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

    /**
     * Initialize the swarm properties with a custom folder and file name.
     *
     * @param swarmFolder swarm folder location
     * @param fileName    swarm properties file name
     * @throws HiveMindException if an error occurs while getting the properties file or parsing it
     */
    public static void initialize(String swarmFolder, String fileName) throws HiveMindException {
        if (signalPattern == null)
            loadFile(swarmFolder, fileName);
    }

    /**
     * Initialize the swarm properties.
     *
     * @throws HiveMindException if an error occurs while getting the properties file or parsing it
     */
    public static void initialize() throws HiveMindException {
        initialize(DEFAULT_SWARM_FOLDER, DEFAULT_SWARM_PROPERTIES_FILE_NAME);
    }

    /**
     * Load the appropriate swarm properties file.
     * If the custom file is not found, the default one is copied from the resources.
     *
     * @param swarmFolder swarm folder location
     * @throws HiveMindException if an error occurs while getting the properties file or parsing it
     */
    private static void loadFile(String swarmFolder, String fileName) throws HiveMindException {
        InputStream swarmPropertiesStream;
        Path customSwarmPropertiesPath = Path.of(swarmFolder + fileName);
        try {
            if (Files.notExists(customSwarmPropertiesPath)) {
                Path swarmFolderPath = Path.of(swarmFolder);
                if (Files.notExists(swarmFolderPath))
                    Files.createDirectories(swarmFolderPath);
                swarmPropertiesStream = Objects.requireNonNull(
                        HiveMind.class.getResourceAsStream(DEFAULT_SWARM_PROPERTIES_RESOURCE_LOCATION),
                        "Default swarm properties resource not found inside library.");
                Files.copy(swarmPropertiesStream, customSwarmPropertiesPath);
                swarmPropertiesStream.close();
            }
            swarmPropertiesStream = new FileInputStream(customSwarmPropertiesPath.toFile());
        } catch (IOException e) {
            throw new HiveMindException("Error while creating swarm folder or copying swarm properties file.", e);
        }
        parseProperties(swarmPropertiesStream);
    }

    /**
     * Parse the swarm properties file.
     *
     * @param swarmPropertiesStream input stream for the properties
     * @throws HiveMindException if an error occurs while parsing the properties
     */
    private static void parseProperties(InputStream swarmPropertiesStream) throws HiveMindException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(swarmPropertiesStream);
            tolerance = root.get("tolerance").requireNonNull().asDouble();
            parallelism = root.get("parallelism").requireNonNull().asInt();
            maxPolygonVertices = root.get("maxPolygonVertices").requireNonNull().asInt();
            maxDomainRegions = root.get("maxDomainRegions").requireNonNull().asInt();
            maxDronesNumber = root.get("maxDronesNumber").requireNonNull().asInt();
            signalPattern = Pattern.compile(root.get("signalRegex").requireNonNull().asText());
            echoPattern = Pattern.compile(root.get("echoRegex").requireNonNull().asText());
            checkProperties();
        } catch (IOException | IllegalArgumentException | SwarmException e) {
            throw new HiveMindException("Error while parsing swarm properties.", e);
        }
    }

    /**
     * Check the properties for valid values.
     *
     * @throws SwarmException if a property is invalid
     */
    private static void checkProperties() {
        if (!SwarmUtils.isPositive(tolerance))
            throw new SwarmException("Tolerance must be finite and positive.");
        if (parallelism < 4 || parallelism > 32)
            throw new SwarmException("Parallelism level must be between 4 and 32.");
        if (maxPolygonVertices < 3 || maxPolygonVertices > 256)
            throw new SwarmException("Maximum number of vertices for a polygon must be between 3 and 256.");
        if (maxDomainRegions < 0 || maxDomainRegions > 64)
            throw new SwarmException("Maximum number of regions in the domain must be between 0 and 256.");
        if (maxDronesNumber < 16 || maxDronesNumber > 262144)
            throw new SwarmException("Maximum number of drones in the swarm must be between 16 and 262144.");
        if (isInvalidSwarmRegex(signalPattern) || isInvalidSwarmRegex(echoPattern))
            throw new SwarmException("Signal and echo patterns must be valid regular expressions " +
                    "that do not match spaces.");
    }

    /**
     * Check if a regex is invalid for the swarm (i.e. matches spaces).
     *
     * @param regex regex to check
     * @return true if the regex is invalid, false otherwise
     */
    private static boolean isInvalidSwarmRegex(Pattern regex) {
        return regex.matcher(" ").matches();
    }

    /**
     * Reset the swarm properties.
     */
    public static void reset() {
        tolerance = 0;
        parallelism = 0;
        maxPolygonVertices = 0;
        maxDomainRegions = 0;
        maxDronesNumber = 0;
        signalPattern = null;
        echoPattern = null;
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
     * Getter for parallelism level.
     *
     * @return parallelism level
     */
    public static int parallelism() {
        return parallelism;
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
     * Getter for maximum number of regions in the domain.
     *
     * @return maximum number of regions in the domain
     */
    public static int maxDomainRegions() {
        return maxDomainRegions;
    }

    /**
     * Getter for maximum number of drones in the swarm.
     *
     * @return maximum number of drones in the swarm
     */
    public static int maxDronesNumber() {
        return maxDronesNumber;
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
