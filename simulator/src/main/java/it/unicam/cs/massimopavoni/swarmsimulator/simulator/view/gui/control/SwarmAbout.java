package it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui.control;

import it.unicam.cs.massimopavoni.swarmsimulator.simulator.Resources;
import it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui.GUIApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

/**
 * Class representing about dialog control.
 */
public final class SwarmAbout {
    /**
     * About dialog stage.
     */
    private final Stage stage = new Stage();
    /**
     * About dialog owner.
     */
    private Window owner;
    /**
     * About dialog vertical box.
     */
    @FXML
    private VBox vbox;
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
        FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource("about.fxml"));
        fxmlLoader.setController(this);
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(Resources.LOGO);
        initAbout();
    }

    /**
     * Initialize about dialog texts.
     */
    private void initAbout() {
        stage.setTitle(String.format("About %s", Resources.NAME));
        headerLabel.setText(String.format("%s v%s", Resources.NAME, Resources.VERSION));
        contentLabel.setText(Resources.NOTICE);
    }

    /**
     * Initialize about dialog owner.
     *
     * @param newOwner new owner
     */
    public void initOwner(Window newOwner) {
        owner = newOwner;
        stage.initOwner(owner);
    }

    /**
     * Show about dialog in a blocking way.
     */
    public void showAndWait() {
        positionStage();
        stage.showAndWait();
    }

    /**
     * Position about dialog in the middle of the owner.
     */
    private void positionStage() {
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
