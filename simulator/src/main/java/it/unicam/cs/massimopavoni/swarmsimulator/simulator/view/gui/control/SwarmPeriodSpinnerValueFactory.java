package it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui.control;

import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.util.StringConverter;

/**
 * Class representing an integer spinner value factory for swarm simulation period.
 */
public class SwarmPeriodSpinnerValueFactory extends IntegerSpinnerValueFactory {
    /**
     * Constructor for swarm period spinner value factory.
     *
     * @param min minimum value
     * @param max maximum value
     */
    public SwarmPeriodSpinnerValueFactory(int min, int max) {
        this(min, max, min);
    }

    /**
     * Constructor for swarm period spinner value factory.
     *
     * @param min          minimum value
     * @param max          maximum value
     * @param initialValue initial value
     */
    public SwarmPeriodSpinnerValueFactory(int min, int max, int initialValue) {
        this(min, max, initialValue, 1);
    }

    /**
     * Constructor for swarm period spinner value factory.
     *
     * @param min            minimum value
     * @param max            maximum value
     * @param initialValue   initial value
     * @param amountToStepBy amount to step by
     */
    public SwarmPeriodSpinnerValueFactory(int min, int max, int initialValue, int amountToStepBy) {
        super(min, max, initialValue, amountToStepBy);
        setConverter(new StringConverter<>() {
            @Override
            public String toString(Integer object) {
                if (object > 999)
                    return String.format("%.3f s", object / 1000.0);
                else
                    return String.format("%d ms", object);
            }

            @Override
            public Integer fromString(String string) {
                throw new UnsupportedOperationException();
            }
        });
    }
}
