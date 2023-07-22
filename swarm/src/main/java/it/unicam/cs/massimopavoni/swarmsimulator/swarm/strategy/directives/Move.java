package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.Drone;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmState;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Position;

/**
 * Move movement directive, for setting the drone's direction and speed.
 */
public final class Move implements MovementDirective {
    /**
     * Destination position to move towards.
     */
    private final Position destination;
    /**
     * Movement speed to set.
     */
    private final double speed;

    /**
     * Constructor for a move movement directive.
     *
     * @param destination destination position
     * @param speed       movement speed
     * @throws DirectiveException if the speed is not positive
     */
    public Move(Position destination, double speed) {
        if (!SwarmUtils.isPositive(speed))
            throw new DirectiveException("A move directive's speed must be finite and positive.");
        this.destination = destination;
        this.speed = speed;
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
