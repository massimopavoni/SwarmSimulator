package it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.parser;


import it.unicam.cs.massimopavoni.swarmsimulator.swarm.domain.Region;
import it.unicam.cs.massimopavoni.swarmsimulator.swarm.strategy.directives.ParserDirective;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Parser for swarm strategy creation from external source.
 */
public final class StrategyParser {

    private int counter = 0;

    private final StrategyParserHandler handler;

    private final FollowMeShapeChecker checker;

    public StrategyParser(StrategyParserHandler handler, FollowMeShapeChecker checker) {
        this.handler = handler;
        this.checker = checker;
    }

    public StrategyParser(StrategyParserHandler handler) {
        this(handler, FollowMeShapeChecker.DEFAULT_CHECKER);
    }

    public synchronized void parseRobotProgram(File sourceFile) throws IOException, StrategyParserException {
        parseRobotProgram(sourceFile.toPath());
    }

    public synchronized void parseRobotProgram(Path path) throws IOException, StrategyParserException {
        parseRobotProgram(Files.readAllLines(path));
    }

    public synchronized void parseRobotProgram(String code) throws StrategyParserException {
        parseRobotProgram(List.of(code.split("\n")));
    }

    public synchronized List<Region> parseEnvironment(File file) throws IOException, StrategyParserException {
        return parseEnvironment(file.toPath());
    }

    public synchronized List<Region> parseEnvironment(String data) throws StrategyParserException {
        return parseEnvironment(List.of(data.split("\n")));
    }

    public synchronized List<Region> parseEnvironment(Path path) throws IOException, StrategyParserException {
        return parseEnvironment(Files.readAllLines(path));
    }

    private List<Region> parseEnvironment(List<String> lines) throws StrategyParserException {
        this.counter = 0;
        List<Region> data = new LinkedList<>();
        for (String line: lines) {
            this.counter++;
            String[] elements = line.trim().toUpperCase().split(" ");
            if (checker.checkParameters(elements)) {
                //data.add(Region.fromString(elements));
            } else {
                throwSyntaxErrorException();
            }
        }
        return data;
    }

    private void parseRobotProgram(List<String> lines) throws StrategyParserException {
        this.counter = 0;
        handler.parsingStarted();
        for (String line: lines) {
            this.handleLine(line.trim().toUpperCase());
        }
        handler.parsingDone();
    }

    private void handleLine(String line) throws StrategyParserException {
        counter++;
        if (!line.isBlank()) {
            selectAndCallHandlerMethod(handler, line);
        }
    }

    private void selectAndCallHandlerMethod(StrategyParserHandler handler, String line) throws StrategyParserException {
        Optional<ParserDirective> oCommand = ParserDirective.fromLine(line);
        if (oCommand.isEmpty()) {
            throw new StrategyParserException(FollowMeParserUtil.unknownCommandMessage(this.counter));
        }
        callHandlerMethod(handler, oCommand.get(), line);
    }

    private void callHandlerMethod(StrategyParserHandler handler, ParserDirective ParserDirective, String line) throws StrategyParserException {
        String[] elements = line.split(" ");
        switch (ParserDirective) {
            case MOVE       -> callMoveMethods(handler, elements);
            case SIGNAL     -> callSignalMethod(handler, elements);
            case UNSIGNAL   -> callUnSignalMethod(handler, elements);
            case FOLLOW     -> callFollowMethod(handler, elements);
            case STOP       -> callStopMethod(handler, elements);
            case CONTINUE -> callContinueMethod(handler, elements);
            case REPEAT     -> callRepeatMethod(handler, elements);
            case UNTIL      -> callUntilMethod(handler, elements);
            case DOFOREVER    -> callForeverMethod(handler, elements);
            case DONE       -> callDoneMethod(handler, elements);
        }
    }

    private void callDoneMethod(StrategyParserHandler handler, String[] elements) throws StrategyParserException {
        if (elements.length == 1) {
            handler.doneCommand();
        } else {
            throwSyntaxErrorException();
        }
    }

    private void callForeverMethod(StrategyParserHandler handler, String[] elements) throws StrategyParserException {
        if (elements.length == 1) {
            handler.doForeverStart();
        } else {
            throwSyntaxErrorException();
        }
    }

    private void callUntilMethod(StrategyParserHandler handler, String[] elements) throws StrategyParserException {
        if (elements.length == 2) {
            handler.untilCommandStart(elements[1]);
        } else {
            throwSyntaxErrorException();
        }
    }

    private void callRepeatMethod(StrategyParserHandler handler, String[] elements) throws StrategyParserException {
        if (elements.length == 2) {
            try {
                handler.repeatCommandStart(Integer.parseInt(elements[1]));
            } catch (NumberFormatException e) {
                throwSyntaxErrorException();
            }
        } else {
            throwSyntaxErrorException();
        }
    }

    private void callContinueMethod(StrategyParserHandler handler, String[] elements) throws StrategyParserException {
        if (elements.length == 2) {
            try {
                handler.continueCommand(Integer.parseInt(elements[1]));
            } catch (NumberFormatException e) {
                throwSyntaxErrorException();
            }
        } else {
            throwSyntaxErrorException();
        }
    }

    private void callStopMethod(StrategyParserHandler handler, String[] elements) throws StrategyParserException {
        if (elements.length == 1) {
            handler.stopCommand();
        } else {
            throwSyntaxErrorException();
        }
    }

    private void callFollowMethod(StrategyParserHandler handler, String[] elements) throws StrategyParserException {
        if (elements.length == 4) {
            handler.followCommand(elements[1], toDoubleArray(2, elements));
        } else {
            throwSyntaxErrorException();
        }

    }


    private void callUnSignalMethod(StrategyParserHandler handler, String[] elements) throws StrategyParserException {
        if (elements.length == 2) {
            handler.unsignalCommand(elements[1]);
        } else {
            throwSyntaxErrorException();
        }
    }

    private void callSignalMethod(StrategyParserHandler handler, String[] elements) throws StrategyParserException {
        if (elements.length == 2) {
            handler.signalCommand(elements[1]);
        } else {
            throwSyntaxErrorException();
        }
    }

    private void callMoveMethods(StrategyParserHandler handler, String[] elements) throws StrategyParserException {
        if (elements.length > 1) {
            if (elements[1].equals("RANDOM")) {
                callMoveRandomMethod(handler, elements);
            } else {
                callMoveMethod(handler, elements);
            }
        } else {
            throwSyntaxErrorException();
        }
    }

    private void callMoveMethod(StrategyParserHandler handler, String[] elements) throws StrategyParserException {
        if (elements.length == 6) {
            handler.moveCommand(toDoubleArray(1,elements));
        } else {
            throwSyntaxErrorException();
        }
    }

    private void callMoveRandomMethod(StrategyParserHandler handler, String[] elements) throws StrategyParserException {
        if (elements.length == 6) {
            handler.moveRandomCommand(toDoubleArray(2,elements));
        } else {
            throwSyntaxErrorException();
        }
    }

    private double[] toDoubleArray(int from, String[] elements) throws StrategyParserException {
        try {
            double[] result = new double[elements.length-from];
            for(int i=0;i<result.length;i++) {
                result[i] = Double.parseDouble(elements[from+i]);
            }
            return result;
        } catch (NumberFormatException e) {
            throwSyntaxErrorException();
        }
        return null;
    }

    private void throwSyntaxErrorException() throws StrategyParserException {
        throw new StrategyParserException(String.format("Syntax error at line %d", counter));
    }

}
