package de.ostfale.qk.parser;

import java.util.Arrays;

import java.util.stream.Collectors;

public class HtmlParserException extends Exception {

    private ParsedComponent parsedComponent = ParsedComponent.UNKNOWN;
    private final Exception exception;

    public HtmlParserException(ParsedComponent parsedComponent, Exception e) {
        super(e);
        this.parsedComponent = parsedComponent;
        this.exception = e;
    }

    public HtmlParserException(ParsedComponent parsedComponent, String message) {
        super(message);
        this.parsedComponent = parsedComponent;
        this.exception = new Exception(message);
    }

    public String getParserError() {
        var stacktraceAsString = Arrays.stream(getStackTrace())
                .limit(5)
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n\t\t"));


        return "\nHtmlParserException: \n\tComponent: " + parsedComponent + "\n\tMessage: " + exception.getMessage() + "\n\tStacktrace:" + stacktraceAsString;
    }
}
