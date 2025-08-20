package de.ostfale.qk.parser.file;

import de.ostfale.qk.parser.ParsedComponent;

import java.util.Arrays;
import java.util.stream.Collectors;

public class FileParserException extends Exception {

    private final ParsedComponent parsedComponent;
    private final Exception exception;

    public FileParserException(ParsedComponent parsedComponent, Exception e) {
        super(e);
        this.parsedComponent = parsedComponent;
        this.exception = e;
    }

    public FileParserException(ParsedComponent parsedComponent, String message) {
        super(message);
        this.parsedComponent = parsedComponent;
        this.exception = new Exception(message);
    }

    public String getParserError() {
        var stacktraceAsString = Arrays.stream(getStackTrace())
                .limit(5)
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n\t\t"));

        return "\nFileParserException: \n\tComponent: " + parsedComponent + "\n\tMessage: " + exception.getMessage() + "\n\tStacktrace:" + stacktraceAsString;
    }
}
