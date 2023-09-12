package it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.parser;

import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Region;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Parser for domain creation from an external source.
 */
public interface DomainParser {
    /**
     * Parses a domain from a file.
     *
     * @param file file to parse
     * @return regions created from the file
     * @throws DomainParserException if a line could not be parsed or the file could not be read
     */
    default List<Region> parseDomain(File file) throws DomainParserException {
        return parseDomain(file.toPath());
    }

    /**
     * Parses a domain from a file path.
     *
     * @param path path to the file
     * @return regions created from the file
     * @throws DomainParserException if a line could not be parsed or the file could not be read
     */
    default List<Region> parseDomain(Path path) throws DomainParserException {
        try {
            return parseDomain(Files.readString(path));
        } catch (IOException e) {
            throw new DomainParserException("Error while reading domain file.", e);
        }
    }

    /**
     * Parses a domain from a string.
     *
     * @param data string to parse
     * @return regions created from the string
     * @throws DomainParserException if a line could not be parsed
     */
    List<Region> parseDomain(String data) throws DomainParserException;
}
