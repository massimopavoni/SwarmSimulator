package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;

import java.util.List;

/**
 * Interface for echo directives.
 */
public sealed interface EchoDirective extends Directive permits Absorb, Radiate {
    /**
     * List of permitted echo directives.
     */
    List<Class<?>> PERMITTED_DIRECTIVES = List.of(EchoDirective.class.getPermittedSubclasses());

    /**
     * Returns true if the given directive is a permitted echo directive.
     *
     * @param directive directive to check
     * @return true if directive is an echo directive, false otherwise
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
        return DirectiveType.ECHO;
    }
}
