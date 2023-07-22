package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.Drone;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmState;

/**
 * Radiate echo directive, for starting the radiation of a certain echo.
 */
public final class Radiate implements EchoDirective {
    /**
     * Echo to be radiated.
     */
    private final String echo;

    /**
     * Constructor for a radiate echo directive.
     *
     * @param echo echo string
     * @throws DirectiveException if the echo string does not match the defined pattern
     */
    public Radiate(String echo) {
        SwarmUtils.checkEcho(echo, new DirectiveException("A radiate directive's echo must match " +
                "the pattern defined in the swarm properties file."));
        this.echo = echo;
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
