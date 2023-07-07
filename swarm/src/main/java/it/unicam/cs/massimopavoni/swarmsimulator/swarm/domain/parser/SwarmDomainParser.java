package it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.parser;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.SwarmUtils;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.DomainException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Region;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.ShapeException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.ShapeFactory;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.shapes.ShapeType;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Swarm implementation of the domain parser.
 */
public final class SwarmDomainParser implements DomainParser {
    private final ShapeFactory shapeFactory;

    /**
     * Constructor for domain parser given a shape factory.
     *
     * @param shapeFactory factory class object for creating shapes
     */
    public SwarmDomainParser(ShapeFactory shapeFactory) {
        this.shapeFactory = shapeFactory;
    }

    /**
     * Parses a domain from a string.
     *
     * @param data string to parse
     * @return regions created from the string
     * @throws DomainParserException if a line could not be parsed
     */
    @Override
    public List<Region> parseDomain(String data) throws DomainParserException {
        return parseDomain(data.lines().toList());
    }

    /**
     * Parses a list of lines into a list of regions (a swarm domain).
     *
     * @param lines lines to parse
     * @return regions created from the lines
     * @throws DomainParserException if a line could not be parsed
     */
    private List<Region> parseDomain(List<String> lines) throws DomainParserException {
        AtomicInteger lineCounter = new AtomicInteger(-1);
        try {
            return lines.stream()
                    .map(l -> {
                        lineCounter.getAndIncrement();
                        return parseLine(l);
                    }).collect(toImmutableList());
        } catch (DomainException | ShapeException | IllegalArgumentException e) {
            throw new DomainParserException(
                    String.format("Error while parsing domain on line %d.", lineCounter.get()), e);
        }
    }

    /**
     * Parses a single line of the domain file, creating a region.
     *
     * @param line line to parse
     * @return region created from the line
     * @throws DomainException          if the line could not be split into enough arguments
     * @throws ShapeException           if the shape could not be created
     * @throws IllegalArgumentException if the arguments could not be parsed into a double array
     */
    private Region parseLine(String line) {
        String[] args = line.trim().split(" ");
        if (args.length < 3)
            throw new DomainException("Not enough arguments for region creation.");
        ShapeType shapeType = ShapeType.fromString(args[1].toLowerCase());
        return new Region(args[0], shapeType,
                shapeFactory.createShape(shapeType, SwarmUtils.toDoubleArray(args, 2)));
    }
}
