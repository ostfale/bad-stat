package de.ostfale.qk.parser.web.set;

import de.ostfale.qk.domain.set.MatchSet;
import de.ostfale.qk.parser.HtmlParserException;
import de.ostfale.qk.parser.web.BaseParser;

import java.util.List;

public interface SetParser extends BaseParser {

    List<MatchSet> parseSets(String[] rawSetElements) throws HtmlParserException;
}
