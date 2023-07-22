package it.unicam.cs.massimopavoni.swarmsimulator.swarm.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
    public static final String DEFAULT_SWARM_FOLDER =
            String.format("%s/.swarm/", System.getProperty("user.home"));
    /**
     * Custom swarm properties file name for user defined properties.
     */
    public static final String CUSTOM_SWARM_PROPERTIES_FILE_NAME = "swarmProperties.json";
    /**
     * Default swarm properties resource location if no custom file is found.
     */
    private static final String DEFAULT_SWARM_PROPERTIES_RESOURCE_LOCATION = "defaultSwarmProperties.json";
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
     * Initialize the swarm properties.
     *
     * @param swarmFolder swarm folder location
     * @throws HiveMindException if an error occurs while getting the properties file or parsing it
     */
    public static void initialize(String swarmFolder) throws HiveMindException {
        if (signalPattern == null)
            loadFile(swarmFolder);
    }

    /**
     * Initialize the swarm properties.
     *
     * @throws HiveMindException if an error occurs while getting the properties file or parsing it
     */
    public static void initialize() throws HiveMindException {
        if (signalPattern == null)
            loadFile(DEFAULT_SWARM_FOLDER);
    }

    /**
     * Load the appropriate swarm properties file.
     * If the custom file is not found, the default one is copied from the resources.
     *
     * @param swarmFolder swarm folder location
     * @throws HiveMindException if an error occurs while getting the properties file or parsing it
     */
    private static void loadFile(String swarmFolder) throws HiveMindException {
        InputStream swarmPropertiesStream;
        Path customSwarmPropertiesPath = Path.of(swarmFolder + CUSTOM_SWARM_PROPERTIES_FILE_NAME);
        try {
            if (Files.notExists(customSwarmPropertiesPath)) {
                Path swarmFolderPath = Path.of(swarmFolder);
                if (Files.notExists(swarmFolderPath))
                    Files.createDirectories(swarmFolderPath);
                swarmPropertiesStream = HiveMind.class.getResourceAsStream(DEFAULT_SWARM_PROPERTIES_RESOURCE_LOCATION);
                if (swarmPropertiesStream == null)
                    throw new HiveMindException("Error while getting default swarm properties resource.");
                Files.copy(swarmPropertiesStream, customSwarmPropertiesPath);
                swarmPropertiesStream.close();
            }
            swarmPropertiesStream = new FileInputStream(customSwarmPropertiesPath.toFile());
        } catch (IOException ie) {
            throw new HiveMindException("Error while creating swarm folder or copying swarm properties file.", ie);
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
        if (!(Double.isFinite(tolerance) && tolerance >= 0))
            throw new SwarmException("Tolerance must be finite and non-negative.");
        if (parallelism < 4 || parallelism > 32)
            throw new SwarmException("Parallelism level must be between 1 and 32.");
        if (maxPolygonVertices < 3)
            throw new SwarmException("Maximum number of vertices for a polygon must be at least 3.");
        if (maxDomainRegions < 0)
            throw new SwarmException("Maximum number of regions in the domain must be at least 0.");
        if (maxDronesNumber < 1 || maxDronesNumber > 1048576)
            throw new SwarmException("Maximum number of drones in the swarm must be between 1 and 1048576.");
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
