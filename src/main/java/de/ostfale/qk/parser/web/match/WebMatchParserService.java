package de.ostfale.qk.parser.web.match;

import de.ostfale.qk.domain.match.DisciplineMatch;
import de.ostfale.qk.parser.web.set.MatchSetParserService;
import de.ostfale.qk.parser.web.BaseParser;
import de.ostfale.qk.parser.web.player.MatchPlayerParserService;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.htmlunit.html.HtmlElement;


@ApplicationScoped
public class WebMatchParserService implements WebMatchParser, BaseParser {

    private final MatchSetParserService matchSetParserService;
    private final MatchPlayerParserService matchPlayerParserService;

    public WebMatchParserService( MatchSetParserService matchSetParserService, MatchPlayerParserService matchPlayerParserService) {
        this.matchSetParserService = matchSetParserService;
        this.matchPlayerParserService = matchPlayerParserService;
    }

    @Override
    public DisciplineMatch parseSingleMatch(HtmlElement matchGroupElement) {
        Log.debug("WebMatchParserService :: parse single match website");
        DisciplineMatch disciplineMatch = parseMatchCommon(matchGroupElement);
        parseMatchData(disciplineMatch, matchGroupElement);
        matchPlayerParserService.parseMatchPlayers(disciplineMatch, matchGroupElement);
        return disciplineMatch;
    }

    @Override
    public DisciplineMatch parseDoubleMatch(HtmlElement matchGroupElement) {
        Log.debug("WebMatchParserService :: parse double match website");
        DisciplineMatch disciplineMatch = parseMatchCommon(matchGroupElement);
        parseMatchData(disciplineMatch, matchGroupElement);
        matchPlayerParserService.parseMatchPlayers(disciplineMatch, matchGroupElement);
        return disciplineMatch;
    }

    @Override
    public DisciplineMatch parseMixedMatch(HtmlElement matchGroupElement) {
        Log.debug("WebMatchParserService :: parse mixed match website");
        DisciplineMatch disciplineMatch = parseMatchCommon(matchGroupElement);
        parseMatchData(disciplineMatch, matchGroupElement);
        matchPlayerParserService.parseMatchPlayers(disciplineMatch, matchGroupElement);
        return disciplineMatch;
    }

    private DisciplineMatch parseMatchCommon(HtmlElement matchGroupElement) {
        DisciplineMatch match = new DisciplineMatch();
        HtmlElement matchRoundNameDiv = htmlStructureParser.getMatchRoundNameElement(matchGroupElement);
        String matchRoundName = matchRoundNameDiv != null ? matchRoundNameDiv.asNormalizedText() : "";
        match.setRoundName(matchRoundName);
        return match;
    }

    private void parseMatchData(DisciplineMatch disciplineMatch, HtmlElement matchGroupElement) {
        var matchSets = matchSetParserService.parseMatchSets(matchGroupElement);
        disciplineMatch.getMatchSets().addAll(matchSets);
    }
}
