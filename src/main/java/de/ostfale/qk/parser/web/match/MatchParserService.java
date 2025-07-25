package de.ostfale.qk.parser.web.match;

import de.ostfale.qk.domain.discipline.DisciplineType;
import de.ostfale.qk.domain.match.DisciplineMatch;
import de.ostfale.qk.domain.match.MatchResultType;
import de.ostfale.qk.domain.set.MatchSet;
import de.ostfale.qk.parser.HtmlParserException;
import de.ostfale.qk.parser.ParsedComponent;
import de.ostfale.qk.parser.web.HtmlStructureParser;
import de.ostfale.qk.parser.web.set.SetParser;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.htmlunit.html.HtmlElement;

import java.util.Objects;

@ApplicationScoped
public class MatchParserService implements MatchParser {

    private final SetParser setParser;

    public MatchParserService(SetParser setParser) {
        this.setParser = setParser;
    }

    @Override
    public DisciplineMatch parseMatch(DisciplineType disciplineType, HtmlElement matchGroupElement) throws HtmlParserException {
        Log.debug("MatchParserService :: parse match data and round name");
        DisciplineMatch disciplineMatch = new DisciplineMatch();
        disciplineMatch.setRoundName(extractMatchRoundName(matchGroupElement));
        String[] matchStringElements = extractMatchBodyElements(matchGroupElement);
        MatchResultAnalyzer analyzer = new MatchResultAnalyzer(matchStringElements);
        var sets = setParser.parseSets(matchStringElements);
        disciplineMatch.getMatchSets().addAll(sets);

        switch (disciplineType) {
            case SINGLE -> parseSingleMatch(disciplineMatch, analyzer);
            case DOUBLE, MIXED -> parseTeamMatch(disciplineMatch, analyzer);
            default -> throw new HtmlParserException(ParsedComponent.MATCH, "Unknown discipline type: " + disciplineType);
        }
        return disciplineMatch;
    }

    private void parseSingleMatch(DisciplineMatch disciplineMatch, MatchResultAnalyzer analyzer) throws HtmlParserException {
        Log.debug("MatchParserService :: parse single match");

        if (analyzer.isByeMatch()) {
            assignSinglePlayerNamesForBye(disciplineMatch, analyzer);
            return;
        }
        assignSinglePlayerNamesWithMarker(disciplineMatch, analyzer);
    }

    private void assignSinglePlayerNamesWithMarker(DisciplineMatch disciplineMatch, MatchResultAnalyzer analyzer) throws HtmlParserException {
        Log.debug("MatchParserService :: assignSinglePlayerNamesWithMarker");
        var MARKER_POS_1 = analyzer.getMarkerPosition() == 1;

        if (MARKER_POS_1) {
            disciplineMatch.setPlayerOneName(analyzer.getFirstPlayerName(true));
            disciplineMatch.setPlayerTwoName(analyzer.getSecondPlayerName(false));
        } else {
            disciplineMatch.setPlayerOneName(analyzer.getFirstPlayerName(false));
            disciplineMatch.setPlayerTwoName(analyzer.getSecondPlayerName(true));
        }
    }

    private void parseTeamMatch(DisciplineMatch disciplineMatch, MatchResultAnalyzer analyzer) throws HtmlParserException {
        Log.debug("MatchParserService :: parse team match");

        if (analyzer.isByeMatch()) {
            assignTeamNamesForBye(disciplineMatch, analyzer);
            return;
        }

        if (analyzer.isWalkOverMatch()) {
            assignTeamNamesForWalkover(disciplineMatch, analyzer);
            return;
        }

        assignTeamPlayerNamesWithMarker(disciplineMatch, analyzer);
    }

    private void assignTeamNamesForWalkover(DisciplineMatch disciplineMatch, MatchResultAnalyzer analyzer) throws HtmlParserException {
        Log.debug("MatchParserService :: assignTeamNamesForWalkover");

        var MARKER_POS_2 = 2;
        var matchMarkerPosition = analyzer.getMarkerPosition();

        if (matchMarkerPosition == MARKER_POS_2) {
            disciplineMatch.setPlayerOneName(analyzer.getFirstPlayerName(false));
            disciplineMatch.setPartnerOneName(analyzer.getSecondPlayerName(true));
            disciplineMatch.setPlayerTwoName(analyzer.getThirdPlayerName(false));
            disciplineMatch.setPartnerTwoName(analyzer.getFourthPlayerName(false));
            disciplineMatch.getMatchSets().clear();
            disciplineMatch.getMatchSets().add(new MatchSet(MatchResultType.WALKOVER));
        } else {
            disciplineMatch.setPlayerOneName(MatchResultType.BYE.getDisplayName());
            disciplineMatch.setPlayerTwoName(analyzer.getFirstPlayerName(false));
            disciplineMatch.setPartnerTwoName(analyzer.getSecondPlayerName(true));
        }
    }

    private void assignTeamPlayerNamesWithMarker(DisciplineMatch disciplineMatch, MatchResultAnalyzer analyzer) throws HtmlParserException {
        Log.debug("MatchParserService :: assignTeamPlayerNamesWithMarker");

        var isFirstTeamMarked = analyzer.getMarkerPosition() == 2;

        var firstPlayer = analyzer.getFirstPlayerName(false);
        var secondPlayer = isFirstTeamMarked ? analyzer.getSecondPlayerName(true) : analyzer.getSecondPlayerName(false);
        var thirdPlayer = analyzer.getThirdPlayerName(false);
        var fourthPlayer = !isFirstTeamMarked ? analyzer.getFourthPlayerName(false) : analyzer.getFourthPlayerName(true);

        disciplineMatch.setPlayerOneName(firstPlayer);
        disciplineMatch.setPartnerOneName(secondPlayer);
        disciplineMatch.setPlayerTwoName(thirdPlayer);
        disciplineMatch.setPartnerTwoName(fourthPlayer);
    }

    private void assignSinglePlayerNamesForBye(DisciplineMatch disciplineMatch, MatchResultAnalyzer analyzer) throws HtmlParserException {
        Log.debug("MatchParserService :: handle single player has rast");
        var markerPosition = analyzer.getMarkerPosition();

        if (markerPosition == 2 || markerPosition == 3) {
            disciplineMatch.setPlayerOneName(MatchResultType.BYE.getDisplayName());
            disciplineMatch.setPlayerTwoName(analyzer.getFirstPlayerName(true));
        } else {
            disciplineMatch.setPlayerOneName(analyzer.getFirstPlayerName(true));
            disciplineMatch.setPlayerTwoName(MatchResultType.BYE.getDisplayName());
        }
    }

    private void assignTeamNamesForBye(DisciplineMatch disciplineMatch, MatchResultAnalyzer analyzer) throws HtmlParserException {
        Log.debug("MatchParserService :: handle team player have rast");
        var markerPosition = analyzer.getMarkerPosition();

        if (markerPosition == 2) {
            disciplineMatch.setPlayerOneName(analyzer.getFirstPlayerName(false));
            disciplineMatch.setPartnerOneName(analyzer.getSecondPlayerName(true));
            disciplineMatch.setPlayerTwoName(MatchResultType.BYE.getDisplayName());
        } else {
            disciplineMatch.setPlayerOneName(MatchResultType.BYE.getDisplayName());
            disciplineMatch.setPlayerTwoName(analyzer.getFirstPlayerName(false));
            disciplineMatch.setPartnerTwoName(analyzer.getSecondPlayerName(true));
        }
    }

    private String extractMatchRoundName(HtmlElement matchGroupElement) {
        Log.debug("MatchParserService :: extractMatchRoundName");
        HtmlElement matchRoundNameDiv = htmlStructureParser.getMatchRoundNameElement(matchGroupElement);
        return matchRoundNameDiv != null ? matchRoundNameDiv.asNormalizedText() : "";
    }

    private String[] extractMatchBodyElements(HtmlElement matchGroupElement) {
        Log.debug("BaseParser :: extractMatchBodyElements");
        Objects.requireNonNull(matchGroupElement, "matchGroupElement must not be null");
        HtmlElement matchBody = new HtmlStructureParser().getMatchBodyElement(matchGroupElement);
        return matchBody.asNormalizedText().split(MATCH_RESULT_SEPARATOR);
    }
}
