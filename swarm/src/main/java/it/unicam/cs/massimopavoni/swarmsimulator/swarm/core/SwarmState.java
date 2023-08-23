package it.unicam.cs.massimopavoni.swarmsimulator.swarm.core;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.Drone;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Region;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.parser.DomainParser;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.parser.DomainParserException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.parser.SwarmDomainParser;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.Shape;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.ShapeFactory;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.SwarmShapeFactory;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives.Directive;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives.DirectiveFactory;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives.DirectiveType;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives.SwarmDirectiveFactory;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.parser.StrategyParser;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.parser.StrategyParserException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.parser.SwarmStrategyParser;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.IntStream;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Class representing the current state of the swarm.
 */
public final class SwarmState {
    /**
     * Parser for the domain.
     */
    private static DomainParser domainParser;
    /**
     * Parser for the strategy.
     */
    private static StrategyParser strategyParser;

    /**
     * List of regions constituting the domain.
     */
    private final List<Region> domain;
    /**
     * List of directives constituting the strategy.
     */
    private final List<Directive> strategy;
    /**
     * List of drones constituting the swarm.
     */
    private final List<Drone> swarm;

    /**
     * Constructor for a swarm state from domain, strategy and swarm information.
     *
     * @param domainFile   domain file to create list of regions
     * @param strategyFile strategy file to create list of directives
     * @param dronesNumber number of drones
     * @param spawnShape   shape of the swarm spawn region
     * @param onBoundary   true if the swarm spawns on the boundary of the provided shape
     * @throws SwarmException          if any runtime errors occur during state creation
     * @throws DomainParserException   if the domain string could not be parsed correctly
     * @throws StrategyParserException if the strategy string could not be parsed correctly
     */
    public SwarmState(File domainFile, File strategyFile,
                      int dronesNumber, Shape spawnShape, boolean onBoundary)
            throws DomainParserException, StrategyParserException {
        this(domainParser.parseDomain(domainFile), strategyParser.parseStrategy(strategyFile),
                dronesNumber, spawnShape, onBoundary);
    }

    /**
     * Constructor for a swarm state from domain, strategy and swarm information.
     *
     * @param domainPath   domain path to create list of regions
     * @param strategyPath strategy path to create list of directives
     * @param dronesNumber number of drones
     * @param spawnShape   shape of the swarm spawn region
     * @param onBoundary   true if the swarm spawns on the boundary of the provided shape
     * @throws SwarmException          if any runtime errors occur during state creation
     * @throws DomainParserException   if the domain string could not be parsed correctly
     * @throws StrategyParserException if the strategy string could not be parsed correctly
     */
    public SwarmState(Path domainPath, Path strategyPath,
                      int dronesNumber, Shape spawnShape, boolean onBoundary)
            throws DomainParserException, StrategyParserException {
        this(domainParser.parseDomain(domainPath), strategyParser.parseStrategy(strategyPath),
                dronesNumber, spawnShape, onBoundary);
    }

    /**
     * Constructor for a swarm state from domain, strategy and swarm information.
     *
     * @param domainString   domain string to create list of regions
     * @param strategyString strategy string to create list of directives
     * @param dronesNumber   number of drones
     * @param spawnShape     shape of the swarm spawn region
     * @param onBoundary     true if the swarm spawns on the boundary of the provided shape
     * @throws SwarmException          if any runtime errors occur during state creation
     * @throws DomainParserException   if the domain string could not be parsed correctly
     * @throws StrategyParserException if the strategy string could not be parsed correctly
     */
    public SwarmState(String domainString, String strategyString,
                      int dronesNumber, Shape spawnShape, boolean onBoundary)
            throws DomainParserException, StrategyParserException {
        this(domainParser.parseDomain(domainString), strategyParser.parseStrategy(strategyString),
                dronesNumber, spawnShape, onBoundary);
    }

    /**
     * Constructor for a swarm state from domain, strategy and swarm information.
     *
     * @param domain       domain list of regions
     * @param strategy     strategy list of directives
     * @param dronesNumber number of drones
     * @param spawnShape   shape of the swarm spawn region
     * @param onBoundary   true if the swarm spawns on the boundary of the provided shape
     * @throws SwarmException if any runtime errors occur during state creation
     */
    private SwarmState(List<Region> domain, List<Directive> strategy,
                       int dronesNumber, Shape spawnShape, boolean onBoundary) {
        if (dronesNumber < 1)
            throw new SwarmException("The number of drones must be greater than 0.");
        this.domain = domain;
        this.strategy = strategy;
        List<Integer> jumpDirectiveIndexes = IntStream.range(0, strategy.size())
                .filter(i -> strategy.get(i).getType().equals(DirectiveType.JUMP))
                .boxed().toList();
        swarm = SwarmUtils.parallelize(() -> spawnShape.getRandomPositions(onBoundary, dronesNumber)
                .parallelStream()
                .map(rp -> new Drone(rp, jumpDirectiveIndexes))).collect(toImmutableList());
    }

    /**
     * Initializes or reassigns the parsers.
     *
     * @param shapeFactory     factory class object for creating shapes, null for new default
     * @param directiveFactory factory class object for creating directives, null for new default
     */
    public static void initializeParsers(ShapeFactory shapeFactory, DirectiveFactory directiveFactory) {
        domainParser = new SwarmDomainParser(shapeFactory == null ?
                new SwarmShapeFactory() : shapeFactory);
        strategyParser = new SwarmStrategyParser(directiveFactory == null ?
                new SwarmDirectiveFactory() : directiveFactory);
    }

    /**
     * Getter for the domain.
     *
     * @return list of regions
     */
    public List<Region> domain() {
        return domain;
    }

    /**
     * Getter for the strategy.
     *
     * @return list of directives
     */
    public List<Directive> strategy() {
        return strategy;
    }

    /**
     * Getter for the swarm.
     *
     * @return list of drones
     */
    public List<Drone> swarm() {
        return swarm;
    }
}
