package it.unicam.cs.massimopavoni.swarmsimulator.swarm;

import com.google.common.math.DoubleMath;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmProperties;

/**
 * Static class for math operations.
 */
public final class MathUtils {
    /**
     * Static class constructor, private to prevent instantiation.
     *
     * @throws IllegalStateException if an attempt to instantiate the class is made
     */
    private MathUtils() {
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
}
