package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.parser;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.StrategyException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives.Directive;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives.DirectiveException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives.DirectiveFactory;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives.ParserDirective;

import java.util.*;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Parser for swarm strategy creation from external source.
 */
public final class SwarmStrategyParser implements StrategyParser {
    /**
     * Factory class object for creating directives.
     */
    private final DirectiveFactory directiveFactory;
    /**
     * Line counter integer.
     */
    private int lineCounter;
    /**
     * List of directives to be created.
     */
    private List<Directive> strategy;
    /**
     * Deque of uncompleted jump directives to use for backtracking.
     */
    private Deque<ParserDirective> uncompletedJumpDirectives;
    /**
     * Deque of uncompleted jump directives indexes to use for backtracking.
     */
    private Deque<Integer> uncompletedJumpIndexes;
    /**
     * Deque of uncompleted jump directives arguments to use for backtracking.
     */
    private Deque<List<String>> uncompletedJumpArgs;

    /**
     * Constructor for strategy parser given a directive factory.
     *
     * @param directiveFactory factory class object for creating directives
     */
    public SwarmStrategyParser(DirectiveFactory directiveFactory) {
        this.directiveFactory = directiveFactory;
    }

    /**
     * Parses a strategy from a string.
     *
     * @param data string to parse
     * @return directives created from the string
     * @throws StrategyParserException if a line could not be parsed
     */
    @Override
    public List<Directive> parseStrategy(String data) throws StrategyParserException {
        return parseStrategy(data.lines().toList());
    }

    /**
     * Parses a list of lines into a list of directives (a swarm strategy).
     *
     * @param lines lines to parse
     * @return directives created from the lines
     * @throws StrategyParserException if a line could not be parsed
     */
    private List<Directive> parseStrategy(List<String> lines) throws StrategyParserException {
        lineCounter = -1;
        strategy = new ArrayList<>();
        uncompletedJumpDirectives = new ArrayDeque<>();
        uncompletedJumpIndexes = new ArrayDeque<>();
        uncompletedJumpArgs = new ArrayDeque<>();
        try {
            lines.forEach(this::parseLine);
            if (!uncompletedJumpDirectives.isEmpty())
                throw new StrategyException("Inconsistent loop directives detected.");
            return strategy.stream().collect(toImmutableList());
        } catch (DirectiveException | StrategyException | IllegalArgumentException e) {
            throw new StrategyParserException(
                    String.format("Error while parsing strategy on line %d.", lineCounter), e);
        }
    }

    /**
     * Parses a single line of the strategy file, creating directives and adding them to the strategy.
     *
     * @param line line to parse
     * @throws DirectiveException       if the directive could not be created
     * @throws StrategyException        if the strategy loops are inconsistent
     * @throws IllegalArgumentException if the arguments could not be parsed
     */
    private void parseLine(String line) {
        List<String> args = Arrays.asList(line.trim().split(" "));
        if (args.isEmpty())
            throw new DirectiveException("Not enough arguments for directive parsing.");
        ParserDirective parserDirective = ParserDirective.fromString(args.get(0).toLowerCase());
        args.remove(0);
        lineCounter++;
        switch (parserDirective) {
            case DOFOREVER, REPEAT, UNTIL -> {
                uncompletedJumpDirectives.push(parserDirective);
                uncompletedJumpIndexes.push(lineCounter);
                uncompletedJumpArgs.push(args);
            }
            case DONE -> {
                if (uncompletedJumpDirectives.isEmpty())
                    throw new StrategyException("Unexpected inconsistent loop directive received.");
                int index = uncompletedJumpIndexes.pop();
                uncompletedJumpArgs.getFirst().add(0, String.valueOf(lineCounter + 1));
                strategy.add(index, directiveFactory.createDirective(
                        uncompletedJumpDirectives.pop(), uncompletedJumpArgs.pop().toArray(String[]::new)));
                args.add(0, String.valueOf(index));
                strategy.add(directiveFactory.createDirective(parserDirective, args.toArray(String[]::new)));
            }
            default -> strategy.add(directiveFactory.createDirective(
                    parserDirective, args.toArray(String[]::new)));
        }
    }
}
