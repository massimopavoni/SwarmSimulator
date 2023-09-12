package it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui.control;

import it.unicam.cs.massimopavoni.swarmsimulator.simulator.Resources;
import javafx.stage.Modality;

import java.io.IOException;

/**
 * Class representing controls swarm dialog.
 */
public final class SwarmControls extends SwarmDialog {

    /**
     * Constructor for controls dialog with keyboard and mouse.
     *
     * @throws IOException if FXML file cannot be loaded
     */
    public SwarmControls() throws IOException {
        super("controls.fxml", Modality.NONE);
        stage.setTitle(String.format("%s controls", Resources.NAME));
    }

    /**
     * Show controls dialog in a non-blocking way.
     */
    public void show() {
        positionStage();
        stage.showAndWait();
    }
}
