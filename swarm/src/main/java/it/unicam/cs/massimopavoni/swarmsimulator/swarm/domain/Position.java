package it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmProperties;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Immutable class for all swarm positions representation.
 */
public final class Position {
    /**
     * First coordinate of the position.
     */
    private final double x;
    /**
     * Second coordinate of the position.
     */
    private final double y;

    /**
     * Position constructor from coordinates.
     *
     * @param x first coordinate
     * @param y second coordinate
     * @throws PositionException if the position is not meaningful
     */
    public Position(double x, double y) {
        if (!(SwarmUtils.isMeaningful(x) && SwarmUtils.isMeaningful(y)))
            throw new PositionException("Domain coordinates must be meaningful.");
        this.x = x;
        this.y = y;
    }

    /**
     * Position constructor from a position.
     *
     * @param p position
     */
    public Position(Position p) {
        this.x = p.x;
        this.y = p.y;
    }

    /**
     * Get random position within a rectangle area.
     *
     * @param llp lower left position
     * @param urp upper right position
     * @return random position
     */
    public static Position random(Position llp, Position urp) {
        // The following shenanigans are hereby put in place because of the shortcomings of
        // the bounded nextDouble implementation of the RandomGenerator interface (implemented by
        // ThreadLocalRandom), caused by the RandomSupport utility class' check (double) range method.
        double xDelta = llp.x == urp.x ? SwarmProperties.tolerance() : 0;
        double yDelta = llp.y == urp.y ? SwarmProperties.tolerance() : 0;
        return new Position(ThreadLocalRandom.current().nextDouble(
                Math.min(llp.x, urp.x) - xDelta, Math.max(llp.x, urp.x) + xDelta),
                ThreadLocalRandom.current().nextDouble(
                        Math.min(llp.y, urp.y) - yDelta, Math.max(llp.y, urp.y) + yDelta));
    }

    /**
     * Calculate the average position of a list of positions.
     *
     * @param positions list of positions
     * @return average position
     */
    public static Position averageOf(List<Position> positions) {
        double x = positions.stream().mapToDouble(Position::x).average().orElse(0);
        double y = positions.stream().mapToDouble(Position::y).average().orElse(0);
        return new Position(x, y);
    }

    /**
     * First coordinate getter.
     *
     * @return first coordinate
     */
    public double x() {
        return x;
    }

    /**
     * Second coordinate getter.
     *
     * @return second coordinate
     */
    public double y() {
        return y;
    }

    /**
     * Check if the position is positive.
     *
     * @return true if the position is positive, false otherwise
     */
    public boolean isPositive() {
        return x > 0 && y > 0;
    }

    /**
     * Check if the position is equal to another position, with the configured tolerance.
     *
     * @param p second position
     * @return true if the two positions are equal, false otherwise
     */
    public boolean equalTo(Position p) {
        return SwarmUtils.compare(x, p.x) == 0 && SwarmUtils.compare(y, p.y) == 0;
    }

    /**
     * Translate the position by a delta position.
     *
     * @param dp delta position
     * @return translated position
     */
    public Position translate(Position dp) {
        return new Position(x + dp.x, y + dp.y);
    }

    /**
     * Scale the position by a scalar.
     *
     * @param scalar scalar to multiply
     * @return scaled position
     * @throws PositionException if the scalar is not finite
     */
    public Position scale(double scalar) {
        if (!Double.isFinite(scalar)) throw new PositionException("Scalar to multiply must be finite.");
        return new Position(x * scalar, y * scalar);
    }

    /**
     * Calculate distance to another position.
     *
     * @param p second position
     * @return distance between the two positions
     */
    public double distanceTo(Position p) {
        return Math.hypot(p.x - x, p.y - y);
    }

    /**
     * Calculate the Manhattan distance to another position.
     *
     * @param p second position
     * @return Manhattan distance between the two position
     */
    public double manhattanDistanceTo(Position p) {
        return Math.abs(p.x - x) + Math.abs(p.y - y);
    }

    /**
     * Calculate normalized direction to another position.
     *
     * @param p second position
     * @return versor towards the second position
     */
    public Position directionTo(Position p) {
        double dx = p.x - x;
        double dy = p.y - y;
        double norm = Math.hypot(dx, dy);
        if (norm == 0) return new Position(0, 0);
        return new Position(dx / norm, dy / norm);
    }
}
