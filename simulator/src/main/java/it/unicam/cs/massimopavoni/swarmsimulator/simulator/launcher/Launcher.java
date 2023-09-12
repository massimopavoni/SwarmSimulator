package it.unicam.cs.massimopavoni.swarmsimulator.simulator.launcher;

import it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui.GUIApplication;

/**
 * Class representing the simulator launcher.
 */
public final class Launcher {
    /**
     * Main method used to launch the simulator.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        new Thread(() -> GUIApplication.main(args)).start();
    }
}
