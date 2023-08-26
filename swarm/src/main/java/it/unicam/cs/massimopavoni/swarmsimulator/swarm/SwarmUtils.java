package it.unicam.cs.massimopavoni.swarmsimulator.swarm;

import com.google.common.math.DoubleMath;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmProperties;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

/**
 * Static utility class for swarm operations.
 */
public final class SwarmUtils {
    /**
     * Custom thread pool for parallel tasks.
     */
    private static final ForkJoinPool customThreadPool;

    static {
        customThreadPool = new ForkJoinPool(SwarmProperties.parallelism());
    }

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
     * Check if a double is finite and positive.
     *
     * @param a double to check
     * @return true if the double is finite and positive, false otherwise
     */
    public static boolean isPositive(double a) {
        return Double.isFinite(a) && a > 0;
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

    /**
     * Check if a string is a valid signal.
     *
     * @param signal    string to check
     * @param exception exception to throw
     * @throws SwarmException if the string is not a valid signal
     */
    public static void checkSignal(String signal, SwarmException exception) {
        if (!SwarmProperties.signalPattern().matcher(signal).matches())
            throw exception;
    }

    /**
     * Check if a string is a valid echo.
     *
     * @param echo      string to check
     * @param exception exception to throw
     * @throws SwarmException if the string is not a valid echo
     */
    public static void checkEcho(String echo, SwarmException exception) {
        if (!SwarmProperties.echoPattern().matcher(echo).matches())
            throw exception;
    }

    /**
     * Swarm parallel task execution without final result.
     *
     * @param task task to execute
     */
    public static void parallelize(Runnable task) {
        try {
            customThreadPool.submit(task).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new SwarmException("Error during parallel task execution.", e);
        }
    }

    /**
     * Swarm parallel task execution with final result.
     *
     * @param task task to execute
     * @param <T>  task result type
     * @return task result
     */
    public static <T> T parallelize(Callable<T> task) {
        try {
            return customThreadPool.submit(task).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new SwarmException("Error during parallel task execution.", e);
        }
    }
}
