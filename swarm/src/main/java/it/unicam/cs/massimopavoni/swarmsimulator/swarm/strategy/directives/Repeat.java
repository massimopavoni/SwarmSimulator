package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.Drone;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmState;

/**
 * Repeat jump directive, for repeating the execution of set of directives for several times.
 */
public final class Repeat implements JumpDirective {
    /**
     * Index of the directive to jump to when the jumps reach the limit.
     */
    private final int jumpIndex;
    /**
     * Jumps limit in time units.
     */
    private final int jumpLimit;

    /**
     * Constructor for a repeat jump directive.
     *
     * @param jumpIndex directive index to jump to
     * @param jumpLimit jumps limit time units
     */
    public Repeat(int jumpIndex, int jumpLimit) {
        this.jumpIndex = jumpIndex;
        this.jumpLimit = jumpLimit;
    }

    /**
     * Constructor for a repeat forever jump directive.
     *
     * @param jumpIndex directive index to jump to
     */
    public Repeat(int jumpIndex) {
        this.jumpIndex = jumpIndex;
        this.jumpLimit = 0;
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
        if (drone.jumpCounter(currentDirective) == jumpLimit - 1)
            drone.setCurrentDirective(jumpIndex);
        else {
            drone.setCurrentDirective(currentDirective + 1);
            drone.incrementJumpCounter(currentDirective);
        }
    }
}
