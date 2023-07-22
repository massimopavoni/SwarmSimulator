package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.Drone;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmState;

/**
 * Absorb echo directive, for stopping the radiation of a certain echo.
 */
public final class Absorb implements EchoDirective {
    /**
     * Echo to be absorbed.
     */
    private final String echo;

    /**
     * Constructor for an absorb echo directive.
     *
     * @param echo echo string
     * @throws DirectiveException if the echo string does not match the defined pattern
     */
    public Absorb(String echo) {
        SwarmUtils.checkEcho(echo, new DirectiveException("An absorb directive's echo must match " +
                "the pattern defined in the swarm properties file."));
        this.echo = echo;
    }

    /**
     * Executes the absorb directive on the given drone.
     *
     * @param swarmState swarm state for swarm-wide operations
     * @param drone      drone executing the directive
     */
    @Override
    public void execute(SwarmState swarmState, Drone drone) {
        drone.absorb(echo);
    }
}
