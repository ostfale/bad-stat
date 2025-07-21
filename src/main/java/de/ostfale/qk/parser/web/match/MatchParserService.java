package de.ostfale.qk.parser.web.match;

import de.ostfale.qk.domain.discipline.DisciplineType;
import de.ostfale.qk.domain.match.DisciplineMatch;
import de.ostfale.qk.domain.match.MatchResultType;
import de.ostfale.qk.parser.HtmlParserException;
import de.ostfale.qk.parser.ParsedComponent;
import de.ostfale.qk.parser.web.HtmlStructureParser;
import de.ostfale.qk.parser.web.set.SetParser;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.ArrayUtils;
import org.htmlunit.html.HtmlElement;

import java.util.Arrays;
import java.util.Objects;

@ApplicationScoped
public class MatchParserService implements MatchParser {

    private static final String WINNER_MARKER = "W";
    private static final String LOSER_MARKER = "L";

    private static final String BYE_MARKER = MatchResultType.BYE.getDisplayName();
    private static final int BYE_MARKER_INDEX_FIRST = 0;
    private static final int BYE_MARKER_INDEX_FOUR = 3;


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
    public DisciplineMatch parseMatch(DisciplineType disciplineType,HtmlElement matchGroupElement) throws HtmlParserException {
        Log.debug("MatchParserService :: parse match data and round name");
        DisciplineMatch disciplineMatch = new DisciplineMatch();
        String[] matchStringElements = extractMatchBodyElements(matchGroupElement);
        disciplineMatch.setRoundName(extractMatchRoundName(matchGroupElement));
        var sets = setParser.parseSets(matchStringElements);
        disciplineMatch.getMatchSets().addAll(sets);

        switch (disciplineType) {
            case SINGLE -> parseSingleMatch(disciplineMatch, matchStringElements);
            case DOUBLE -> parseDoubleMatch(disciplineMatch, matchStringElements);
            case MIXED -> parseMixedMatch(disciplineMatch, matchStringElements);
            default -> throw new HtmlParserException(ParsedComponent.MATCH, "Unknown discipline type: " + disciplineType);
        }
        return disciplineMatch;
    }

    private void parseSingleMatch(DisciplineMatch disciplineMatch, String[] matchStringElements) throws HtmlParserException {
        Log.debug("MatchParserService :: parse single match");
        MarkerValueAndIndex markerValueAndIndex = findWinnerLoserMarker(matchStringElements);
        var firstPlayerName = matchStringElements[FIRST_SINGLE_PLAYER_INDEX];
        var secondPlayerName = matchStringElements[SECOND_SINGLE_PLAYER_INDEX];
        disciplineMatch.setPlayerOneName(markerValueAndIndex.getMarkerDisplayValue(firstPlayerName));
        disciplineMatch.setPlayerTwoName(secondPlayerName);
    }

    private void parseDoubleMatch(DisciplineMatch disciplineMatch, String[] matchStringElements) throws HtmlParserException {
        Log.debug("MatchParserService :: parse double match");
        MarkerValueAndIndex markerValueAndIndex = findWinnerLoserMarker(matchStringElements);

        if (isBye(matchStringElements)) {
            handleTeamHasRast(disciplineMatch, matchStringElements);
            return;
        }

        if (markerValueAndIndex.markerIndex == DOUBLES_MIXED_MATCH_INDEX) {
            String secondNameExtension = matchStringElements[FIRST_DOUBLE_MIXED_PARTNER_INDEX];
            disciplineMatch.setPlayerOneName(matchStringElements[FIRST_DOUBLE_MIXED_PLAYER_INDEX]);
            disciplineMatch.setPartnerOneName(markerValueAndIndex.getMarkerDisplayValue(secondNameExtension));
            disciplineMatch.setPlayerTwoName(matchStringElements[3]);
            disciplineMatch.setPartnerTwoName(matchStringElements[4]);
        }
        if (markerValueAndIndex.markerIndex == DOUBLES_MIXED_MATCH_ALT_INDEX) {
            disciplineMatch.setPlayerOneName(matchStringElements[FIRST_DOUBLE_MIXED_PLAYER_INDEX]);
            disciplineMatch.setPartnerOneName(matchStringElements[FIRST_DOUBLE_MIXED_PARTNER_INDEX]);
            disciplineMatch.setPlayerTwoName(matchStringElements[2]);
            disciplineMatch.setPartnerTwoName(markerValueAndIndex.getMarkerDisplayValue(matchStringElements[3]));
        }
    }

    private void handleTeamHasRast(DisciplineMatch disciplineMatch, String[] matchStringElements) {
        Log.debug("MatchParserService :: handle double or mixed team has rast");
        if (matchStringElements[BYE_MARKER_INDEX_FIRST].equalsIgnoreCase(BYE_MARKER)) {
            var firstPlayerName = matchStringElements[1];
            var secondPlayerName = matchStringElements[2] + " (W)";
            disciplineMatch.setPlayerOneName(MatchResultType.BYE.getDisplayName());
            disciplineMatch.setPlayerTwoName(firstPlayerName);
            disciplineMatch.setPartnerTwoName(secondPlayerName);
        }else if (matchStringElements[BYE_MARKER_INDEX_FOUR].equalsIgnoreCase(BYE_MARKER)) {
            var firstPlayerName = matchStringElements[0];
            var firstPartnerName = matchStringElements[1] + " (W)";
            disciplineMatch.setPlayerOneName(firstPlayerName);
            disciplineMatch.setPartnerOneName(firstPartnerName);
            disciplineMatch.setPlayerTwoName(MatchResultType.BYE.getDisplayName());
        }
    }

    private void parseMixedMatch(DisciplineMatch disciplineMatch, String[] matchStringElements) throws HtmlParserException {
        Log.debug("MatchParserService :: parse mixed match");
        MarkerValueAndIndex markerValueAndIndex = findWinnerLoserMarker(matchStringElements);

        if (isBye(matchStringElements)) {
            handleTeamHasRast(disciplineMatch, matchStringElements);
            return;
        }

        if (markerValueAndIndex.markerIndex == DOUBLES_MIXED_MATCH_INDEX) {
            String secondNameExtension = matchStringElements[FIRST_DOUBLE_MIXED_PARTNER_INDEX];
            disciplineMatch.setPlayerOneName(matchStringElements[FIRST_DOUBLE_MIXED_PLAYER_INDEX]);
            disciplineMatch.setPartnerOneName(markerValueAndIndex.getMarkerDisplayValue(secondNameExtension));
            disciplineMatch.setPlayerTwoName(matchStringElements[3]);
            disciplineMatch.setPartnerTwoName(matchStringElements[4]);
        }
        if (markerValueAndIndex.markerIndex == DOUBLES_MIXED_MATCH_ALT_INDEX) {
            disciplineMatch.setPlayerOneName(matchStringElements[FIRST_DOUBLE_MIXED_PLAYER_INDEX]);
            disciplineMatch.setPartnerOneName(matchStringElements[FIRST_DOUBLE_MIXED_PARTNER_INDEX]);
            disciplineMatch.setPlayerTwoName(matchStringElements[2]);
            disciplineMatch.setPartnerTwoName(markerValueAndIndex.getMarkerDisplayValue(matchStringElements[3]));
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

    private boolean isBye(String[] matchStringElements) {
        Log.debug("MatchParserService :: isRast");
        return Arrays.stream(matchStringElements).anyMatch(element->element.equalsIgnoreCase("Rast"));
    }

    private record MarkerValueAndIndex(
            int markerIndex,
            String markerValue
    ) {

        public String getMarkerDisplayValue(String playerName) {
            return String.format("%s (%s)", playerName, markerValue);
        }
    }
}
