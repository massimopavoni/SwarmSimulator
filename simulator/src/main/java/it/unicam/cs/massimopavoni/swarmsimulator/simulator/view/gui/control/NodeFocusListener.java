package it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui.control;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Label;

/**
 * Class representing a listener for focus change events on a node.
 */
public final class NodeFocusListener implements ChangeListener<Boolean> {
    /**
     * Help label for GUI elements information.
     */
    private final Label helpLabel;
    /**
     * Observed node.
     */
    private final Node observedNode;

    /**
     * Constructor for the listener.
     *
     * @param helpLabel    help label
     * @param observedNode observed node
     */
    public NodeFocusListener(Label helpLabel, Node observedNode) {
        this.helpLabel = helpLabel;
        this.observedNode = observedNode;
    }

    /**
     * Changed method for observed focus property changes.
     *
     * @param observable observable value which value changed
     * @param oldValue   old value
     * @param newValue   new value
     */
    @Override
    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (newValue != null && newValue)
            helpLabel.setText(observedNode.getAccessibleHelp());
    }
}
