package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.Drone;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmState;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Position;

/**
 * Until jump directive, for repeating the execution of set of directives until a specific signal is perceived.
 *
 * @param jumpIndex index of the directive to jump to when the condition is met
 * @param signal    signal to perceive
 */
public record Until(int jumpIndex, String signal) implements JumpDirective {
    /**
     * Constructor for an until jump directive.
     *
     * @param jumpIndex directive index to jump to
     * @param signal    signal string
     * @throws DirectiveException if the signal string does not match the defined pattern
     */
    public Until {
        SwarmUtils.checkSignal(signal, new DirectiveException("An until directive's signal must match " +
                "the pattern defined in the swarm properties file."));
    }

    /**
     * Executes the until directive on the given drone.
     *
     * @param swarmState swarm state for swarm-wide operations
     * @param drone      drone executing the directive
     */
    @Override
    public void execute(SwarmState swarmState, Drone drone) {
        Position dp = drone.position();
        if (swarmState.domain().stream()
                .filter(r -> r.signal().equals(signal))
                .anyMatch(r -> r.shape().contains(dp)))
            drone.setCurrentDirective(jumpIndex);
        else {
            int currentDirective = drone.currentDirective();
            drone.setCurrentDirective(currentDirective + 1);
            drone.incrementJumpCounter(currentDirective);
        }
    }
}
