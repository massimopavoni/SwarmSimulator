package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.Drone;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmState;

/**
 * Stop movement directive, for stopping the drone and terminating its life.
 */
public final class Stop implements MovementDirective {
    /**
     * Executes the stop directive on the given drone.
     *
     * @param swarmState swarm state for swarm-wide operations
     * @param drone      drone executing the directive
     */
    @Override
    public void execute(SwarmState swarmState, Drone drone) {
        drone.terminateLife();
    }
}
