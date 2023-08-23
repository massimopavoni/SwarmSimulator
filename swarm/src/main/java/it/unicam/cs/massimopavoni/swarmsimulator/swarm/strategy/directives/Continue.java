package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.Drone;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmState;

/**
 * Continue jump directive, for continuing the execution of a previous movement.
 *
 * @param jumpLimit jumps limit in time units
 */
public record Continue(int jumpLimit) implements JumpDirective {
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
