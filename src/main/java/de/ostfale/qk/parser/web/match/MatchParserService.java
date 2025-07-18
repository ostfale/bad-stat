package de.ostfale.qk.parser.web.match;

import de.ostfale.qk.domain.match.DisciplineMatch;
import de.ostfale.qk.parser.web.HtmlStructureParser;
import de.ostfale.qk.parser.web.set.SetParser;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.htmlunit.html.HtmlElement;

import java.util.Objects;

@ApplicationScoped
public class MatchParserService implements MatchParser {

    private static final String WINNER_MARKER = "W";
    private static final String LOSER_MARKER = "L";
    private static final int SINGLE_MATCH_MARKER_INDEX = 1;
    private static final int DOUBLES_MIXED_MATCH_INDEX = 3;

    private static final int FIRST_SINGLE_PLAYER_INDEX = 0;
    private static final int SECOND_SINGLE_PLAYER_INDEX = 2;

    private static final int FIRST_DOUBLE_MIXED_PLAYER_INDEX = 0;
    private static final int FIRST_DOUBLE_MIXED_PARTNER_INDEX = 1;
    private static final int SECOND_DOUBLE_MIXED_PLAYER_INDEX = 3;
    private static final int SECOND_DOUBLE_MIXED_PARTNER_INDEX = 4;

    private final SetParser setParser;

    public MatchParserService(SetParser setParser) {
        this.setParser = setParser;
    }

    @Override
    public DisciplineMatch parseMatch(HtmlElement matchGroupElement) {
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
            disciplineMatch.setPlayerOneName(matchStringElements[FIRST_DOUBLE_MIXED_PLAYER_INDEX]);
            disciplineMatch.setPartnerOneName(matchStringElements[FIRST_DOUBLE_MIXED_PARTNER_INDEX]);
            disciplineMatch.setPlayerTwoName(matchStringElements[SECOND_DOUBLE_MIXED_PLAYER_INDEX]);
            disciplineMatch.setPartnerTwoName(matchStringElements[SECOND_DOUBLE_MIXED_PARTNER_INDEX]);
        }
        var sets = setParser.parseSets(matchStringElements);
        disciplineMatch.getMatchSets().addAll(sets);
        return disciplineMatch;
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

    private record PlayerNames(
            String name
    ) {
    }
}
