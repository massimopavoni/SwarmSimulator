package it.unicam.cs.massimopavoni.swarmsimulator.swarm.core;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.Drone;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives.Directive;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives.DirectiveType;

import java.util.ArrayList;
import java.util.List;

/**
 * Class managing every type of macroscopic operation on the swarm.
 */
public final class HiveMind {
    /**
     * Current state of the swarm.
     */
    private final SwarmState state;
    /**
     * List of drones that have not finished their strategy.
     */
    private List<Drone> unfinishedStrategyDrones;
    /**
     * Number of steps executed by the hive mind.
     */
    private int stepCount;
    /**
     * Flag for swarm life status.
     */
    private boolean swarmAlive;
    /**
     * Number of steps the swarm was able to live.
     */
    private int swarmLifeDuration;

    /**
     * Constructor for the hive mind from a swarm state.
     *
     * @param swarmState starting state of the swarm
     * @throws HiveMindException if the swarm properties cannot be initialized
     */
    public HiveMind(SwarmState swarmState) throws HiveMindException {
        SwarmProperties.initialize();
        state = swarmState;
        unfinishedStrategyDrones = new ArrayList<>(state.swarm());
        stepCount = 0;
        swarmAlive = true;
        swarmLifeDuration = 0;
    }

    /**
     * Getter for the swarm state.
     *
     * @return the current state of the swarm
     */
    public SwarmState state() {
        return state;
    }

    /**
     * Getter for the number of steps executed by the hive mind.
     *
     * @return the number of steps
     */
    public int stepCount() {
        return stepCount;
    }

    /**
     * Getter for the swarm life status.
     *
     * @return true if the swarm is alive, false otherwise
     */
    public boolean isSwarmAlive() {
        return swarmAlive;
    }

    /**
     * Getter for the swarm life duration.
     *
     * @return the number of steps the swarm was able to live
     */
    public int swarmLifeDuration() {
        return swarmLifeDuration;
    }

    /**
     * Execute a swarm step, executing the current directives for each drone and moving them.
     */
    public void swarmStep() {
        unfinishedStrategyDrones = SwarmUtils.parallelize(() -> unfinishedStrategyDrones
                .parallelStream()
                .filter(d -> d.currentDirective() < state.strategy().size())
                .toList());
        if (unfinishedStrategyDrones.isEmpty()) {
            swarmAlive = false;
            if (swarmLifeDuration == 0)
                swarmLifeDuration = stepCount;
        } else {
            executeMovementDirectives();
            executeEchoDirectives();
            executeJumpDirectives();
            SwarmUtils.parallelize(() -> unfinishedStrategyDrones
                    .parallelStream()
                    .filter(Drone::isAlive)
                    .forEach(Drone::stepMove));
        }
        stepCount++;
    }

    /**
     * Execute movement directives on drones.
     */
    private void executeMovementDirectives() {
        SwarmUtils.parallelize(() -> unfinishedStrategyDrones
                .parallelStream()
                .forEach(d -> {
                    Directive dv = state.strategy().get(d.currentDirective());
                    if (dv.getType() == DirectiveType.MOVEMENT && d.isAlive())
                        dv.execute(state, d);
                }));
    }

    /**
     * Execute echo directives on drones.
     */
    private void executeEchoDirectives() {
        SwarmUtils.parallelize(() -> unfinishedStrategyDrones
                .parallelStream()
                .forEach(d -> {
                    Directive dv = state.strategy().get(d.currentDirective());
                    if (dv.getType() == DirectiveType.ECHO)
                        dv.execute(state, d);
                }));
    }

    /**
     * Execute jump directives on drones and increment directive indexes when there's no jump.
     */
    private void executeJumpDirectives() {
        SwarmUtils.parallelize(() -> unfinishedStrategyDrones
                .parallelStream()
                .forEach(d -> {
                    Directive dv = state.strategy().get(d.currentDirective());
                    if (dv.getType() == DirectiveType.JUMP)
                        dv.execute(state, d);
                    else
                        d.incrementCurrentDirective();
                }));
    }
}
