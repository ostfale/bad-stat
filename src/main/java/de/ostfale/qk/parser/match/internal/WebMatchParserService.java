package de.ostfale.qk.parser.match.internal;

import de.ostfale.qk.domain.match.DisciplineMatch;
import de.ostfale.qk.parser.match.api.WebMatchParser;
import de.ostfale.qk.parser.player.MatchPlayerParser;
import de.ostfale.qk.parser.set.MatchSetParser;
import de.ostfale.qk.parser.web.HtmlStructureParser;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.htmlunit.html.HtmlElement;


@ApplicationScoped
public class WebMatchParserService implements WebMatchParser {

    @Inject
    HtmlStructureParser htmlStructureParser;

    @Inject
    MatchSetParser matchSetParser;

    @Inject
    MatchPlayerParser matchPlayerParser;

    @Override
    public DisciplineMatch parseSingleMatch(HtmlElement matchGroupElement) {
        Log.debug("WebMatchParserService :: parse single match website");
        DisciplineMatch disciplineMatch =  parseMatchCommon(matchGroupElement);
        parseMatchData(disciplineMatch,matchGroupElement);


        return disciplineMatch;
    }

    @Override
    public DisciplineMatch parseDoubleMatch(HtmlElement matchGroupElement) {
        Log.debug("WebMatchParserService :: parse double match website");
        return parseMatchCommon(matchGroupElement);
    }

    @Override
    public DisciplineMatch parseMixedMatch(HtmlElement matchGroupElement) {
        Log.debug("WebMatchParserService :: parse mixed match website");
        return parseMatchCommon(matchGroupElement);
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
        System.out.println("dddd");
    }
}
