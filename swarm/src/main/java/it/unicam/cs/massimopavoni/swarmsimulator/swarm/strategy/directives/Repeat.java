package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.Drone;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmState;

/**
 * Repeat jump directive, for repeating the execution of set of directives for several times.
 *
 * @param jumpIndex index of the directive to jump to when the jumps reach the limit
 * @param jumpLimit jumps limit in time units
 */
public record Repeat(int jumpIndex, int jumpLimit) implements JumpDirective {
    /**
     * Constructor for a repeat forever jump directive.
     *
     * @param jumpIndex directive index to jump to
     */
    public Repeat(int jumpIndex) {
        this(jumpIndex, 0);
    }

    /**
     * Executes the repeat directive on the given drone.
     *
     * @param swarmState swarm state for swarm-wide operations
     * @param drone      drone executing the directive
     */
    @Override
    public void execute(SwarmState swarmState, Drone drone) {
        int currentDirective = drone.currentDirective();
        if (drone.jumpCounter(currentDirective) == jumpLimit - 1) {
            drone.resetJumpCounter(currentDirective);
            drone.setCurrentDirective(jumpIndex);
        } else {
            drone.incrementCurrentDirective();
            drone.increaseJumpCounter(currentDirective);
        }
    }
}
