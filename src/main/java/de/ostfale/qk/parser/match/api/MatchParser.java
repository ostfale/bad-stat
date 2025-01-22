package de.ostfale.qk.parser.match.api;

import de.ostfale.qk.parser.discipline.internal.model.Discipline;
import de.ostfale.qk.parser.match.internal.model.Match;
import de.ostfale.qk.parser.match.internal.model.MatchInfoDTO;
import org.htmlunit.html.HtmlElement;

import java.util.List;

public interface MatchParser {

    Match parseSingleMatch(HtmlElement content);

    Match parseDoubleMatch(HtmlElement content);

    Match parseMixedMatch(HtmlElement content);

    MatchInfoDTO parseMatchGroupInfo(HtmlElement matchGroup);

    List<Match> parseMatchDiscipline(Discipline discipline, List<HtmlElement> matchGroups);
}
