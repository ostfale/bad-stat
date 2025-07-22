package de.ostfale.qk.parser.web.match;

import de.ostfale.qk.domain.discipline.DisciplineType;
import de.ostfale.qk.domain.match.DisciplineMatch;
import de.ostfale.qk.parser.HtmlParserException;
import de.ostfale.qk.parser.web.BaseParser;
import org.htmlunit.html.HtmlElement;

public interface MatchParser extends BaseParser {

    DisciplineMatch parseMatch(DisciplineType disciplineType,HtmlElement matchGroupElement) throws HtmlParserException;

    String MATCH_RESULT_SEPARATOR = "\n";
}
