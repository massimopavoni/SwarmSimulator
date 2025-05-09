package it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui;

import it.unicam.cs.massimopavoni.swarmsimulator.simulator.Resources;
import it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.ErrorType;
import it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui.control.*;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.HiveMind;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.HiveMindException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmProperties;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmState;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Position;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.parser.DomainParserException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.*;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives.SwarmDirectiveFactory;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.parser.StrategyParserException;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import org.controlsfx.control.ToggleSwitch;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

/**
 * Class representing the gui view, and acting as main controller for JavaFX application.
 */
public final class GUIController implements Initializable {
    //region Fields
    //------------------------------------------------------------------------------------------------
    /**
     * Shape factory instance.
     */
    private final ShapeFactory shapeFactory;
    /**
     * Main application stage.
     */
    private final Stage stage;
    /**
     * Alert dialog for errors.
     */
    private final SwarmAlert errorAlert;
    /**
     * Application controls dialog.
     */
    private final SwarmControls controlsDialog;
    /**
     * Application about dialog.
     */
    private final SwarmAbout aboutDialog;
    /**
     * List of focusable nodes.
     */
    private final List<Node> focusableNodes;
    /**
     * Swarm domain file property.
     */
    private final Property<File> domainFileProperty;
    /**
     * Swarm strategy file property.
     */
    private final Property<File> strategyFileProperty;
    /**
     * File chooser for swarm files.
     */
    FileChooser swarmFileChooser;
    //endregion

    //region JavaFX fields
    //------------------------------------------------------------------------------------------------
    /**
     * Swarm hive mind.
     */
    private HiveMind hiveMind;
    /**
     * Help menu item for author account.
     */
    @FXML
    private MenuItem helpAuthorMenuItem;
    /**
     * Button for simulation step forward.
     */
    @FXML
    private Button stepForwardButton;
    /**
     * Button for simulation step play.
     */
    @FXML
    private Button stepPlayButton;
    /**
     * Label for simulation step count.
     */
    @FXML
    private Label stepCountLabel;
    /**
     * Spinner for simulation period.
     */
    @FXML
    private Spinner<Integer> simulationPeriodSpinner;
    /**
     * Label for simulation frequency.
     */
    @FXML
    private Label simulationFrequencyLabel;
    /**
     * Button for chart view move up.
     */
    @FXML
    private Button moveUpButton;
    /**
     * Button for chart view move down.
     */
    @FXML
    private Button moveDownButton;
    /**
     * Button for chart view move left.
     */
    @FXML
    private Button moveLeftButton;
    /**
     * Button for chart view move down.
     */
    @FXML
    private Button moveRightButton;
    /**
     * Button for chart view move reset.
     */
    @FXML
    private Button moveResetButton;
    /**
     * Button for chart view zoom in.
     */
    @FXML
    private Button zoomInButton;
    /**
     * Button for chart view zoom out.
     */
    @FXML
    private Button zoomOutButton;
    /**
     * Button for chart view zoom reset.
     */
    @FXML
    private Button zoomResetButton;
    /**
     * Button for domain file choose.
     */
    @FXML
    private Button domainFileChooseButton;
    /**
     * Text area for domain file path.
     */
    @FXML
    private TextArea domainPathTextArea;
    /**
     * Button for strategy file choose.
     */
    @FXML
    private Button strategyFileChooseButton;
    /**
     * Text area for strategy file path.
     */
    @FXML
    private TextArea strategyPathTextArea;
    /**
     * Text area for domain file visualization.
     */
    @FXML
    private TextArea domainTextArea;
    /**
     * Text area for strategy file visualization.
     */
    @FXML
    private TextArea strategyTextArea;
    /**
     * Spinner for drones number.
     */
    @FXML
    private Spinner<Integer> dronesNumberSpinner;
    /**
     * Choice box for drones spawn shape.
     */
    @FXML
    private ChoiceBox<String> spawnShapeChoiceBox;
    /**
     * Text area for shape arguments.
     */
    @FXML
    private TextArea shapeArgsTextArea;
    /**
     * Toggle switch for on boundary drones spawn.
     */
    @FXML
    private ToggleSwitch onBoundaryToggleSwitch;
    /**
     * Button for swarm reset.
     */
    @FXML
    private Button resetButton;
    /**
     * Button for swarm spawning.
     */
    @FXML
    private Button spawnButton;
    /**
     * Swarm chart for simulation.
     */
    @FXML
    private XYChart<Number, Number> swarmChart;
    /**
     * Help label for GUI elements information.
     */
    @FXML
    private Label helpLabel;
    /**
     * Controller for swarm chart actions.
     */
    private SwarmChartController swarmChartController;
    //endregion

    //region Constructor
    //------------------------------------------------------------------------------------------------

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
        controlsDialog = new SwarmControls();
        aboutDialog = new SwarmAbout();
        focusableNodes = new ArrayList<>();
        swarmFileChooser = new FileChooser();
        domainFileProperty = new SimpleObjectProperty<>();
        strategyFileProperty = new SimpleObjectProperty<>();
        SwarmState.initializeParsers(shapeFactory, new SwarmDirectiveFactory());
    }
    //endregion

    //region Initialization methods
    //------------------------------------------------------------------------------------------------

    /**
     * Controller initialization method.
     *
     * @param location  url location
     * @param resources resource bundle
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        helpAuthorMenuItem.setText(Resources.AUTHOR);
        swarmFileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("*.swarm", "*.swarm"),
                new ExtensionFilter("*.txt", "*.txt"),
                new ExtensionFilter("All Files", "*.*"));
        spawnShapeChoiceBox.setItems(FXCollections.observableList(
                Arrays.stream(ShapeType.values()).map(ShapeType::toString).toList()));
        spawnShapeChoiceBox.setValue(spawnShapeChoiceBox.getItems().getFirst());
    }

    /**
     * On showing event handler for main application stage.
     */
    private void onShowing() {
        errorAlert.initOwner(stage);
        aboutDialog.initOwner(stage);
        controlsDialog.initOwner(stage);
        // Prevent alt key from focusing menu bar, as it's a modifier for other controls
        stage.getScene().addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.isAltDown())
                event.consume();
        });
    }

    /**
     * On shown event handler for main application stage.
     */
    private void onShown() {
        try {
            Properties helpProperties = new Properties();
            helpProperties.load(Objects.requireNonNull(
                    GUIApplication.class.getResourceAsStream("help.properties")));
            addFocusChangeListeners(helpProperties);
            addSimulationPeriodChangeListener();
            addFileChangeListener(domainFileProperty, domainPathTextArea,
                    () -> strategyFileProperty != null && !shapeArgsTextArea.getText().isBlank());
            addFileChangeListener(strategyFileProperty, strategyPathTextArea,
                    () -> domainFileProperty.getValue() != null && !shapeArgsTextArea.getText().isBlank());
            addShapeArgsChangeListener();
        } catch (IOException e) {
            errorAlert.showAndWait(ErrorType.ERROR, "While loading help text properties.", e);
        }
    }

    /**
     * Pseudo-event handler used only for operations done after the shown event is fully completed.
     */
    void completeInitialization() {
        swarmFileChooser.setInitialDirectory(new File(SwarmProperties.DEFAULT_SWARM_FOLDER));
        try {
            SwarmProperties.initialize();
            dronesNumberSpinner.setValueFactory(new IntegerSpinnerValueFactory(
                    1, SwarmProperties.maxDronesNumber(), 1, 1));
            swarmChartController = new SwarmChartController(swarmChart, stepCountLabel);
            addSimulatingListener();
            Thread.currentThread().setUncaughtExceptionHandler((thread, e) ->
                    handleSpawnException((Exception) e));
        } catch (HiveMindException e) {
            errorAlert.showAndWait(ErrorType.FATAL, "While initializing swarm properties.", e);
        }
    }
    //endregion

    //region Listener methods
    //------------------------------------------------------------------------------------------------

    /**
     * Get all nodes of the scene and add focus change listeners.
     *
     * @param helpProperties help texts properties
     */
    private void addFocusChangeListeners(Properties helpProperties) {
        addAllDescendants(stage.getScene().getRoot(), focusableNodes, Node::isFocusTraversable);
        focusableNodes.forEach(n -> {
            n.setAccessibleHelp(helpProperties.getProperty(n.getId()));
            n.focusedProperty().addListener(new NodeFocusListener(helpLabel, n));
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
     * Add simulating listener.
     */
    private void addSimulatingListener() {
        swarmChartController.simulating.addListener(
                (observable, oldValue, newValue) -> {
                    setSimulatingButtonsDisable(newValue);
                    swarmChartController.toggleSimulating(
                            simulationPeriodSpinner.getValue().doubleValue());
                });
    }

    /**
     * Add simulation period change listener.
     */
    private void addSimulationPeriodChangeListener() {
        simulationPeriodSpinner.valueProperty().addListener(
                (observable, oldValue, newValue) -> simulationPeriodChange(newValue));
    }

    /**
     * Simulation period change listener.
     *
     * @param value value to update with
     */
    private void simulationPeriodChange(Integer value) {
        if (value != null && value != 0) {
            double frequency = 1 / (value / 1000.0);
            if (frequency < 1)
                simulationFrequencyLabel.setText(String.format("%.2f mHz", frequency * 1000));
            else
                simulationFrequencyLabel.setText(String.format("%.2f Hz", frequency));
        }
    }

    /**
     * Add file change listener.
     *
     * @param property     property to add listener to
     * @param path         text area to set file path to
     * @param buttonEnable button enable condition
     */
    private void addFileChangeListener(Property<File> property, TextArea path, BooleanSupplier buttonEnable) {
        property.addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                path.setText("");
                spawnButton.setDisable(true);
            } else {
                path.setText(newValue.getAbsolutePath());
                if (buttonEnable.getAsBoolean())
                    spawnButton.setDisable(false);
            }
        });
    }

    /**
     * Add shape args change listener.
     */
    private void addShapeArgsChangeListener() {
        shapeArgsTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isBlank())
                spawnButton.setDisable(true);
            else if (!domainPathTextArea.getText().isBlank() && !strategyPathTextArea.getText().isBlank())
                spawnButton.setDisable(false);
        });
    }
    //endregion

    //region Menu event handlers methods
    //------------------------------------------------------------------------------------------------

    /**
     * File quit menu item action event handler.
     */
    @FXML
    private void fileQuitMenuItemAction() {
        stage.close();
    }

    /**
     * Help controls menu item action event handler.
     */
    @FXML
    private void helpControlsMenuItemAction() {
        controlsDialog.show();
    }

    /**
     * Help about menu item action event handler.
     */
    @FXML
    private void helpAboutMenuItemAction() {
        aboutDialog.showAndWait();
    }

    /**
     * Help GitHub menu item action event handler.
     */
    @FXML
    private void helpGitHubMenuItemAction() {
        GUIApplication.openURI(Resources.GITHUB);
    }

    /**
     * Help author menu item action event handler.
     */
    @FXML
    private void helpAuthorMenuItemAction() {
        GUIApplication.openURI(Resources.ACCOUNT);
    }

    /**
     * Help license menu item action event handler.
     */
    @FXML
    private void helpLicenseMenuItemAction() {
        GUIApplication.openURI(Resources.LICENSE);
    }
    //endregion

    //region Simulation event handlers methods
    //------------------------------------------------------------------------------------------------

    /**
     * Step forward button action event handler.
     *
     * @param event action event
     */
    @FXML
    private void stepForwardButtonAction(ActionEvent event) {
        swarmChartController.swarmStep();
        event.consume();
    }

    /**
     * Step play button action event handler.
     *
     * @param event action event
     */
    @FXML
    private void stepPlayButtonAction(ActionEvent event) {
        swarmChartController.simulating.set(!swarmChartController.simulating.getValue());
        event.consume();
    }

    /**
     * Simulation period spinner scroll event handler.
     *
     * @param event scroll event
     */
    @FXML
    private void simulationPeriodSpinnerScroll(ScrollEvent event) {
        if (event.getDeltaY() > 0) {
            simulationPeriodSpinner.increment(
                    getSpinnerIncrement(event.isAltDown(), event.isControlDown()));
            event.consume();
        } else if (event.getDeltaY() < 0) {
            simulationPeriodSpinner.decrement(
                    getSpinnerIncrement(event.isAltDown(), event.isControlDown()));
            event.consume();
        }
    }

    /**
     * Simulation period spinner key pressed event handler.
     *
     * @param event key event
     */
    @FXML
    private void simulationPeriodSpinnerKeyPressed(KeyEvent event) {
        if (!event.isConsumed()) {
            if (event.getCode().equals(KeyCode.UP)) {
                simulationPeriodSpinner.increment(
                        getSpinnerIncrement(event.isAltDown(), event.isControlDown()));
                event.consume();
            } else if (event.getCode().equals(KeyCode.DOWN)) {
                simulationPeriodSpinner.decrement(
                        getSpinnerIncrement(event.isAltDown(), event.isControlDown()));
                event.consume();
            }
        }
    }
    //endregion

    //region Chart view event handlers methods
    //------------------------------------------------------------------------------------------------

    /**
     * Move up button mouse clicked event handler.
     *
     * @param event mouse event
     */
    @FXML
    private void moveUpButtonMouseClicked(MouseEvent event) {
        swarmChartController.moveView(
                0, 1, event.isShiftDown(), event.isControlDown());
        event.consume();
    }

    /**
     * Move down button mouse clicked event handler.
     *
     * @param event mouse event
     */
    @FXML
    private void moveDownButtonMouseClicked(MouseEvent event) {
        swarmChartController.moveView(
                1, 1, event.isShiftDown(), event.isControlDown());
        event.consume();
    }

    /**
     * Move left button mouse clicked event handler.
     *
     * @param event mouse event
     */
    @FXML
    private void moveLeftButtonMouseClicked(MouseEvent event) {
        swarmChartController.moveView(
                2, 1, event.isShiftDown(), event.isControlDown());
        event.consume();
    }

    /**
     * Move right button mouse clicked event handler.
     *
     * @param event mouse event
     */
    @FXML
    private void moveRightButtonMouseClicked(MouseEvent event) {
        swarmChartController.moveView(
                3, 1, event.isShiftDown(), event.isControlDown());
        event.consume();
    }

    /**
     * Move reset button mouse clicked event handler.
     *
     * @param event mouse event
     */
    @FXML
    private void moveResetButtonMouseClicked(MouseEvent event) {
        swarmChartController.moveViewToCenter(event.isShiftDown());
        event.consume();
    }

    /**
     * Zoom in button mouse clicked event handler.
     *
     * @param event mouse event
     */
    @FXML
    private void zoomInButtonMouseClicked(MouseEvent event) {
        swarmChartController.zoomView(true, event.isShiftDown(), event.isControlDown());
        event.consume();
    }

    /**
     * Zoom out button mouse clicked event handler.
     *
     * @param event mouse event
     */
    @FXML
    private void zoomOutButtonMouseClicked(MouseEvent event) {
        swarmChartController.zoomView(false, event.isShiftDown(), event.isControlDown());
        event.consume();
    }

    /**
     * Zoom reset button mouse clicked event handler.
     *
     * @param event mouse event
     */
    @FXML
    private void zoomResetButtonMouseClicked(MouseEvent event) {
        swarmChartController.zoomViewToCenter(event.isShiftDown());
        event.consume();
    }

    /**
     * Swarm chart scroll event handler.
     *
     * @param event scroll event
     */
    @FXML
    private void swarmChartScroll(ScrollEvent event) {
        double deltaY = event.getDeltaY();
        double deltaX = event.getDeltaX();
        if (event.isAltDown()) {
            if (event.getDeltaY() != 0) {
                swarmChartController.translateAxes(swarmChartController.getMousePosition(event));
                swarmChartController.zoomView(deltaY > 0, false, event.isControlDown());
                event.consume();
            } else if (event.getDeltaX() != 0) {
                swarmChartController.translateAxes(swarmChartController.getMousePosition(event));
                swarmChartController.zoomView(deltaX > 0, true, event.isControlDown());
                event.consume();
            }
        } else {
            if (deltaY > 0) {
                swarmChartController.moveView(0, 1, false, event.isControlDown());
                event.consume();
            } else if (deltaY < 0) {
                swarmChartController.moveView(1, 1, false, event.isControlDown());
                event.consume();
            } else if (deltaX < 0) {
                swarmChartController.moveView(2, 1, false, event.isControlDown());
                event.consume();
            } else if (deltaX > 0) {
                swarmChartController.moveView(3, 1, false, event.isControlDown());
                event.consume();
            }
        }
    }
    //endregion

    //region Simulation additional methods
    //------------------------------------------------------------------------------------------------

    /**
     * Set buttons disable state during simulation.
     *
     * @param disable disable flag
     */
    private void setSimulatingButtonsDisable(boolean disable) {
        stepForwardButton.setDisable(disable);
        simulationPeriodSpinner.setDisable(disable);
        domainFileChooseButton.setDisable(disable);
        strategyFileChooseButton.setDisable(disable);
        resetButton.setDisable(disable);
        spawnButton.setDisable(disable);
    }

    /**
     * Enable simulation and view controls.
     */
    private void enableSimulationView() {
        setSimulationViewButtonsDisable(false);
        stage.getScene().addEventHandler(KeyEvent.KEY_PRESSED,
                swarmChartController.getKeyPressedEventHandler());
        Position simulationPeriodRange = swarmChartController.getSimulationPeriodRange();
        int shortestSimulationPeriod = (int) Math.ceil(simulationPeriodRange.x());
        int longestSimulationPeriod = (int) Math.floor(simulationPeriodRange.y());
        simulationPeriodSpinner.setValueFactory(new SwarmPeriodSpinnerValueFactory(
                shortestSimulationPeriod, longestSimulationPeriod,
                shortestSimulationPeriod, 1));
        simulationPeriodSpinner.getEditor().setText(
                simulationPeriodSpinner.getValueFactory().getConverter()
                        .toString(shortestSimulationPeriod));
        simulationPeriodChange(simulationPeriodSpinner.getValue());
        stepCountLabel.setText("0 steps");
    }

    /**
     * Disable simulation and view controls.
     */
    private void disableSimulationView() {
        setSimulationViewButtonsDisable(true);
        stage.getScene().removeEventHandler(KeyEvent.KEY_PRESSED,
                swarmChartController.getKeyPressedEventHandler());
        hiveMind = null;
        swarmChartController.setSources(null);
        stepCountLabel.setText("steps");
        simulationPeriodSpinner.getEditor().clear();
        simulationFrequencyLabel.setText("Hz");
        domainTextArea.setText("");
        strategyTextArea.setText("");
    }

    /**
     * Set simulation and view buttons disable state.
     *
     * @param disable disable flag
     */
    private void setSimulationViewButtonsDisable(boolean disable) {
        stepForwardButton.setDisable(disable);
        stepPlayButton.setDisable(disable);
        simulationPeriodSpinner.setDisable(disable);
        moveUpButton.setDisable(disable);
        moveDownButton.setDisable(disable);
        moveLeftButton.setDisable(disable);
        moveRightButton.setDisable(disable);
        moveResetButton.setDisable(disable);
        zoomInButton.setDisable(disable);
        zoomOutButton.setDisable(disable);
        zoomResetButton.setDisable(disable);
        swarmChart.setDisable(disable);
    }
    //endregion

    //region Swarm event handlers methods
    //------------------------------------------------------------------------------------------------

    /**
     * Domain file choose button action event handler.
     */
    @FXML
    private void domainFileChooseButtonAction(ActionEvent event) {
        swarmFileChooser.setTitle("Choose swarm domain file");
        swarmFileChooser.setSelectedExtensionFilter(swarmFileChooser.getExtensionFilters().getFirst());
        domainFileProperty.setValue(swarmFileChooser.showOpenDialog(stage));
        event.consume();
    }

    /**
     * Strategy file choose button action event handler.
     */
    @FXML
    private void strategyFileChooseButtonAction(ActionEvent event) {
        swarmFileChooser.setTitle("Choose swarm strategy file");
        swarmFileChooser.setSelectedExtensionFilter(swarmFileChooser.getExtensionFilters().getFirst());
        strategyFileProperty.setValue(swarmFileChooser.showOpenDialog(stage));
        event.consume();
    }

    /**
     * Drones number spinner scroll event handler.
     *
     * @param event scroll event
     */
    @FXML
    private void dronesNumberSpinnerScroll(ScrollEvent event) {
        if (event.getDeltaY() > 0) {
            dronesNumberSpinner.increment(
                    getSpinnerIncrement(event.isAltDown(), event.isControlDown()));
            event.consume();
        } else if (event.getDeltaY() < 0) {
            dronesNumberSpinner.decrement(
                    getSpinnerIncrement(event.isAltDown(), event.isControlDown()));
            event.consume();
        }
    }

    /**
     * Drones number spinner key pressed event handler.
     *
     * @param event key event
     */
    @FXML
    private void dronesNumberSpinnerKeyPressed(KeyEvent event) {
        if (!event.isConsumed()) {
            if (event.getCode().equals(KeyCode.UP)) {
                dronesNumberSpinner.increment(
                        getSpinnerIncrement(event.isAltDown(), event.isControlDown()));
                event.consume();
            } else if (event.getCode().equals(KeyCode.DOWN)) {
                dronesNumberSpinner.decrement(
                        getSpinnerIncrement(event.isAltDown(), event.isControlDown()));
                event.consume();
            }
        }
    }

    /**
     * Spawn shape choice box scroll event handler.
     *
     * @param event scroll event
     */
    @FXML
    private void spawnShapeChoiceBoxScroll(ScrollEvent event) {
        if (event.getDeltaY() > 0) {
            spawnShapeChoiceBoxSelection(false);
            event.consume();
        } else if (event.getDeltaY() < 0) {
            spawnShapeChoiceBoxSelection(true);
            event.consume();
        }
    }

    /**
     * Spawn shape choice box key pressed event handler.
     *
     * @param event key event
     */
    @FXML
    private void spawnShapeChoiceBoxKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.UP)) {
            spawnShapeChoiceBoxSelection(false);
            event.consume();
        } else if (event.getCode().equals(KeyCode.DOWN)) {
            spawnShapeChoiceBoxSelection(true);
            event.consume();
        }
    }

    /**
     * Shape arguments text area key pressed event handler.
     *
     * @param event key event
     */
    @FXML
    private void shapeArgsTextAreaKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.TAB)) {
            shapeArgsTextArea.setText(shapeArgsTextArea.getText().replace("\t", ""));
            focusableNodes.get((focusableNodes.indexOf(shapeArgsTextArea) +
                    (event.isShiftDown() ? focusableNodes.size() - 1 : 1))
                    % focusableNodes.size()).requestFocus();
            event.consume();
        }
    }

    /**
     * Other text areas key pressed event handler, to forward event to simulation, if enabled.
     *
     * @param event key event
     */
    @FXML
    private void textAreaKeyPressed(KeyEvent event) {
        if (!swarmChart.isDisabled())
            swarmChartController.getKeyPressedEventHandler().handle(event);
    }

    /**
     * On boundary toggle switch mouse clicked event handler.
     */
    @FXML
    private void onBoundaryToggleSwitchMouseClicked(MouseEvent event) {
        onBoundaryToggleSwitch.requestFocus();
        event.consume();
    }

    /**
     * Reset button action event handler.
     */
    @FXML
    private void resetButtonAction(ActionEvent event) {
        domainFileProperty.setValue(null);
        strategyFileProperty.setValue(null);
        dronesNumberSpinner.getValueFactory().setValue(1);
        spawnShapeChoiceBox.setValue(spawnShapeChoiceBox.getItems().getFirst());
        shapeArgsTextArea.setText("");
        onBoundaryToggleSwitch.setSelected(false);
        disableSimulationView();
        event.consume();
    }

    /**
     * Spawn button action event handler.
     */
    @FXML
    private void spawnButtonAction(ActionEvent event) {
        try {
            Shape spawnShape = shapeFactory.createShape(
                    ShapeType.fromString(spawnShapeChoiceBox.getValue().toLowerCase()),
                    SwarmUtils.toDoubleArray(shapeArgsTextArea.getText().trim().split(" "), 0));
            hiveMind = new HiveMind(new SwarmState(
                    domainFileProperty.getValue(), strategyFileProperty.getValue(),
                    dronesNumberSpinner.getValue(), spawnShape,
                    onBoundaryToggleSwitch.isSelected()));
            swarmChartController.setSources(hiveMind);
            domainTextArea.setText(Files.readString(domainFileProperty.getValue().toPath()));
            strategyTextArea.setText(Files.readString(strategyFileProperty.getValue().toPath()));
            enableSimulationView();
        } catch (Exception e) {
            disableSimulationView();
            handleSpawnException(e);
        } finally {
            event.consume();
        }
    }
    //endregion

    //region Swarm additional methods
    //------------------------------------------------------------------------------------------------

    /**
     * Calculate spinner increment, based on modifiers' flags.
     *
     * @param firstModifier  first modifier enabled flag
     * @param secondModifier second modifier enabled flag
     */
    private int getSpinnerIncrement(boolean firstModifier, boolean secondModifier) {
        int unit = 1;
        if (firstModifier)
            unit *= 10;
        if (secondModifier)
            unit *= 100;
        return unit;
    }

    /**
     * Spawn shape choice box selection with modifiers.
     *
     * @param decrease decrease flag
     */
    private void spawnShapeChoiceBoxSelection(boolean decrease) {
        List<String> items = spawnShapeChoiceBox.getItems();
        if (decrease)
            spawnShapeChoiceBox.setValue(items.get((items.indexOf(spawnShapeChoiceBox.getValue()) + 1)
                    % items.size()));
        else
            spawnShapeChoiceBox.setValue(items.get((items.indexOf(spawnShapeChoiceBox.getValue()) - 1 + items.size())
                    % items.size()));
    }

    /**
     * Spawn button action errors handler.
     *
     * @param e exception
     */
    private void handleSpawnException(Exception e) {
        switch (e) {
            case NumberFormatException nfe ->
                    errorAlert.showAndWait(ErrorType.ERROR, "While converting shape arguments.", nfe);
            case ShapeException se -> errorAlert.showAndWait(ErrorType.ERROR, "While creating shape.", se);
            case DomainParserException dpe -> errorAlert.showAndWait(ErrorType.ERROR, "While creating domain.", dpe);
            case StrategyParserException spe ->
                    errorAlert.showAndWait(ErrorType.ERROR, "While creating strategy.", spe);
            case HiveMindException hme ->
                    errorAlert.showAndWait(ErrorType.FATAL, "While initializing swarm properties.", hme);
            case SwarmException se -> errorAlert.showAndWait(ErrorType.ERROR, "While creating swarm.", se);
            case IOException ioe -> errorAlert.showAndWait(ErrorType.ERROR, "While reading swarm files.", ioe);
            default -> errorAlert.showAndWait(ErrorType.ERROR, "While spawning swarm.", e);
        }
    }
    //endregion
}
