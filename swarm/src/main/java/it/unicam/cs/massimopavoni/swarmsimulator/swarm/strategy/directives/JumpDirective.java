package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;

/**
 * Interface for jump directives.
 */
public sealed interface JumpDirective extends Directive permits Continue, Done, Repeat, Until {
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
