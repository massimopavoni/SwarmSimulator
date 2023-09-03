package it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.Drone;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Position;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Region;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.List;
import java.util.function.DoubleToLongFunction;

public final class SwarmChartController {
    public static final double DEFAULT_X_AXIS_SPAN = 14;
    public static final double DEFAULT_TICK_UNIT = 10;
    public static final Position DEFAULT_ORIGIN = new Position(0, 0);
    private static final DoubleToLongFunction DEFAULT_AXIS_ROUNDING = Math::round;
    private final XYChart<Number, Number> chart;
    private final NumberAxis xAxis;
    private final NumberAxis yAxis;
    private final double xyAxesRatio;
    private List<Region> domain;
    private List<Drone> swarm;

    public SwarmChartController(XYChart<Number, Number> chart) {
        this.chart = chart;
        xAxis = (NumberAxis) chart.getXAxis();
        yAxis = (NumberAxis) chart.getYAxis();
        this.xyAxesRatio = (xAxis.getUpperBound() - xAxis.getLowerBound()) /
                (yAxis.getUpperBound() - yAxis.getLowerBound());
        reset();
    }

    public void setSources(List<Region> domain, List<Drone> swarm) {
        this.domain = domain;
        this.swarm = swarm;
    }

    public void translateAxes(Position destination) {
        Position currentCenter = new Position((xAxis.getUpperBound() + xAxis.getLowerBound()) / 2,
                (yAxis.getUpperBound() + yAxis.getLowerBound()) / 2);
        Position toDestination = currentCenter.directionTo(destination).scale(currentCenter.distanceTo(destination));
        translateAxes(toDestination.x(), toDestination.y());
    }

    public void translateAxes(double dx, double dy) {
        translateXAxis(dx);
        translateYAxis(dy);
    }

    public void translateXAxis(double dx) {
        translateAxis(xAxis, dx);
    }

    public void translateYAxis(double dy) {
        translateAxis(yAxis, dy);
    }

    private void translateAxis(NumberAxis axis, double delta) {
        axis.setLowerBound(axis.getLowerBound() + delta);
        axis.setUpperBound(axis.getUpperBound() + delta);
    }

    public void zoom(double zoomFactor) {

    }

    private void scaleAxes(double newXAxisSpan) {
        double newYAxisSpan = DEFAULT_AXIS_ROUNDING.applyAsLong(newXAxisSpan / xyAxesRatio);
        newXAxisSpan = DEFAULT_AXIS_ROUNDING.applyAsLong(newYAxisSpan * xyAxesRatio);
        scaleAxis(xAxis, newXAxisSpan);
        scaleAxis(yAxis, newYAxisSpan);
        double newTickUnit = newYAxisSpan / DEFAULT_TICK_UNIT;
        xAxis.setTickUnit(newTickUnit);
        yAxis.setTickUnit(newTickUnit);
    }

    private void scaleAxis(NumberAxis axis, double newSpan) {
        double spanIncreaseHalf = (newSpan + axis.getLowerBound() - axis.getUpperBound()) / 2;
        axis.setLowerBound(axis.getLowerBound() - spanIncreaseHalf);
        axis.setUpperBound(axis.getUpperBound() + spanIncreaseHalf);
    }

    private void update() {
        chart.getData().clear();
    }

    public void reset() {
        chart.getData().clear();
        translateAxes(DEFAULT_ORIGIN);
        scaleAxes(DEFAULT_X_AXIS_SPAN);
    }
}
