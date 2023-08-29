/**
 * Simulator module info.
 */
module it.unicam.cs.massimopavoni.swarmsimulator.simulator {
    requires it.unicam.cs.massimopavoni.swarmsimulator.swarm;
    requires javafx.controls;
    requires javafx.fxml;

    opens it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui to javafx.graphics, javafx.fxml;
    opens it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui.control to javafx.fxml;
    opens it.unicam.cs.massimopavoni.swarmsimulator.simulator to javafx.fxml, javafx.graphics;
}