package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.Drone;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmState;

/**
 * Done jump directive, for ending a loop.
 */
public final class Done implements JumpDirective {
    /**
     * Index of the directive to unconditionally jump to.
     */
    private final int jumpIndex;

    /**
     * Constructor for a done jump directive.
     *
     * @param jumpIndex directive index to jump to
     */
    public Done(int jumpIndex) {
        this.jumpIndex = jumpIndex;
    }

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
