package de.ostfale.qk.parser.web.match;

import de.ostfale.qk.domain.match.DisciplineMatch;
import org.htmlunit.html.HtmlElement;

public interface WebMatchParser {

    DisciplineMatch parseSingleMatch(HtmlElement matchGroupElement);

    DisciplineMatch parseDoubleMatch(HtmlElement matchGroupElement);

    DisciplineMatch parseMixedMatch(HtmlElement matchGroupElement);
}
