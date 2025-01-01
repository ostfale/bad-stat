package de.ostfale.qk.parser.match.api;

import de.ostfale.qk.parser.match.internal.model.Match;
import de.ostfale.qk.parser.match.internal.model.MatchInfoDTO;
import org.htmlunit.html.HtmlDivision;
import org.htmlunit.html.HtmlElement;

public interface MatchParser {

    Match parseSingleMatch(HtmlDivision content);

    Match parseDoubleMatch(HtmlDivision content);

    Match parseMixedMatch(HtmlDivision content);

    MatchInfoDTO parseMatchGroupInfo(HtmlElement matchGroup);
}
