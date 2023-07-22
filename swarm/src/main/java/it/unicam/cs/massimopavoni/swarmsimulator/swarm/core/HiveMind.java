package it.unicam.cs.massimopavoni.swarmsimulator.swarm.core;

public final class HiveMind {
    private SwarmState state;

    public HiveMind() throws HiveMindException {
        SwarmProperties.initialize();
    }

    public SwarmState state() {
        return state;
    }
}
