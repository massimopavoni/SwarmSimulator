package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.Drone;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmState;

/**
 * Radiate echo directive, for starting the radiation of a certain echo.
 *
 * @param echo echo to be radiated
 */
public record Radiate(String echo) implements EchoDirective {
    /**
     * Constructor for a radiate echo directive.
     *
     * @param echo echo string
     * @throws DirectiveException if the echo string does not match the defined pattern
     */
    public Radiate {
        SwarmUtils.checkEcho(echo, new DirectiveException("A radiate directive's echo must match " +
                "the pattern defined in the swarm properties file."));
    }

    /**
     * Executes the radiate directive on the given drone.
     *
     * @param swarmState swarm state for swarm-wide operations
     * @param drone      drone executing the directive
     */
    @Override
    public void execute(SwarmState swarmState, Drone drone) {
        drone.radiate(echo);
    }
}
