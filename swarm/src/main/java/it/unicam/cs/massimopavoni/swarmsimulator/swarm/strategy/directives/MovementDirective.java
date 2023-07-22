package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;

/**
 * Interface for movement directives.
 */
public sealed interface MovementDirective extends Directive permits Follow, Move, Stop {
    /**
     * Returns the type of the directive.
     *
     * @return directive type
     */
    @Override
    default DirectiveType getType() {
        return DirectiveType.MOVEMENT;
    }
}
