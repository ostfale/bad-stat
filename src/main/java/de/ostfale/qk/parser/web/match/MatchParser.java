package de.ostfale.qk.parser.web.match;

import de.ostfale.qk.domain.match.DisciplineMatch;
import de.ostfale.qk.parser.web.BaseParser;
import de.ostfale.qk.parser.web.HtmlStructureParser;
import io.quarkus.logging.Log;
import org.htmlunit.html.HtmlElement;

import java.util.Objects;

public interface MatchParser extends BaseParser {

    DisciplineMatch parseMatch(HtmlElement matchGroupElement);

    String MATCH_RESULT_SEPARATOR = "\n";

    default String[] extractMatchBodyElements1(HtmlElement matchGroupElement) {
        Log.debug("BaseParser :: extractMatchBodyElements");
        Objects.requireNonNull(matchGroupElement, "matchGroupElement must not be null");
        HtmlElement matchBody = new HtmlStructureParser().getMatchBodyElement(matchGroupElement);
        return matchBody.asNormalizedText().split(MATCH_RESULT_SEPARATOR);
    }
}
