package it.unicam.cs.massimopavoni.swarmsimulator.swarm;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.HiveMindException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmProperties;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.core.SwarmState;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.parser.DomainParserException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.ShapeFactory;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.ShapeType;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.SwarmShapeFactory;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.parser.StrategyParserException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public final class TestUtils {
    public static void initializeProperties(String testName) throws HiveMindException {
        SwarmProperties.initialize(SwarmProperties.DEFAULT_SWARM_FOLDER + "test/" + testName + "/",
                SwarmProperties.DEFAULT_SWARM_PROPERTIES_FILE_NAME);
    }

    public static void deleteProperties(String testName) throws IOException {
        String testSwarmFolder = SwarmProperties.DEFAULT_SWARM_FOLDER + "test/";
        Files.deleteIfExists(Path.of(testSwarmFolder + testName + "/" +
                SwarmProperties.DEFAULT_SWARM_PROPERTIES_FILE_NAME));
        Files.deleteIfExists(Path.of(testSwarmFolder + testName + "/"));
        Files.deleteIfExists(Path.of(testSwarmFolder));
        SwarmProperties.reset();
    }

    public static SwarmState getNewTestSwarmState(int dronesNumber, ShapeType shapeType,
                                                  double[] shapeArgs, boolean onBoundary)
            throws DomainParserException, StrategyParserException {
        ShapeFactory shapeFactory = new SwarmShapeFactory();
        SwarmState.initializeParsers(shapeFactory, null);
        return new SwarmState(new File(
                Objects.requireNonNull(TestUtils.class.getClassLoader().getResource(
                        "it/unicam/cs/massimopavoni/swarmsimulator/swarm/domain/parser/" +
                                "testDomain.swarm")).getPath()),
                new File(Objects.requireNonNull(TestUtils.class.getClassLoader().getResource(
                        "it/unicam/cs/massimopavoni/swarmsimulator/swarm/strategy/parser/" +
                                "testStrategy.swarm")).getPath()),
                dronesNumber, shapeFactory.createShape(shapeType, shapeArgs), onBoundary);
    }
}
