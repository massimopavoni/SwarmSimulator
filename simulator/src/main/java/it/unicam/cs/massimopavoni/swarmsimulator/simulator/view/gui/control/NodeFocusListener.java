package it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui.control;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Label;

/**
 * Class representing a listener for focus events on a node and show the corresponding help text.
 */
public final class NodeFocusListener implements ChangeListener<Boolean> {
    /**
     * Help label for GUI elements information.
     */
    private final Label help;
    /**
     * Observed node.
     */
    private final Node observedNode;

    /**
     * Constructor for the listener.
     *
     * @param help         help label
     * @param observedNode observed node
     */
    public NodeFocusListener(Label help, Node observedNode) {
        this.help = help;
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
            help.setText(observedNode.getAccessibleHelp());
    }
}
