package it.unicam.cs.massimopavoni.swarmsimulator.swarm;

import com.google.common.math.DoubleMath;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmProperties;

import java.util.Arrays;

/**
 * Static class for math operations.
 */
public final class SwarmUtils {
    /**
     * Static class constructor, private to prevent instantiation.
     *
     * @throws IllegalStateException if an attempt to instantiate the class is made
     */
    private SwarmUtils() {
        throw new IllegalStateException("Static class cannot be instantiated.");
    }

    /**
     * Compare two doubles with the configured tolerance.
     *
     * @param a first double
     * @param b second double
     * @return 0 if the two doubles are equal, positive if the first is greater than the second, negative otherwise
     */
    public static int compare(double a, double b) {
        return DoubleMath.fuzzyCompare(a, b, SwarmProperties.tolerance());
    }

    /**
     * Converts a string array to a double array skipping some elements.
     *
     * @param args string array
     * @param skip number of elements to skip
     * @return double array
     * @throws NumberFormatException if the string array contains non-double elements
     */
    public static double[] toDoubleArray(String[] args, int skip) {
        return Arrays.stream(args).skip(skip).mapToDouble(Double::parseDouble).toArray();
    }
}
