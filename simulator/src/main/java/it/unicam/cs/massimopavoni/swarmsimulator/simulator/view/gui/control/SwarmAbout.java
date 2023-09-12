package it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui.control;

import it.unicam.cs.massimopavoni.swarmsimulator.simulator.Resources;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Modality;

import java.io.IOException;

/**
 * Class representing about swarm dialog.
 */
public final class SwarmAbout extends SwarmDialog {
    /**
     * About dialog header label.
     */
    @FXML
    private Label headerLabel;
    /**
     * About dialog content label.
     */
    @FXML
    private Label contentLabel;

    /**
     * Constructor for about dialog with application information.
     *
     * @throws IOException if FXML file cannot be loaded
     */
    public SwarmAbout() throws IOException {
        super("about.fxml", Modality.APPLICATION_MODAL);
        stage.setTitle(String.format("About %s", Resources.NAME));
        headerLabel.setText(String.format("%s v%s", Resources.NAME, Resources.VERSION));
        contentLabel.setText(Resources.NOTICE);
    }

    /**
     * Show about dialog in a blocking way.
     */
    public void showAndWait() {
        positionStage();
        stage.showAndWait();
    }
}
