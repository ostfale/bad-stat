package de.ostfale.qk.parser.match.api;

import de.ostfale.qk.parser.match.internal.model.Match;
import org.htmlunit.html.HtmlDivision;

public interface MatchParser {

    Match parseSingleMatch(HtmlDivision content);

    Match parseDoubleMatch(HtmlDivision content);
}
