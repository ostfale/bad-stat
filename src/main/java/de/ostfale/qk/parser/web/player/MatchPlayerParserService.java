package de.ostfale.qk.parser.web.player;

import de.ostfale.qk.domain.match.DisciplineMatch;
import de.ostfale.qk.domain.match.MatchResultType;
import de.ostfale.qk.parser.web.match.MatchParser;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.htmlunit.html.HtmlElement;

import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class MatchPlayerParserService implements MatchParser {

    private static final String WINNER_MARKER = "W";
    private static final String LOSER_MARKER = "L";
    private static final String RETIRED_MARKER = "Retired.";
    private static final String RETIRED_MARKER_LOST = "Retired. L";
    private static final String H2H_MARKER = "H2H";

    public void parseMatchPlayers(DisciplineMatch disciplineMatch, HtmlElement matchGroupElement) {
        Log.debug("MatchPlayerParser :: Parsing match players");

        final int SINGLES_PLAYER_COUNT = 2;
        final int DOUBLES_MIXED_PLAYER_COUNT_BYE = 3;
        final int DOUBLES_MIXED_PLAYER_COUNT = 4;

        String[] matchBodyElements = extractMatchBodyElements1(matchGroupElement);

        List<PlayerNames> playerNames = Arrays.stream(matchBodyElements)
                .filter(this::isValidPlayerString)
                .map(PlayerNames::new)
                .toList();

        if (playerNames.isEmpty()) {
            Log.error("MatchPlayerParser :: No valid players found in match");
            return;
        }

        if (containsWalkoverPlayer(playerNames)) {
            Log.debug("MatchPlayerParser :: Match contains walkover player");
            handleMatchWithWalkover(disciplineMatch, playerNames);
            return;
        }

        int playerCount = playerNames.size();
        switch (playerCount) {
            case SINGLES_PLAYER_COUNT -> handleSinglesMatch(disciplineMatch, playerNames);
            case DOUBLES_MIXED_PLAYER_COUNT -> handleDoubleAndMixedMatch(disciplineMatch, playerNames);
            case DOUBLES_MIXED_PLAYER_COUNT_BYE ->handleDoubleMixedMatchWithBye(disciplineMatch, playerNames);
            default -> Log.error("MatchPlayerParser :: Invalid number of players: " + playerCount);
        }
    }

    private void handleMatchWithWalkover(DisciplineMatch disciplineMatch, List<PlayerNames> playerNames) {
    }

    private boolean containsWalkoverPlayer(List<PlayerNames> players) {
        return players.stream()
                .anyMatch(player -> player.name().equalsIgnoreCase(MatchResultType.WALKOVER.getDisplayName()));
    }

    private boolean containsByePlayer(List<PlayerNames> players) {
        return players.stream()
                .anyMatch(player -> player.name().equalsIgnoreCase(MatchResultType.BYE.getDisplayName()));
    }

    private boolean isFirstPlayerBye(List<PlayerNames> players) {
        return players.getFirst().name().equalsIgnoreCase(MatchResultType.BYE.getDisplayName());
    }

    private void assignPlayersWithFirstBye(DisciplineMatch match, List<PlayerNames> players) {
        match.setPlayerOneName(players.getFirst().name());
        match.setPlayerTwoName(players.get(1).name());
        match.setPartnerTwoName(players.get(2).name());
    }

    private void assignPlayersWithLastBye(DisciplineMatch match, List<PlayerNames> players) {
        match.setPlayerOneName(players.getFirst().name());
        match.setPartnerOneName(players.get(1).name());
        match.setPlayerTwoName(players.get(2).name());
    }


    private void handleDoubleMixedMatchWithBye(DisciplineMatch match, List<PlayerNames> playerNames) {
        Log.debug("MatchPlayerParser :: Processing doubles match with bye player");

        if (!containsByePlayer(playerNames)) {
            Log.error("MatchPlayerParser :: Expected bye player not found. Invalid players count: " + playerNames.size());
            return;
        }

        if (isFirstPlayerBye(playerNames)) {
            assignPlayersWithFirstBye(match, playerNames);
        } else {
            assignPlayersWithLastBye(match, playerNames);
        }
    }

    private void handleSinglesMatch(DisciplineMatch match, List<PlayerNames> players) {
        Log.debug("MatchPlayerParser :: Processing singles match");
        match.setPlayerOneName(players.getFirst().name());
        match.setPlayerTwoName(players.getLast().name());
    }

    private void handleDoubleAndMixedMatch(DisciplineMatch match, List<PlayerNames> players) {
        Log.debug("MatchPlayerParser :: Processing doubles match");
        match.setPlayerOneName(players.get(0).name());
        match.setPartnerOneName(players.get(1).name());
        match.setPlayerTwoName(players.get(2).name());
        match.setPartnerTwoName(players.get(3).name());
    }

    private boolean isValidPlayerString(String str) {
        return !str.equalsIgnoreCase(WINNER_MARKER)
                && !str.equalsIgnoreCase(LOSER_MARKER)
                && !str.equalsIgnoreCase(RETIRED_MARKER_LOST)
                && !str.equalsIgnoreCase(RETIRED_MARKER)
                && !str.equalsIgnoreCase(H2H_MARKER)
                && !isNumeric(str);
    }

    @Override
    public DisciplineMatch parseMatch(HtmlElement matchGroupElement) {
        return null;
    }

    private record PlayerNames(
            String name
    ) {
    }
}
