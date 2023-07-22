package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.parser;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives.Directive;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Parser for strategy creation from an external source.
 */
public interface StrategyParser {
    /**
     * Parses a strategy from a file.
     *
     * @param file file to parse
     * @return directives created from the file
     * @throws StrategyParserException if a line could not be parsed or the file could not be read
     */
    default List<Directive> parseStrategy(File file) throws StrategyParserException {
        return parseStrategy(file.toPath());
    }

    /**
     * Parses a strategy from a file path.
     *
     * @param path path to the file
     * @return directives created from the file
     * @throws StrategyParserException if a line could not be parsed or the file could not be read
     */
    default List<Directive> parseStrategy(Path path) throws StrategyParserException {
        try {
            return parseStrategy(Files.readString(path));
        } catch (IOException e) {
            throw new StrategyParserException("Error while reading strategy file.", e);
        }
    }

    /**
     * Parses a strategy from a string.
     *
     * @param data string to parse
     * @return directives created from the string
     * @throws StrategyParserException if a line could not be parsed
     */
    List<Directive> parseStrategy(String data) throws StrategyParserException;
}
