package it.unicam.cs.massimopavoni.swarmsimulator.swarm.core;

public final class HiveMind {
    private final SwarmState state;

    public HiveMind() throws HiveMindException {
        SwarmProperties.initialize();
        state = new SwarmState();
    }

    public SwarmState state() {
        return state;
    }
}
