package it.unicam.cs.massimopavoni.swarmsimulator.swarm;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Position;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class representing a swarm drone.
 */
public class Drone {
    /**
     * Map of jump directive counters.
     */
    private final Map<Integer, Integer> jumpCounters;
    /**
     * Set of echoes currently being radiated.
     */
    private final Set<String> echoes;
    /**
     * Flag for drone life status.
     */
    private boolean alive;
    /**
     * Index of current directive.
     */
    private int currentDirective;
    /**
     * Flag for strategy completion.
     */
    private boolean strategyDone;
    /**
     * Current drone position.
     */
    private Position position;
    /**
     * Current drone direction.
     */
    private Position direction;
    /**
     * Current drone speed.
     */
    private double speed;

    /**
     * Constructor for drone given an initial position and a list of jump indexes.
     *
     * @param initialPosition drone starting position
     * @param jumpIndexes     list of jump indexes
     */
    public Drone(Position initialPosition, List<Integer> jumpIndexes) {
        jumpCounters = jumpIndexes.stream().collect(Collectors.toMap(i -> i, i -> 0));
        echoes = new HashSet<>();
        alive = true;
        currentDirective = 0;
        strategyDone = false;
        position = initialPosition;
        direction = new Position(0, 0);
        speed = 0;
    }

    /**
     * Getter for drone life status.
     *
     * @return true if drone is alive, false otherwise
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Terminates drone life, setting speed and direction to 0 values.
     */
    public void terminateLife() {
        setDirection(new Position(0, 0));
        setSpeed(0);
        alive = false;
    }

    /**
     * Getter for current directive index.
     *
     * @return current directive index
     */
    public int currentDirective() {
        return currentDirective;
    }

    /**
     * Setter for current directive index.
     *
     * @param index new current directive index
     */
    public void setCurrentDirective(int index) {
        this.currentDirective = index;
    }

    /**
     * Setter for strategy completion flag.
     */
    public void setStrategyDone() {
        strategyDone = true;
    }

    /**
     * Getter for strategy completion flag.
     *
     * @return true if strategy is done, false otherwise
     */
    public boolean isStrategyDone() {
        return strategyDone;
    }

    /**
     * Getter for a specified jump directive counter.
     *
     * @param jumpIndex jump directive index
     * @return corresponding directive's jump counter
     * @throws SwarmException if jump directive index is not found
     */
    public int jumpCounter(int jumpIndex) {
        if (!jumpCounters.containsKey(jumpIndex))
            throw new SwarmException("Jump directive index not found.");
        return jumpCounters.get(jumpIndex);
    }

    /**
     * Increments a specified jump directive counter.
     *
     * @param jumpIndex jump directive index
     * @throws SwarmException if jump directive index is not found
     */
    public void incrementJumpCounter(int jumpIndex) {
        if (!jumpCounters.containsKey(jumpIndex))
            throw new SwarmException("Jump directive index not found.");
        jumpCounters.put(jumpIndex, jumpCounters.get(jumpIndex) + 1);
    }

    /**
     * Getter for current position.
     *
     * @return current position
     */
    public Position position() {
        return position;
    }

    /**
     * Getter for current direction.
     *
     * @return current direction
     */
    public Position direction() {
        return direction;
    }

    /**
     * Setter for current direction.
     *
     * @param direction new current direction
     */
    public void setDirection(Position direction) {
        this.direction = direction;
    }

    /**
     * Getter for current speed.
     *
     * @return current speed
     */
    public double speed() {
        return speed;
    }

    /**
     * Setter for current speed.
     *
     * @param speed new current speed
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Checks if a specified echo is currently being radiated.
     *
     * @param echo echo to check
     * @return true if echo is being radiated, false otherwise
     */
    public boolean isRadiating(String echo) {
        return echoes.contains(echo);
    }

    /**
     * Starts radiating a specified echo.
     *
     * @param echo echo to radiate
     * @throws SwarmException if the echo string does not match the defined pattern
     */
    public void radiate(String echo) {
        SwarmUtils.checkEcho(echo, new SwarmException("A drone echo must match " +
                "the pattern defined in the swarm properties file."));
        echoes.add(echo);
    }

    /**
     * Stops radiating a specified echo.
     *
     * @param echo echo to absorb
     * @throws SwarmException if the echo string does not match the defined pattern
     */
    public void absorb(String echo) {
        SwarmUtils.checkEcho(echo, new SwarmException("A drone echo must match " +
                "the pattern defined in the swarm properties file."));
        echoes.remove(echo);
    }

    /**
     * Moves the drone one step in the current direction.
     */
    public void stepMove() {
        position = position.translate(direction.scale(speed));
    }
}
