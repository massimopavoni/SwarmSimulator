package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;

/**
 * Enum of available directive types to categorize directives.
 */
public enum DirectiveType {
    /**
     * Echo directives, for interactions between drones and coordination of the swarm.
     */
    ECHO,
    /**
     * Jump directives, for repetitions of sets of directives and potentially endless swarm survival.
     */
    JUMP,
    /**
     * Movement directives, for movement of drones within the swarm's domain.
     */
    MOVEMENT
}
