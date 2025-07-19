package de.ostfale.qk.parser.web.match;

import de.ostfale.qk.domain.match.DisciplineMatch;
import de.ostfale.qk.parser.HtmlParserException;
import de.ostfale.qk.parser.ParsedComponent;
import de.ostfale.qk.parser.web.HtmlStructureParser;
import de.ostfale.qk.parser.web.set.SetParser;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.ArrayUtils;
import org.htmlunit.html.HtmlElement;

import java.util.Objects;

@ApplicationScoped
public class MatchParserService implements MatchParser {

    private static final String WINNER_MARKER = "W";
    private static final String LOSER_MARKER = "L";
    private static final int SINGLE_MATCH_MARKER_INDEX = 1;
    private static final int DOUBLES_MIXED_MATCH_INDEX = 2;
    private static final int DOUBLES_MIXED_MATCH_ALT_INDEX = 4;

    private static final int FIRST_SINGLE_PLAYER_INDEX = 0;
    private static final int SECOND_SINGLE_PLAYER_INDEX = 2;

    private static final int FIRST_DOUBLE_MIXED_PLAYER_INDEX = 0;
    private static final int FIRST_DOUBLE_MIXED_PARTNER_INDEX = 1;

    private final SetParser setParser;

    public MatchParserService(SetParser setParser) {
        this.setParser = setParser;
    }

    @Override
    public DisciplineMatch parseMatch(HtmlElement matchGroupElement) throws HtmlParserException {
        Log.debug("MatchParserService :: parse match data and round name");
        DisciplineMatch disciplineMatch = new DisciplineMatch();
        disciplineMatch.setRoundName(extractMatchRoundName(matchGroupElement));
        String[] matchStringElements = extractMatchBodyElements(matchGroupElement);
        if (isSingleMatch(matchStringElements)) {
            Log.debug("MatchParserService :: single match");
            var marker = matchStringElements[SINGLE_MATCH_MARKER_INDEX];
            var firstPlayerName = matchStringElements[FIRST_SINGLE_PLAYER_INDEX];
            var secondPlayerName = matchStringElements[SECOND_SINGLE_PLAYER_INDEX];
            disciplineMatch.setPlayerOneName(firstPlayerName + " (" + marker + ")");
            disciplineMatch.setPlayerTwoName(secondPlayerName);
        } else {
            Log.debug("MatchParserService :: doubles or mixed match");
            MarkerValueAndIndex markerValueAndIndex = findWinnerLoserMarker(matchStringElements);
            if (markerValueAndIndex.markerIndex == DOUBLES_MIXED_MATCH_INDEX) {
                String secondNameExtension = matchStringElements[FIRST_DOUBLE_MIXED_PARTNER_INDEX] + " (" + markerValueAndIndex.markerValue + ")";
                disciplineMatch.setPlayerOneName(matchStringElements[FIRST_DOUBLE_MIXED_PLAYER_INDEX]);
                disciplineMatch.setPartnerOneName(secondNameExtension);
                disciplineMatch.setPlayerTwoName(matchStringElements[3]);
                disciplineMatch.setPartnerTwoName(matchStringElements[4]);
            }
            if (markerValueAndIndex.markerIndex == DOUBLES_MIXED_MATCH_ALT_INDEX) {
                String secondNameExtension = prepareNameExtension(matchStringElements[3], markerValueAndIndex.markerValue);
                disciplineMatch.setPlayerOneName(matchStringElements[FIRST_DOUBLE_MIXED_PLAYER_INDEX]);
                disciplineMatch.setPartnerOneName(matchStringElements[FIRST_DOUBLE_MIXED_PARTNER_INDEX]);
                disciplineMatch.setPlayerTwoName(matchStringElements[2]);
                disciplineMatch.setPartnerTwoName(secondNameExtension);
            }
        }
        var sets = setParser.parseSets(matchStringElements);
        disciplineMatch.getMatchSets().addAll(sets);
        return disciplineMatch;
    }

    private String prepareNameExtension(String name, String marker) {
        return name + " (" + marker + ")";
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

    private boolean isSingleMatch(String[] matchStringElements) {
        Log.debug("MatchParserService :: isSingleMatch");
        String secondElement = matchStringElements[SINGLE_MATCH_MARKER_INDEX];
        return secondElement.equalsIgnoreCase(WINNER_MARKER) || secondElement.equalsIgnoreCase(LOSER_MARKER);
    }

    private MarkerValueAndIndex findWinnerLoserMarker(String[] matchStringElements) throws HtmlParserException {
        for (var element : matchStringElements) {
            if (isWinnerOrLoserMarker(element)) {
                return new MarkerValueAndIndex(
                        ArrayUtils.indexOf(matchStringElements, element),
                        element
                );
            }
        }
        throw new HtmlParserException(ParsedComponent.MATCH, "Match does not contain W-L-Marker!");
    }

    private boolean isWinnerOrLoserMarker(String element) {
        return element.equalsIgnoreCase(WINNER_MARKER) ||
                element.equalsIgnoreCase(LOSER_MARKER);
    }

    private record MarkerValueAndIndex(
            int markerIndex,
            String markerValue
    ) {
    }
}
