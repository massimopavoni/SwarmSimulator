package it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui.control;

import it.unicam.cs.massimopavoni.swarmsimulator.simulator.Resources;
import it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui.GUIApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

/**
 * Class representing a swarm dialog control.
 */
public abstract class SwarmDialog {
    /**
     * Swarm dialog stage.
     */
    protected final Stage stage = new Stage();
    /**
     * Swarm dialog owner.
     */
    private Window owner;
    /**
     * Swarm dialog vertical box.
     */
    @FXML
    private VBox vbox;

    /**
     * Constructor for swarm dialog.
     *
     * @param fxmlFile FXML file to load
     * @param modality modality of the dialog
     * @throws IOException if FXML file cannot be loaded
     */
    protected SwarmDialog(String fxmlFile, Modality modality) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource(fxmlFile));
        fxmlLoader.setController(this);
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.initModality(modality);
        stage.getIcons().add(Resources.LOGO);
    }

    /**
     * Initialize swarm dialog owner.
     *
     * @param newOwner new owner
     */
    public void initOwner(Window newOwner) {
        owner = newOwner;
        stage.initOwner(owner);
    }

    /**
     * Position swarm dialog in the middle of the owner.
     */
    protected void positionStage() {
        vbox.applyCss();
        vbox.layout();
        Scene ownerScene = owner.getScene();
        double dialogWidth = vbox.prefWidth(-1);
        stage.setX(owner.getX() + (ownerScene.getWidth() / 2.0) - (dialogWidth / 2.0));
        stage.setY(owner.getY() + ownerScene.getY() / 2.0 + (ownerScene.getHeight() / 2.0) -
                (vbox.prefHeight(dialogWidth) / 2.0));
    }

    /**
     * Ok button action event handler.
     */
    @FXML
    private void okButtonAction() {
        stage.close();
    }
}
