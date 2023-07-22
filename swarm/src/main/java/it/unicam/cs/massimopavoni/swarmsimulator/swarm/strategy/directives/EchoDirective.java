package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;

/**
 * Interface for echo directives.
 */
public sealed interface EchoDirective extends Directive permits Absorb, Radiate {
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
