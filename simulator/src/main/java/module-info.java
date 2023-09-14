/**
 * Simulator module info.
 */
module it.unicam.cs.massimopavoni.swarmsimulator.simulator {
    requires it.unicam.cs.massimopavoni.swarmsimulator.swarm;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;

    opens it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui to javafx.graphics, javafx.fxml;
    opens it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui.control to javafx.controls, javafx.fxml;
    opens it.unicam.cs.massimopavoni.swarmsimulator.simulator to javafx.graphics, javafx.fxml;

    exports it.unicam.cs.massimopavoni.swarmsimulator.simulator;
    exports it.unicam.cs.massimopavoni.swarmsimulator.simulator.launcher;
    exports it.unicam.cs.massimopavoni.swarmsimulator.simulator.view;
    exports it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui;
    exports it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui.control;
}