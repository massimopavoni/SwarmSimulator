package it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.Drone;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Position;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Region;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.List;
import java.util.function.DoubleFunction;

public final class SwarmChartController {
    public static final Position DEFAULT_ORIGIN = new Position(0, 0);
    public static final double DEFAULT_X_AXIS_SPAN = 14;
    public static final double SMALLEST_X_AXIS_SPAN = Math.pow(2, -8) * DEFAULT_X_AXIS_SPAN;
    public static final double BIGGEST_X_AXIS_SPAN = Double.MAX_VALUE;
    public static final double Y_AXIS_TICK_COUNT = 10;
    private static final DoubleFunction<Double> AXIS_SPAN_TRANSFORM = Math::abs;
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

    public void resetPosition() {
        translateAxes(DEFAULT_ORIGIN);
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
        if (axis.getLowerBound() + delta < -BIGGEST_X_AXIS_SPAN)
            delta = -BIGGEST_X_AXIS_SPAN - axis.getLowerBound();
        else if (axis.getUpperBound() + delta > BIGGEST_X_AXIS_SPAN)
            delta = BIGGEST_X_AXIS_SPAN - axis.getUpperBound();
        axis.setLowerBound(axis.getLowerBound() + delta);
        axis.setUpperBound(axis.getUpperBound() + delta);
    }

    public void resetZoom() {
        scaleAxes(DEFAULT_X_AXIS_SPAN);
    }

    public void zoom(double zoomFactor) {
        scaleAxes((xAxis.getUpperBound() - xAxis.getLowerBound()) * zoomFactor);
    }

    private void scaleAxes(double newXAxisSpan) {
        if (newXAxisSpan < SMALLEST_X_AXIS_SPAN)
            newXAxisSpan = SMALLEST_X_AXIS_SPAN;
        else if (newXAxisSpan > BIGGEST_X_AXIS_SPAN)
            newXAxisSpan = BIGGEST_X_AXIS_SPAN;
        double newYAxisSpan = AXIS_SPAN_TRANSFORM.apply(newXAxisSpan / xyAxesRatio);
        newXAxisSpan = newYAxisSpan * xyAxesRatio;
        scaleAxis(xAxis, newXAxisSpan);
        scaleAxis(yAxis, newYAxisSpan);
        double newTickUnit = newYAxisSpan / Y_AXIS_TICK_COUNT;
        xAxis.setTickUnit(newTickUnit);
        yAxis.setTickUnit(newTickUnit);
    }

    private void scaleAxis(NumberAxis axis, double newSpan) {
        double spanIncreaseHalf = (newSpan + axis.getLowerBound() - axis.getUpperBound()) / 2;
        axis.setLowerBound(axis.getLowerBound() - spanIncreaseHalf);
        axis.setUpperBound(axis.getUpperBound() + spanIncreaseHalf);
    }

    public void reset() {
        chart.getData().clear();
        resetPosition();
        resetZoom();
    }

    public void update() {
        chart.getData().clear();
    }
}
