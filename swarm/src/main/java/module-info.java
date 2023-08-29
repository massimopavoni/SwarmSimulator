/**
 * Swarm module info.
 */
module it.unicam.cs.massimopavoni.swarmsimulator.swarm {
    requires com.google.common;
    requires com.fasterxml.jackson.databind;

    exports it.unicam.cs.massimopavoni.swarmsimulator.swarm;
    exports it.unicam.cs.massimopavoni.swarmsimulator.swarm.core;
    exports it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain;
    exports it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.parser;
    exports it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes;
    exports it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy;
    exports it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives;
    exports it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.parser;
}