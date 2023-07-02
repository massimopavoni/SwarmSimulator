package it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.MathUtils;

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
     * @throws IllegalPositionException if the position is not finite
     */
    public Position(double x, double y) {
        if (!(Double.isFinite(x) && Double.isFinite(y)))
            throw new IllegalPositionException("Domain coordinates must be finite.");
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
     * @param p second position
     * @return true if the two positions are equal, false otherwise
     */
    public boolean isEqual(Position p) {
        return MathUtils.compare(x, p.x) == 0 && MathUtils.compare(y, p.y) == 0;
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
     * @throws IllegalPositionException if the scalar is not finite
     */
    public Position scale(double scalar) {
        if (!(Double.isFinite(scalar))) throw new IllegalPositionException("Scalar to multiply must be finite.");
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
        return new Position(dx / norm, dy / norm);
    }
}
