package it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui;

import it.unicam.cs.massimopavoni.swarmsimulator.simulator.Resources;
import it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.ErrorType;
import it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui.control.NodeFocusListener;
import it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui.control.SwarmAbout;
import it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui.control.SwarmAlert;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.HiveMind;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.HiveMindException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmProperties;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmState;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.parser.DomainParserException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.ShapeException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.ShapeFactory;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.ShapeType;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.SwarmShapeFactory;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives.SwarmDirectiveFactory;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.parser.StrategyParserException;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
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
     * Swarm hive mind property.
     */
    private final Property<HiveMind> hiveMindProperty;
    /**
     * File chooser for swarm files.
     */
    FileChooser swarmFileChooser;
    /**
     * Help label for GUI elements information.
     */
    @FXML
    private Label helpLabel;
    /**
     * Help menu item for author account.
     */
    @FXML
    private MenuItem helpAuthorMenuItem;
    /**
     * Text area for domain file path.
     */
    @FXML
    private TextArea domainPathTextArea;
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
     * Controller for swarm chart actions.
     */
    private SwarmChartController swarmChartController;

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
        swarmFileChooser = new FileChooser();
        domainFileProperty = new SimpleObjectProperty<>();
        strategyFileProperty = new SimpleObjectProperty<>();
        hiveMindProperty = new SimpleObjectProperty<>();
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
        helpAuthorMenuItem.setText(Resources.AUTHOR);
        swarmFileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("*.swarm", "*.swarm"),
                new ExtensionFilter("*.txt", "*.txt"),
                new ExtensionFilter("All Files", "*.*"));
        spawnShapeChoiceBox.setItems(FXCollections.observableList(
                Arrays.stream(ShapeType.values()).map(ShapeType::toString).toList()));
        spawnShapeChoiceBox.setValue(spawnShapeChoiceBox.getItems().get(0));
        swarmChartController = new SwarmChartController(swarmChart);
    }

    /**
     * On showing event handler for main application stage.
     */
    private void onShowing() {
        errorAlert.initOwner(stage);
        aboutDialog.initOwner(stage);
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

    /**
     * Pseudo-event handler used only for operations done after the shown event is fully completed.
     */
    void completeInitialization() {
        swarmFileChooser.setInitialDirectory(new File(SwarmProperties.DEFAULT_SWARM_FOLDER));
        try {
            SwarmProperties.initialize();
            dronesNumberSpinner.setValueFactory(new IntegerSpinnerValueFactory(
                    1, SwarmProperties.maxDronesNumber(), 1));
            addHiveMindChangeListener();
        } catch (HiveMindException e) {
            errorAlert.showAndWait(ErrorType.FATAL, "While initializing swarm properties.", e);
        }
    }

    /**
     * Add hive mind change listener.
     */
    private void addHiveMindChangeListener() {
        hiveMindProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                swarmChart.setDisable(true);
                swarmChartController.reset();
                swarmChartController.setSources(null, null);
            } else {
                swarmChart.setDisable(false);
                swarmChartController.setSources(hiveMindProperty.getValue().state().domain(),
                        hiveMindProperty.getValue().state().swarm());
            }
        });
    }

    /**
     * File quit menu item action event handler.
     */
    @FXML
    private void fileQuitMenuItemAction() {
        stage.close();
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

    /**
     * Help about menu item action event handler.
     */
    @FXML
    private void helpAboutMenuItemAction() {
        aboutDialog.showAndWait();
    }

    /**
     * Domain file choose button action event handler.
     */
    @FXML
    private void domainFileChooseButtonAction() {
        swarmFileChooser.setTitle("Choose swarm domain file");
        swarmFileChooser.setSelectedExtensionFilter(swarmFileChooser.getExtensionFilters().get(0));
        domainFileProperty.setValue(swarmFileChooser.showOpenDialog(stage));
    }

    /**
     * Strategy file choose button action event handler.
     */
    @FXML
    private void strategyFileChooseButtonAction() {
        swarmFileChooser.setTitle("Choose swarm strategy file");
        swarmFileChooser.setSelectedExtensionFilter(swarmFileChooser.getExtensionFilters().get(0));
        strategyFileProperty.setValue(swarmFileChooser.showOpenDialog(stage));
    }

    /**
     * Drones number spinner scroll event handler.
     *
     * @param event scroll event
     */
    @FXML
    private void dronesNumberSpinnerScroll(ScrollEvent event) {
        if (event.getDeltaY() > 0) {
            dronesNumberSpinnerSelection(event.isAltDown(), event.isControlDown(), true);
            event.consume();
        } else if (event.getDeltaY() < 0) {
            dronesNumberSpinnerSelection(event.isAltDown(), event.isControlDown(), false);
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
                dronesNumberSpinnerSelection(event.isAltDown(), event.isControlDown(), true);
                event.consume();
            } else if (event.getCode().equals(KeyCode.DOWN)) {
                dronesNumberSpinnerSelection(event.isAltDown(), event.isControlDown(), false);
                event.consume();
            }
        }
    }

    /**
     * Drones number spinner selection with modifiers.
     *
     * @param altDown     alt key down flag
     * @param controlDown control key down flag
     * @param increase    increase flag
     */
    private void dronesNumberSpinnerSelection(boolean altDown, boolean controlDown, boolean increase) {
        int unit = 1;
        if (altDown)
            unit *= 10;
        if (controlDown)
            unit *= 100;
        if (increase)
            dronesNumberSpinner.increment(unit);
        else
            dronesNumberSpinner.decrement(unit);
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
     * On boundary toggle switch mouse clicked event handler.
     */
    @FXML
    private void onBoundaryToggleSwitchMouseClicked() {
        onBoundaryToggleSwitch.requestFocus();
    }

    /**
     * Reset button action event handler.
     */
    @FXML
    private void resetButtonAction() {
        hiveMindProperty.setValue(null);
        domainFileProperty.setValue(null);
        strategyFileProperty.setValue(null);
        dronesNumberSpinner.getValueFactory().setValue(1);
        spawnShapeChoiceBox.setValue(spawnShapeChoiceBox.getItems().get(0));
        shapeArgsTextArea.setText("");
        onBoundaryToggleSwitch.setSelected(false);
        domainTextArea.setText("");
        strategyTextArea.setText("");
        swarmChart.setDisable(true);
    }

    /**
     * Spawn button action event handler.
     */
    @FXML
    private void spawnButtonAction() {
        hiveMindProperty.setValue(null);
        try {
            hiveMindProperty.setValue(new HiveMind(new SwarmState(
                    domainFileProperty.getValue(), strategyFileProperty.getValue(),
                    dronesNumberSpinner.getValue(), shapeFactory.createShape(
                    ShapeType.fromString(spawnShapeChoiceBox.getValue().toLowerCase()),
                    SwarmUtils.toDoubleArray(shapeArgsTextArea.getText().trim().split(" "), 0)),
                    onBoundaryToggleSwitch.isSelected())));
            domainTextArea.setText(Files.readString(domainFileProperty.getValue().toPath()));
            strategyTextArea.setText(Files.readString(strategyFileProperty.getValue().toPath()));
        } catch (Exception e) {
            domainTextArea.setText("");
            strategyTextArea.setText("");
            handleSpawnException(e);
        }
    }

    /**
     * Spawn button action errors handler.
     *
     * @param e exception
     */
    private void handleSpawnException(Exception e) {
        // Would look better with patter matching switch, will come with Java 21 LTS project update
        if (e instanceof NumberFormatException)
            errorAlert.showAndWait(ErrorType.ERROR, "While converting shape arguments.", e);
        else if (e instanceof ShapeException)
            errorAlert.showAndWait(ErrorType.ERROR, "While creating shape.", e);
        else if (e instanceof DomainParserException)
            errorAlert.showAndWait(ErrorType.ERROR, "While creating domain.", e);
        else if (e instanceof StrategyParserException)
            errorAlert.showAndWait(ErrorType.ERROR, "While creating strategy.", e);
        else if (e instanceof HiveMindException)
            errorAlert.showAndWait(ErrorType.FATAL, "While initializing swarm properties.", e);
        else if (e instanceof SwarmException)
            errorAlert.showAndWait(ErrorType.ERROR, "While creating swarm.", e);
        else if (e instanceof IOException)
            errorAlert.showAndWait(ErrorType.ERROR, "While reading swarm files.", e);
        else
            errorAlert.showAndWait(ErrorType.ERROR, "While spawning swarm.", e);
    }
}
