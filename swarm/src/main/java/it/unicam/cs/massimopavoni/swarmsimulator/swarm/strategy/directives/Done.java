package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.Drone;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmState;

/**
 * Done jump directive, for ending a loop.
 *
 * @param jumpIndex index of the directive to unconditionally jump to
 */
public record Done(int jumpIndex) implements JumpDirective {
    /**
     * Executes the done directive on the given drone.
     *
     * @param swarmState swarm state for swarm-wide operations
     * @param drone      drone executing the directive
     */
    @Override
    public void execute(SwarmState swarmState, Drone drone) {
        drone.setCurrentDirective(jumpIndex);
    }
}
