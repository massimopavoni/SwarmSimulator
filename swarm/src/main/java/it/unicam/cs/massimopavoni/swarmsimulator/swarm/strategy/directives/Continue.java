package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.Drone;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmState;

/**
 * Continue jump directive, for continuing the execution of a previous movement.
 */
public final class Continue implements JumpDirective {
    /**
     * Jumps limit in time units.
     */
    private final int jumpLimit;

    /**
     * Constructor for a continue jump directive.
     *
     * @param jumpLimit jumps limit time units
     */
    public Continue(int jumpLimit) {
        this.jumpLimit = jumpLimit;
    }

    /**
     * Executes the continue directive on the given drone.
     *
     * @param swarmState swarm state for swarm-wide operations
     * @param drone      drone executing the directive
     */
    @Override
    public void execute(SwarmState swarmState, Drone drone) {
        int currentDirective = drone.currentDirective();
        if (drone.jumpCounter(currentDirective) == jumpLimit - 1)
            drone.setCurrentDirective(currentDirective + 1);
        else
            drone.incrementJumpCounter(currentDirective);
    }
}
