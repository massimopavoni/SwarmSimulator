package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.Drone;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmState;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Position;

/**
 * Move movement directive, for setting the drone's direction and speed.
 *
 * @param destination destination position to move towards
 * @param speed       movement speed to set
 */
public record Move(Position destination, double speed) implements MovementDirective {
    /**
     * Constructor for a move movement directive.
     *
     * @param destination destination position
     * @param speed       movement speed
     * @throws DirectiveException if the speed is not positive
     */
    public Move {
        if (!SwarmUtils.isPositive(speed))
            throw new DirectiveException("A move directive's speed must be finite and positive.");
    }

    /**
     * Constructor for a move random movement directive.
     *
     * @param llp   lower left position of the random destination position's bounding box
     * @param urp   upper right position of the random destination position's bounding box
     * @param speed movement speed
     * @throws DirectiveException if the speed is not positive
     */
    public Move(Position llp, Position urp, double speed) {
        this(Position.random(llp, urp), speed);
    }

    /**
     * Executes the move directive on the given drone.
     *
     * @param swarmState swarm state for swarm-wide operations
     * @param drone      drone executing the directive
     */
    @Override
    public void execute(SwarmState swarmState, Drone drone) {
        drone.setDirection(drone.position().directionTo(destination));
        drone.setSpeed(speed);
    }
}
