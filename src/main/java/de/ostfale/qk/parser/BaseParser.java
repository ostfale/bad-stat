package de.ostfale.qk.parser;

import de.ostfale.qk.parser.web.HtmlStructureParser;
import io.quarkus.logging.Log;
import org.htmlunit.html.HtmlElement;

import java.util.Objects;

public interface BaseParser {

    String MATCH_RESULT_SEPARATOR = "\n";

    default boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        return str.trim().matches("\\d+");
    }

    default String[] extractMatchBodyElements(HtmlElement matchGroupElement) {
        Log.debug("BaseParser :: extractMatchBodyElements");
        Objects.requireNonNull(matchGroupElement, "matchGroupElement must not be null");
        HtmlElement matchBody = new HtmlStructureParser().getMatchBodyElement(matchGroupElement);
        return matchBody.asNormalizedText().split(MATCH_RESULT_SEPARATOR);
    }
}
