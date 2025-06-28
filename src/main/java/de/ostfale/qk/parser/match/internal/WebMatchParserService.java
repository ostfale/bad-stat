package de.ostfale.qk.parser.match.internal;

import de.ostfale.qk.domain.match.DisciplineMatch;
import de.ostfale.qk.parser.BaseParser;
import de.ostfale.qk.parser.match.api.WebMatchParser;
import de.ostfale.qk.parser.player.MatchPlayerParser;
import de.ostfale.qk.parser.set.MatchSetParser;
import de.ostfale.qk.parser.web.HtmlStructureParser;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.htmlunit.html.HtmlElement;


@ApplicationScoped
public class WebMatchParserService implements WebMatchParser, BaseParser {

    private final HtmlStructureParser htmlStructureParser;

    private final MatchSetParser matchSetParser;

    private final MatchPlayerParser matchPlayerParser;

    public WebMatchParserService(HtmlStructureParser htmlStructureParser, MatchSetParser matchSetParser, MatchPlayerParser matchPlayerParser) {
        this.htmlStructureParser = htmlStructureParser;
        this.matchSetParser = matchSetParser;
        this.matchPlayerParser = matchPlayerParser;
    }

    @Override
    public DisciplineMatch parseSingleMatch(HtmlElement matchGroupElement) {
        Log.debug("WebMatchParserService :: parse single match website");
        DisciplineMatch disciplineMatch = parseMatchCommon(matchGroupElement);
        parseMatchData(disciplineMatch, matchGroupElement);
        matchPlayerParser.parseMatchPlayers(disciplineMatch, matchGroupElement);
        return disciplineMatch;
    }

    @Override
    public DisciplineMatch parseDoubleMatch(HtmlElement matchGroupElement) {
        Log.debug("WebMatchParserService :: parse double match website");
        DisciplineMatch disciplineMatch = parseMatchCommon(matchGroupElement);
        parseMatchData(disciplineMatch, matchGroupElement);
        matchPlayerParser.parseMatchPlayers(disciplineMatch, matchGroupElement);
        return disciplineMatch;
    }

    @Override
    public DisciplineMatch parseMixedMatch(HtmlElement matchGroupElement) {
        Log.debug("WebMatchParserService :: parse mixed match website");
        DisciplineMatch disciplineMatch = parseMatchCommon(matchGroupElement);
        parseMatchData(disciplineMatch, matchGroupElement);
        matchPlayerParser.parseMatchPlayers(disciplineMatch, matchGroupElement);
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
        var matchSets = matchSetParser.parseMatchSets(matchGroupElement);
        disciplineMatch.getMatchSets().addAll(matchSets);
    }
}
