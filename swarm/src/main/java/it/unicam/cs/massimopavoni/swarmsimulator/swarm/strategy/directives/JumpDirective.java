package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;

import java.util.List;

/**
 * Interface for jump directives.
 */
public sealed interface JumpDirective extends Directive permits Continue, Done, Repeat, Until {
    /**
     * List of permitted jump directives.
     */
    List<Class<?>> PERMITTED_DIRECTIVES = List.of(JumpDirective.class.getPermittedSubclasses());

    /**
     * Returns true if the given directive is a permitted jump directive.
     *
     * @param directive directive to check
     * @return true if directive is a jump directive, false otherwise
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
        return DirectiveType.JUMP;
    }
}
