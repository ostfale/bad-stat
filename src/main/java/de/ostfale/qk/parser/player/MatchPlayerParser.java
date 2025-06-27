package de.ostfale.qk.parser.player;

import de.ostfale.qk.domain.match.DisciplineMatch;
import de.ostfale.qk.parser.BaseParser;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.htmlunit.html.HtmlElement;

import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class MatchPlayerParser implements BaseParser {

    private static final String WINNER_MARKER = "W";
    private static final String LOSER_MARKER = "L";
    private static final String RETIRED_MARKER = "Retired.";
    private static final String RETIRED_MARKER_LOST = "Retired. L";
    private static final String H2H_MARKER = "H2H";

    public void parseMatchPlayer(DisciplineMatch disciplineMatch, HtmlElement matchGroupElement) {
        Log.debug("MatchPlayerParser :: parse match player");
        String[] matchBodyElements = extractMatchBodyElements(matchGroupElement);
        List<PlayerNames> playerNames = Arrays.stream(matchBodyElements)
                .filter(this::isValidPlayerString)
                .map(PlayerNames::new)
                .toList();
        disciplineMatch.setPlayerOneName(playerNames.getFirst().name);
        disciplineMatch.setPlayerTwoName(playerNames.getLast().name);
    }

    private boolean isValidPlayerString(String str) {
        return !str.equalsIgnoreCase(WINNER_MARKER)
                && !str.equalsIgnoreCase(LOSER_MARKER)
                && !str.equalsIgnoreCase(RETIRED_MARKER_LOST)
                && !str.equalsIgnoreCase(RETIRED_MARKER)
                && !str.equalsIgnoreCase(H2H_MARKER)
                && !isNumeric(str);
    }

    private record PlayerNames(
            String name
    ) {
    }
}
