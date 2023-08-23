package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.Drone;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmState;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Position;

import java.util.List;

/**
 * Follow movement directive, for setting a drone direction to the average position of drones radiating a specific echo.
 *
 * @param echo  echo to follow
 * @param range range within which the drone can hear the echo
 * @param speed movement speed to set
 */
public record Follow(String echo, double range, double speed) implements MovementDirective {
    /**
     * Constructor for a follow movement directive.
     *
     * @param echo  echo string
     * @param range echo range
     * @param speed movement speed
     * @throws DirectiveException if the echo does not match the defined pattern or
     *                            if the range or speed are not positive
     */
    public Follow {
        SwarmUtils.checkEcho(echo, new DirectiveException("A follow directive's echo must match " +
                "the pattern defined in the swarm properties file."));
        if (!(SwarmUtils.isPositive(range) && SwarmUtils.isPositive(speed)))
            throw new DirectiveException("A follow directive's range and speed must be finite positive numbers.");
    }

    /**
     * Executes the follow directive on the given drone.
     *
     * @param swarmState swarm state for swarm-wide operations
     * @param drone      drone executing the directive
     */
    @Override
    public void execute(SwarmState swarmState, Drone drone) {
        Position dp = drone.position();
        List<Position> radiatingDronePositions = swarmState.swarm().stream()
                .filter(d -> d.isRadiating(echo) && SwarmUtils.compare(dp.distanceTo(d.position()), range) <= 0)
                .map(Drone::position).toList();
        drone.setDirection(drone.position().directionTo(
                radiatingDronePositions.isEmpty() ?
                        Position.random(new Position(dp.x() - range, dp.y() - range),
                                new Position(dp.x() + range, dp.y() + range))
                        : Position.averageOf(radiatingDronePositions)));
        drone.setSpeed(speed);
    }
}
