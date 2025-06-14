package de.ostfale.qk.parser.match.api;

import de.ostfale.qk.domain.discipline.DisciplineType;
import de.ostfale.qk.parser.match.internal.model.Match;
import de.ostfale.qk.parser.match.internal.model.MatchInfoRawModel;
import org.htmlunit.html.HtmlElement;

import java.util.List;

public interface MatchParser {

    Match parseSingleMatch(HtmlElement content);

    Match parseDoubleMatch(HtmlElement content);

    Match parseMixedMatch(HtmlElement content);

    MatchInfoRawModel parseMatchGroupInfo(HtmlElement matchGroup);

    List<Match> parseMatchDiscipline(DisciplineType disciplineType, List<HtmlElement> matchGroups);
}
