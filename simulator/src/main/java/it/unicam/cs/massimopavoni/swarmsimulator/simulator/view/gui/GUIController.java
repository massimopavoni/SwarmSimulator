package it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui;

import it.unicam.cs.massimopavoni.swarmsimulator.simulator.Resources;
import it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.ErrorType;
import it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui.control.NodeFocusListener;
import it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui.control.SwarmAbout;
import it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui.control.SwarmAlert;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.HiveMind;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.HiveMindException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmProperties;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmState;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.ShapeFactory;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.ShapeType;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.SwarmShapeFactory;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives.SwarmDirectiveFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;

/**
 * Class representing the gui view, and acting as main controller for JavaFX application.
 */
public final class GUIController implements Initializable {
    /**
     * Main application stage.
     */
    private final Stage stage;
    /**
     * Alert dialog for errors.
     */
    private final SwarmAlert errorAlert;
    /**
     * Application about dialog.
     */
    private final SwarmAbout aboutDialog;
    /**
     * Shape factory instance.
     */
    private final ShapeFactory shapeFactory;
    /**
     * List of focusable nodes.
     */
    private final List<Node> focusableNodes;
    /**
     * Swarm hive mind instance.
     */
    private HiveMind hiveMind;
    /**
     * Help label for GUI elements information.
     */
    @FXML
    private Label help;
    /**
     * Help menu item for author account.
     */
    @FXML
    private MenuItem helpAuthor;
    /**
     * Text area for domain file path.
     */
    @FXML
    private TextArea domainPath;
    /**
     * Text area for strategy file path.
     */
    @FXML
    private TextArea strategyPath;
    /**
     * Text area for domain file visualization.
     */
    @FXML
    private TextArea domainText;
    /**
     * Text area for strategy file visualization.
     */
    @FXML
    private TextArea strategyText;
    /**
     * Spinner for number of drones.
     */
    @FXML
    private Spinner<Integer> dronesSpinner;
    /**
     * Choice box for drones spawn shape.
     */
    @FXML
    private ChoiceBox<String> spawnShape;
    /**
     * Text area for shape arguments.
     */
    @FXML
    private TextArea shapeArgs;
    /**
     * Swarm chart for simulation.
     */
    @FXML
    private XYChart<Double, Double> swarmChart;

    /**
     * Constructor for gui, initializing shape and directive factories, application stage and application controls.
     *
     * @param stage main application stage
     * @throws IOException if FXML files of additional windows cannot be loaded
     */
    GUIController(Stage stage) throws IOException {
        this.stage = stage;
        this.stage.setOnShowing(event -> onShowing());
        this.stage.setOnShown(event -> onShown());
        errorAlert = new SwarmAlert(AlertType.ERROR);
        shapeFactory = new SwarmShapeFactory();
        aboutDialog = new SwarmAbout();
        focusableNodes = new ArrayList<>();
        SwarmState.initializeParsers(shapeFactory, new SwarmDirectiveFactory());
    }

    /**
     * Controller initialization method.
     *
     * @param location  url location
     * @param resources resource bundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        helpAuthor.setText(Resources.AUTHOR);
        spawnShape.setItems(FXCollections.observableList(
                Arrays.stream(ShapeType.values()).map(ShapeType::toString).toList()));
    }

    /**
     * On showing event handler for main application stage.
     */
    private void onShowing() {
        errorAlert.initOwner(stage);
        aboutDialog.initOwner(stage);
    }

    /**
     * On shown event handler for main application stage.
     */
    private void onShown() {
        try {
            Properties helpProperties = new Properties();
            helpProperties.load(Objects.requireNonNull(
                    GUIApplication.class.getResourceAsStream("help.properties")));
            bindFocusChangeHelp(helpProperties);
        } catch (IOException e) {
            errorAlert.showAndWait(ErrorType.ERROR, e);
        }
    }

    /**
     * Get all nodes of the scene and bind help text and focus change listener.
     *
     * @param helpProperties help texts properties
     */
    private void bindFocusChangeHelp(Properties helpProperties) {
        addAllDescendants(stage.getScene().getRoot(), focusableNodes, Node::isFocusTraversable);
        focusableNodes.forEach(n -> {
            n.setAccessibleHelp(helpProperties.getProperty(n.getId()));
            n.focusedProperty().addListener(new NodeFocusListener(help, n));
        });
    }

    /**
     * Get filtered descendants of a parent node recursively.
     *
     * @param parent parent node
     * @param nodes  list of nodes to add descendants to
     * @param filter filter predicate for nodes to add
     */
    private void addAllDescendants(Parent parent, List<Node> nodes, Predicate<Node> filter) {
        parent.getChildrenUnmodifiable().forEach(c -> {
            if (filter.test(c))
                nodes.add(c);
            if (c instanceof Parent p)
                addAllDescendants(p, nodes, filter);
        });
    }

    /**
     * Pseudo-event handler used only for operations done after the shown event is fully completed.
     */
    void completeInitialization() {
        try {
            SwarmProperties.initialize();
        } catch (HiveMindException e) {
            errorAlert.showAndWait(ErrorType.FATAL, e);
        }
        dronesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                1, SwarmProperties.maxDronesNumber(), 1));
    }

    /**
     * File menu quit action event handler.
     */
    @FXML
    private void fileQuitAction() {
        stage.close();
    }

    /**
     * Help menu GitHub action event handler.
     */
    @FXML
    private void helpGitHubAction() {
        GUIApplication.openURI(Resources.GITHUB);
    }

    /**
     * Help menu author action event handler.
     */
    @FXML
    private void helpAuthorAction() {
        GUIApplication.openURI(Resources.ACCOUNT);
    }

    /**
     * Help menu license action event handler.
     */
    @FXML
    private void helpLicenseAction() {
        GUIApplication.openURI(Resources.LICENSE);
    }

    /**
     * Help menu about action event handler.
     */
    @FXML
    private void helpAboutAction() {
        aboutDialog.showAndWait();
    }

    /**
     * Drones number spinner scroll event handler.
     *
     * @param event scroll event
     */
    @FXML
    private void dronesSpinnerScroll(ScrollEvent event) {
        int unit = 1;
        if (event.isAltDown())
            unit *= 10;
        if (event.isControlDown())
            unit *= 100;
        if (event.getDeltaY() > 0) {
            dronesSpinner.increment(unit);
            event.consume();
        } else if (event.getDeltaY() < 0) {
            dronesSpinner.decrement(unit);
            event.consume();
        }
    }

    /**
     * Spawn shape choice box scroll event handler.
     *
     * @param event scroll event
     */
    @FXML
    private void spawnShapeScroll(ScrollEvent event) {
        if (event.getDeltaY() > 0) {
            spawnShape.setValue(spawnShape.getItems().get(
                    (spawnShape.getItems().indexOf(spawnShape.getValue()) + 1)
                            % spawnShape.getItems().size()));
            event.consume();
        } else if (event.getDeltaY() < 0) {
            spawnShape.setValue(spawnShape.getItems().get(
                    (spawnShape.getItems().indexOf(spawnShape.getValue()) - 1 +
                            spawnShape.getItems().size()) % spawnShape.getItems().size()));

            event.consume();
        }
    }

    /**
     * Shape arguments text area key pressed event handler.
     *
     * @param event key event
     */
    @FXML
    private void shapeArgsKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.TAB)) {
            shapeArgs.setText(shapeArgs.getText().replace("\t", ""));
            focusableNodes.get((focusableNodes.indexOf(shapeArgs) +
                    (event.isShiftDown() ? focusableNodes.size() - 1 : 1))
                    % focusableNodes.size()).requestFocus();
        }
    }
}
