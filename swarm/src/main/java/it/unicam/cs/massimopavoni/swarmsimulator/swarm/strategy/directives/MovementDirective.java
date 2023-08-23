package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;

import java.util.List;

/**
 * Interface for movement directives.
 */
public sealed interface MovementDirective extends Directive permits Follow, Move, Stop {
    /**
     * List of permitted movement directives.
     */
    List<Class<?>> PERMITTED_DIRECTIVES = List.of(MovementDirective.class.getPermittedSubclasses());

    /**
     * Returns true if the given directive is a permitted movement directive.
     *
     * @param directive directive to check
     * @return true if directive is a movement directive, false otherwise
     */
    static boolean isPermittedDirective(Class<? extends Directive> directive) {
        return PERMITTED_DIRECTIVES.contains(directive);
    }

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
