package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.Drone;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmState;

/**
 * Interface for swarm strategy directives.
 */
public sealed interface Directive permits EchoDirective, JumpDirective, MovementDirective {
    /**
     * Returns the type of the directive.
     *
     * @return directive type
     */
    DirectiveType getType();

    /**
     * Executes the directive on the given drone.
     *
     * @param swarmState swarm state for swarm-wide operations
     * @param drone      drone executing the directive
     */
    void execute(SwarmState swarmState, Drone drone);
}
