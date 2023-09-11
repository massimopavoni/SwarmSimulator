package it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.HiveMind;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmProperties;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Position;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.input.GestureEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.function.ToDoubleBiFunction;
import java.util.stream.IntStream;

/**
 * Class representing a controller for the swarm chart actions and simulation.
 */
public final class SwarmChartController {
    //region Constant fields
    //------------------------------------------------------------------------------------------------
    /**
     * Default origin for the axes.
     */
    public static final Position DEFAULT_ORIGIN = new Position(0, 0);
    /**
     * Default span for the x-axis.
     */
    public static final double DEFAULT_X_AXIS_SPAN = 14;
    /**
     * Number of ticks for the y-axis.
     */
    public static final double Y_AXIS_TICK_COUNT = 10;
    /**
     * Move multiplier value for first modifier.
     */
    public static final double MOVE_FIRST_MODIFIER_MULTIPLIER = 2;
    /**
     * Move multiplier value for second modifier.
     */
    public static final double MOVE_SECOND_MODIFIER_MULTIPLIER = 4;
    /**
     * Zoom multiplier value for first modifier.
     */
    public static final double ZOOM_FIRST_MODIFIER_MULTIPLIER = 1.5;
    /**
     * Zoom multiplier value for second modifier.
     */
    public static final double ZOOM_SECOND_MODIFIER_MULTIPLIER = 2;
    /**
     * Default color for the domain regions.
     */
    public static final Paint DEFAULT_REGION_COLOR =
            new Color(0, 204 / 255.0, 28 / 255.0, 0.15);
    /**
     * Default font size for the domain regions' labels.
     */
    public static final Font DEFAULT_REGION_LABEL_FONT = new Font(15);
    /**
     * Default background for the domain regions' panes.
     */
    public static final Background DEFAULT_REGION_PANE_BACKGROUND =
            new Background(new BackgroundFill(null, null, null));
    /**
     * Shortest simulation period formula function, calculates an empirical lower bound in milliseconds.
     */
    public static final ToDoubleBiFunction<Integer, Integer> SHORTEST_SIMULATION_PERIOD =
            (dronesNumber, parallelism) -> Math.max(dronesNumber * 0.05 * 16 / parallelism, 20);
    /**
     * Multiplier for calculating the longest simulation period.
     */
    public static final int LONGEST_SIMULATION_PERIOD_MULTIPLIER = (int) Math.pow(2, 6);
    //endregion

    //region Fields
    //------------------------------------------------------------------------------------------------
    /**
     * Smallest span for the x-axis.
     */
    public final double smallestXAxisSpan;
    /**
     * Biggest span for the x-axis.
     */
    public final double biggestXAxisSpan;
    /**
     * Number axis for X.
     */
    private final NumberAxis xAxis;
    /**
     * Number axis for Y.
     */
    private final NumberAxis yAxis;
    /**
     * Ratio between the x-axis span and the y-axis span.
     */
    private final double xyAxesRatio;
    /**
     * Chart series for the domain regions.
     */
    private final Series<Number, Number> domainSeries;
    /**
     * Chart series for the swarm drones.
     */
    private final Series<Number, Number> swarmSeries;
    /**
     * Additional key pressed event handler for the simulation.
     */
    private final EventHandler<KeyEvent> keyPressedEventHandler;
    /**
     * Step count label.
     */
    private final Label stepCountLabel;
    /**
     * Simulation timeline.
     */
    private final Timeline simulationTimeline;
    /**
     * Simulation running flag property.
     */
    BooleanProperty simulating;
    /**
     * Scaling factor for the domain regions' shapes.
     */
    private double shapeScalingFactor;
    /**
     * Span of the x-axis.
     */
    private double xAxisSpan;
    /**
     * Span of the y-axis.
     */
    private double yAxisSpan;
    /**
     * Tick unit for the axes.
     */
    private double tickUnit;
    /**
     * Hive mind instance.
     */
    private HiveMind hiveMind;
    //endregion

    //region Constructor
    //------------------------------------------------------------------------------------------------

    /**
     * Constructor for a swarm chart controller.
     *
     * @param chart          chart instance
     * @param stepCountLabel step count label
     */
    public SwarmChartController(XYChart<Number, Number> chart, Label stepCountLabel) {
        this.stepCountLabel = stepCountLabel;
        simulating = new SimpleBooleanProperty(false);
        simulationTimeline = new Timeline();
        simulationTimeline.setCycleCount(Animation.INDEFINITE);
        smallestXAxisSpan = SwarmProperties.tolerance() * 4 * DEFAULT_X_AXIS_SPAN;
        biggestXAxisSpan = SwarmProperties.maximumMeaningfulDoubleValue();
        xAxis = (NumberAxis) chart.getXAxis();
        yAxis = (NumberAxis) chart.getYAxis();
        updateAxesSpan();
        this.xyAxesRatio = xAxisSpan / yAxisSpan;
        domainSeries = new Series<>();
        swarmSeries = new Series<>();
        keyPressedEventHandler = this::swarmChartKeyPressed;
        chart.getData().add(domainSeries);
        chart.getData().add(swarmSeries);
        reset();
    }
    //endregion

    //region Main methods
    //------------------------------------------------------------------------------------------------

    /**
     * Set hive mind.
     *
     * @param hiveMind hive mind
     */
    public void setSources(HiveMind hiveMind) {
        this.hiveMind = hiveMind;
        if (hiveMind == null)
            reset();
        else {
            updateAxesSpan();
            translateAndScaleToSpawn();
            drawDomain();
            drawSwarm();
        }
    }

    /**
     * Update axes span and shapes scaling factor.
     */
    private void updateAxesSpan() {
        xAxisSpan = xAxis.getUpperBound() - xAxis.getLowerBound();
        yAxisSpan = yAxis.getUpperBound() - yAxis.getLowerBound();
        shapeScalingFactor = xAxis.getWidth() / xAxisSpan;
    }

    /**
     * Translate and scale axes to the spawn shape.
     */
    private void translateAndScaleToSpawn() {
        translateAxesToSpawn();
        scaleAxesToSpawn();
    }

    /**
     * Reset chart and simulation state.
     */
    public void reset() {
        domainSeries.getData().clear();
        swarmSeries.getData().clear();
        translateAxesToDefault();
        scaleAxesToDefault();
    }

    /**
     * Execute a swarm step and update the chart.
     */
    public void swarmStep() {
        if (hiveMind.isSwarmAlive()) {
            hiveMind.swarmStep();
            drawDomain();
            drawSwarm();
            updateStepCountLabel();
        } else
            simulating.set(false);
    }

    /**
     * Update the step count label text with proper formatting.
     */
    private void updateStepCountLabel() {
        double stepCount = hiveMind.stepCount();
        if (stepCount < 1000)
            stepCountLabel.setText(String.format("%.0f steps", stepCount));
        else if (stepCount < 1000000)
            stepCountLabel.setText(String.format("%.3f Ksteps", stepCount / 1000.0));
        else if (stepCount < 1000000000)
            stepCountLabel.setText(String.format("%.3f Msteps", stepCount / 1000000.0));
        else
            stepCountLabel.setText(String.format("%.3f Gsteps", stepCount / 1000000000.0));
    }
    //endregion

    //region Draw methods
    //------------------------------------------------------------------------------------------------

    /**
     * Draw domain regions.
     */
    private void drawDomain() {
        domainSeries.getData().clear();
        domainSeries.getData().addAll(hiveMind.state().domain().stream().map(r -> {
            Rectangle br = r.shape().getBoundingRectangle();
            javafx.scene.shape.Shape shape = shapeTransform(r.shape());
            shape.setFill(DEFAULT_REGION_COLOR);
            Label label = new Label(r.signal());
            label.setFont(DEFAULT_REGION_LABEL_FONT);
            StackPane regionPane = new StackPane(shape, label);
            regionPane.autosize();
            regionPane.setBackground(DEFAULT_REGION_PANE_BACKGROUND);
            Data<Number, Number> data = new Data<>(br.getCenter().x(), br.getCenter().y());
            data.setNode(regionPane);
            return data;
        }).toList());
    }

    /**
     * Transform a swarm shape to a JavaFX shape.
     *
     * @param shape swarm shape
     * @return JavaFX shape
     */
    private javafx.scene.shape.Shape shapeTransform(Shape shape) {
        double[] args = Arrays.stream(shape.getProperties()).map(a -> a * shapeScalingFactor).toArray();
        // Would look better with patter matching switch, will come with Java 21 LTS project update
        if (shape instanceof Circle)
            return new javafx.scene.shape.Circle(args[0], args[1], args[2]);
        else if (shape instanceof Ellipse)
            return new javafx.scene.shape.Ellipse(args[0], args[1], args[2], args[3]);
        else if (shape instanceof Rectangle)
            return new javafx.scene.shape.Rectangle(args[0], args[1], args[2], args[3]);
        else if (shape instanceof Polygon)
            return new javafx.scene.shape.Polygon(IntStream.range(0, args.length)
                    .mapToDouble(i -> i % 2 == 0 ? args[i] : -args[i]).toArray());
        else
            throw new IllegalArgumentException("Unknown shape type: " + shape.getClass().getName());
    }

    /**
     * Draw swarm drones.
     */
    private void drawSwarm() {
        swarmSeries.getData().clear();
        swarmSeries.getData().addAll(SwarmUtils.parallelize(() ->
                hiveMind.state().swarm().parallelStream().map(d ->
                        new Data<Number, Number>(d.position().x(), d.position().y())).toList()));
    }
    //endregion

    //region Translation methods
    //------------------------------------------------------------------------------------------------

    /**
     * Translate axes to the default origin.
     */
    public void translateAxesToDefault() {
        translateAxes(DEFAULT_ORIGIN);
    }

    /**
     * Translate axes to the spawn shape origin.
     */
    public void translateAxesToSpawn() {
        translateAxes(hiveMind.state().spawnShape().getBoundingRectangle().getCenter());
    }

    /**
     * Move axes to the center of the spawn shape or the default origin.
     *
     * @param defaultOrigin default origin flag
     */
    public void moveViewToCenter(boolean defaultOrigin) {
        if (defaultOrigin)
            translateAxesToDefault();
        else
            translateAxesToSpawn();
    }

    /**
     * Translate axes to a destination position.
     *
     * @param destination destination position
     */
    public void translateAxes(Position destination) {
        Position currentCenter = new Position((xAxis.getUpperBound() + xAxis.getLowerBound()) / 2,
                (yAxis.getUpperBound() + yAxis.getLowerBound()) / 2);
        Position toDestination = currentCenter.directionTo(destination).scale(currentCenter.distanceTo(destination));
        translateAxes(toDestination.x(), toDestination.y());
    }

    /**
     * Translate axes by a delta.
     *
     * @param dx x-axis delta
     * @param dy y-axis delta
     */
    public void translateAxes(double dx, double dy) {
        translateXAxis(dx);
        translateYAxis(dy);
        updateAxesSpan();
    }

    /**
     * Move view in a specific direction by a delta.
     *
     * @param direction      direction (0: up, 1: down, 2: left, 3: right)
     * @param amount         delta
     * @param firstModifier  first modifier flag
     * @param secondModifier second modifier flag
     */
    public void moveView(int direction, double amount, boolean firstModifier, boolean secondModifier) {
        switch (direction) {
            case 0 -> translateYAxis(
                    applyMoveMultiplier(amount * tickUnit, firstModifier, secondModifier));
            case 1 -> translateYAxis(
                    applyMoveMultiplier(-amount * tickUnit, firstModifier, secondModifier));
            case 2 -> translateXAxis(
                    applyMoveMultiplier(-amount * tickUnit, firstModifier, secondModifier));
            case 3 -> translateXAxis(
                    applyMoveMultiplier(amount * tickUnit, firstModifier, secondModifier));
            default -> throw new IllegalArgumentException("Unknown direction: " + direction);
        }
    }

    /**
     * Translate x-axis by a delta.
     *
     * @param dx x-axis delta
     */
    public void translateXAxis(double dx) {
        translateAxis(xAxis, dx);
    }

    /**
     * Translate y-axis by a delta.
     *
     * @param dy y-axis delta
     */
    public void translateYAxis(double dy) {
        translateAxis(yAxis, dy);
    }

    /**
     * Translate an axis by a delta.
     *
     * @param axis  axis to translate
     * @param delta delta
     */
    private void translateAxis(NumberAxis axis, double delta) {
        if (axis.getLowerBound() + delta < -biggestXAxisSpan)
            delta = -biggestXAxisSpan - axis.getLowerBound();
        else if (axis.getUpperBound() + delta > biggestXAxisSpan)
            delta = biggestXAxisSpan - axis.getUpperBound();
        axis.setLowerBound(axis.getLowerBound() + delta);
        axis.setUpperBound(axis.getUpperBound() + delta);
    }

    /**
     * Apply the move multiplier to a value, based on modifiers' flags.
     *
     * @param value          value to multiply
     * @param firstModifier  first modifier enabled flag
     * @param secondModifier second modifier enabled flag
     * @return move multiplied value
     */
    public double applyMoveMultiplier(double value, boolean firstModifier, boolean secondModifier) {
        if (firstModifier)
            value *= MOVE_FIRST_MODIFIER_MULTIPLIER;
        if (secondModifier)
            value *= MOVE_SECOND_MODIFIER_MULTIPLIER;
        return value;
    }
    //endregion

    //region Scaling methods
    //------------------------------------------------------------------------------------------------

    /**
     * Scale axes to the default span.
     */
    public void scaleAxesToDefault() {
        scaleAxes(DEFAULT_X_AXIS_SPAN);
    }

    /**
     * Scale axes to the spawn shape span.
     */
    public void scaleAxesToSpawn() {
        Rectangle br = hiveMind.state().spawnShape().getBoundingRectangle();
        if (br.getWidthHeight().x() > br.getWidthHeight().y())
            scaleAxes(br.getWidthHeight().x() + 2 * br.getWidthHeight().x() / 10);
        else
            scaleAxes((br.getWidthHeight().y() + 2 * br.getWidthHeight().y() / 10) * xyAxesRatio);
    }


    /**
     * Scale axes to spawn shape span or default span.
     *
     * @param defaultSpan default span flag
     */
    public void zoomViewToCenter(boolean defaultSpan) {
        if (defaultSpan)
            scaleAxesToDefault();
        else
            scaleAxesToSpawn();
    }

    /**
     * Scale axes by a scaling factor.
     *
     * @param scalingFactor scaling factor
     */
    public void scaleAxesByFactor(double scalingFactor) {
        scaleAxes((xAxis.getUpperBound() - xAxis.getLowerBound()) * scalingFactor);
    }

    /**
     * Scale axes to a new span.
     *
     * @param newXAxisSpan new x-axis span
     */
    private void scaleAxes(double newXAxisSpan) {
        if (newXAxisSpan < smallestXAxisSpan)
            newXAxisSpan = smallestXAxisSpan;
        else if (newXAxisSpan > biggestXAxisSpan)
            newXAxisSpan = biggestXAxisSpan;
        double newYAxisSpan = newXAxisSpan / xyAxesRatio;
        newXAxisSpan = newYAxisSpan * xyAxesRatio;
        scaleAxis(xAxis, newXAxisSpan);
        scaleAxis(yAxis, newYAxisSpan);
        updateAxesSpan();
        tickUnit = newYAxisSpan / Y_AXIS_TICK_COUNT;
        xAxis.setTickUnit(tickUnit);
        yAxis.setTickUnit(tickUnit);
        if (!domainSeries.getData().isEmpty())
            drawDomain();
    }

    /**
     * Zoom view in or out.
     *
     * @param inOut          zoom in/out flag
     * @param firstModifier  first modifier flag
     * @param secondModifier second modifier flag
     */
    public void zoomView(boolean inOut, boolean firstModifier, boolean secondModifier) {
        if (inOut)
            scaleAxesByFactor(1 /
                    applyZoomMultiplier(2, firstModifier, secondModifier));
        else
            scaleAxesByFactor(applyZoomMultiplier(2, firstModifier, secondModifier));
    }

    /**
     * Scale an axis to a new span.
     *
     * @param axis    axis to scale
     * @param newSpan new span
     */
    private void scaleAxis(NumberAxis axis, double newSpan) {
        double spanIncreaseHalf = (newSpan + axis.getLowerBound() - axis.getUpperBound()) / 2;
        axis.setLowerBound(axis.getLowerBound() - spanIncreaseHalf);
        axis.setUpperBound(axis.getUpperBound() + spanIncreaseHalf);
    }

    /**
     * Apply the zoom multiplier to a value, based on modifiers' flags.
     *
     * @param value          value to multiply
     * @param firstModifier  first modifier enabled flag
     * @param secondModifier second modifier enabled flag
     * @return zoom value multiplied
     */
    public double applyZoomMultiplier(double value, boolean firstModifier, boolean secondModifier) {
        if (firstModifier)
            value *= ZOOM_FIRST_MODIFIER_MULTIPLIER;
        if (secondModifier)
            value *= ZOOM_SECOND_MODIFIER_MULTIPLIER;
        return value;
    }
    //endregion

    //region Simulation additional event handlers methods
    //------------------------------------------------------------------------------------------------

    /**
     * Get the additional key pressed event handler for the simulation.
     *
     * @return key pressed event handler
     */
    public EventHandler<KeyEvent> getKeyPressedEventHandler() {
        return keyPressedEventHandler;
    }

    /**
     * Swarm chart key pressed event handler.
     *
     * @param event key event
     */
    private void swarmChartKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case W, NUMPAD8 -> executeAndConsumeEvent(() ->
                    moveView(0, 1, event.isShiftDown(), event.isControlDown()), event);
            case S, NUMPAD5 -> executeAndConsumeEvent(() ->
                    moveView(1, 1, event.isShiftDown(), event.isControlDown()), event);
            case A, NUMPAD4 -> executeAndConsumeEvent(() ->
                    moveView(2, 1, event.isShiftDown(), event.isControlDown()), event);
            case D, NUMPAD6 -> executeAndConsumeEvent(() ->
                    moveView(3, 1, event.isShiftDown(), event.isControlDown()), event);
            case F, NUMPAD2 -> executeAndConsumeEvent(() ->
                    moveViewToCenter(event.isShiftDown()), event);
            case E, ADD -> executeAndConsumeEvent(() ->
                    zoomView(true, event.isShiftDown(), event.isControlDown()), event);
            case Q, SUBTRACT -> executeAndConsumeEvent(() ->
                    zoomView(false, event.isShiftDown(), event.isControlDown()), event);
            case R, MULTIPLY -> executeAndConsumeEvent(() ->
                    zoomViewToCenter(event.isShiftDown()), event);
            case N, DECIMAL -> executeAndConsumeEvent(this::swarmStep, event);
            case T, NUMPAD0 -> simulating.set(!simulating.getValue());
            default -> {
                // Do nothing
            }
        }
    }
    //endregion

    //region Simulation methods
    //------------------------------------------------------------------------------------------------

    /**
     * Toggle simulating and start or stop the simulation timeline.
     *
     * @param ms frame duration in milliseconds
     */
    public void toggleSimulating(double ms) {
        if (simulating.getValue() != null && !simulating.getValue())
            simulationTimeline.stop();
        else {
            simulationTimeline.getKeyFrames().clear();
            simulationTimeline.getKeyFrames().add(
                    new KeyFrame(Duration.millis(ms), e -> swarmStep()));
            simulationTimeline.play();
        }
    }
    //endregion

    //region Additional helper methods
    //------------------------------------------------------------------------------------------------

    /**
     * Get the mouse position on the swarm chart from a gesture event.
     *
     * @param event gesture event
     * @return mouse position
     */
    public Position getMousePosition(GestureEvent event) {
        Point2D mouseScenePoint = new Point2D(event.getSceneX(), event.getSceneY());
        return new Position(
                xAxis.getValueForDisplay(xAxis.sceneToLocal(mouseScenePoint).getX()).doubleValue(),
                yAxis.getValueForDisplay(yAxis.sceneToLocal(mouseScenePoint).getY()).doubleValue());
    }

    /**
     * Get double values representing the range for the current simulation range.
     *
     * @return range values, represented as a position
     */
    public Position getSimulationPeriodRange() {
        double shortestPeriod = SHORTEST_SIMULATION_PERIOD.applyAsDouble(
                hiveMind.state().swarm().size(), SwarmProperties.parallelism());
        return new Position(shortestPeriod, shortestPeriod * LONGEST_SIMULATION_PERIOD_MULTIPLIER);
    }

    /**
     * Execute a runnable and consume an event.
     *
     * @param runnable runnable
     * @param event    event
     */
    private void executeAndConsumeEvent(Runnable runnable, KeyEvent event) {
        runnable.run();
        event.consume();
    }
    //endregion
}
