package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.parser;

import com.google.common.collect.Iterables;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.StrategyException;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

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
        resetParsing();
        try {
            lines.forEach(this::parseLine);
            if (!uncompletedJumpDirectives.isEmpty())
                throw new StrategyException("Inconsistent loop directives detected.");
            if (Iterables.getLast(strategy, null).getClass() != Stop.class)
                throw new StrategyException("Strategy must end with a stop directive.");
            return strategy.stream().collect(toImmutableList());
        } catch (DirectiveException | StrategyException | IllegalArgumentException e) {
            throw new StrategyParserException(
                    String.format("Error while parsing strategy on line %d.", lineCounter), e);
        }
    }

    /**
     * Resets the parsing state.
     */
    private void resetParsing() {
        lineCounter = -1;
        strategy = new ArrayList<>();
        uncompletedJumpDirectives = new ArrayDeque<>();
        uncompletedJumpIndexes = new ArrayDeque<>();
        uncompletedJumpArgs = new ArrayDeque<>();
    }

    /**
     * Parses a single line of the strategy file, creating directives and adding them to the strategy.
     *
     * @param line line to parse
     * @throws DirectiveException       if the directive could not be created
     * @throws StrategyException        if the strategy directives are inconsistent
     * @throws IllegalArgumentException if the arguments could not be parsed
     */
    private void parseLine(String line) {
        lineCounter++;
        if (line.isBlank())
            throw new DirectiveException("Not enough arguments for directive parsing.");
        List<String> args = new ArrayList<>(List.of(line.trim().split(" ")));
        ParserDirective parserDirective = ParserDirective.fromLine(line.toLowerCase());
        args.remove(0);
        if (EchoDirective.isPermittedDirective(parserDirective.getDirectiveClass()))
            addDirective(parserDirective, args);
        else if (JumpDirective.isPermittedDirective(parserDirective.getDirectiveClass()))
            parseJumpDirective(parserDirective, args);
        else if (MovementDirective.isPermittedDirective(parserDirective.getDirectiveClass()))
            addDirective(parserDirective, args);
    }

    /**
     * Parses a jump directive, adding directives to the strategy or preparing for future backtracking.
     *
     * @param parserDirective directive to parse
     * @param args            arguments for the directive
     */
    private void parseJumpDirective(ParserDirective parserDirective, List<String> args) {
        switch (parserDirective) {
            case DONE -> completeLoop(parserDirective, args);
            case CONTINUE -> addDirective(parserDirective, args);
            default -> {
                uncompletedJumpDirectives.push(parserDirective);
                uncompletedJumpIndexes.push(lineCounter);
                uncompletedJumpArgs.push(args);
            }
        }
    }

    /**
     * Completes a loop directive, adding the two delimiting directives to the strategy.
     *
     * @param parserDirective done parser directive
     * @param args            arguments for the done directive
     */
    private void completeLoop(ParserDirective parserDirective, List<String> args) {
        if (uncompletedJumpDirectives.isEmpty())
            throw new StrategyException("Unexpected inconsistent loop directive received.");
        int index = uncompletedJumpIndexes.pop();
        uncompletedJumpArgs.getFirst().add(0, String.valueOf(lineCounter + 1));
        addDirective(index - uncompletedJumpIndexes.size(),
                uncompletedJumpDirectives.pop(), uncompletedJumpArgs.pop());
        args.add(0, String.valueOf(index));
        addDirective(parserDirective, args);
    }

    /**
     * Adds a directive to the strategy.
     *
     * @param parserDirective directive to add
     * @param args            arguments for the directive
     */
    private void addDirective(ParserDirective parserDirective, List<String> args) {
        strategy.add(directiveFactory.createDirective(parserDirective, args.toArray(String[]::new)));
    }


    /**
     * Adds a directive to the strategy at the given index.
     *
     * @param index           index to add the directive at
     * @param parserDirective directive to add
     * @param args            arguments for the directive
     */
    private void addDirective(int index, ParserDirective parserDirective, List<String> args) {
        strategy.add(index, directiveFactory.createDirective(parserDirective, args.toArray(String[]::new)));
    }
}
