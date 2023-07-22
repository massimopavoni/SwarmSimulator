package it.unicam.cs.massimopavoni.swarmsimulator.swarm.core;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.Drone;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Region;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.Shape;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives.Directive;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives.DirectiveType;

import java.util.List;
import java.util.stream.IntStream;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Class representing the current state of the swarm.
 */
public final class SwarmState {
    /**
     * List of regions constituting the domain.
     */
    private final List<Region> domain;
    /**
     * List of directives constituting the strategy.
     */
    private final List<Directive> strategy;
    /**
     * List of drones constituting the swarm.
     */
    private final List<Drone> swarm;

    /**
     * Constructor for a swarm state from domain, strategy and swarm information.
     *
     * @param domain       list of regions
     * @param strategy     list of directives
     * @param dronesNumber number of drones
     * @param spawnShape   shape of the swarm spawn region
     * @param onBoundary   true if the swarm spawns on the boundary of the provided shape
     */
    public SwarmState(List<Region> domain, List<Directive> strategy,
                      int dronesNumber, Shape spawnShape, boolean onBoundary) {
        this.domain = domain;
        this.strategy = strategy;
        List<Integer> jumpDirectiveIndexes = IntStream.range(0, this.strategy.size())
                .filter(i -> this.strategy.get(i).getType().equals(DirectiveType.JUMP))
                .boxed().toList();
        this.swarm = SwarmUtils.parallelize(() -> spawnShape.getRandomPositions(onBoundary, dronesNumber)
                .parallelStream()
                .map(rp -> new Drone(rp, jumpDirectiveIndexes))).collect(toImmutableList());
    }

    /**
     * Getter for the domain.
     *
     * @return list of regions
     */
    public List<Region> domain() {
        return domain;
    }

    /**
     * Getter for the strategy.
     *
     * @return list of directives
     */
    public List<Directive> strategy() {
        return strategy;
    }

    /**
     * Getter for the swarm.
     *
     * @return list of drones
     */
    public List<Drone> swarm() {
        return swarm;
    }
}
