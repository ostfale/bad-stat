package de.ostfale.qk.parser.web.match;

import de.ostfale.qk.domain.match.MatchResultType;
import de.ostfale.qk.parser.HtmlParserException;
import de.ostfale.qk.parser.ParsedComponent;
import io.quarkus.logging.Log;

import java.util.ArrayList;
import java.util.List;

public class MatchResultAnalyzer {

    private static final String WINNER_MARKER = "W";
    private static final String LOSER_MARKER = "L";
    private static final String NO_GAME = "Kein Spiel";
    private static final String WALKOVER_L = "Walkover L";

    private static final String BYE_MARKER_DE = MatchResultType.BYE.getDisplayName();
    private static final String WALKOVER_MARKER = MatchResultType.WALKOVER.getDisplayName();

    private final String[] matchResultElements;
    private final List<String> matchResultElementList;
    private final List<String> playerNames = new ArrayList<>(4);
    private final String markerValue;
    private final int markerPosition;

    public MatchResultAnalyzer(String[] matchResultElements) throws HtmlParserException {
        Log.debugf("MatchResultAnalyzer :: init -> %s", String.join(",", matchResultElements));
        this.matchResultElements = matchResultElements;
        this.matchResultElementList = List.of(matchResultElements);
        this.markerValue = extractMarkerValue(matchResultElementList);
        this.markerPosition = extractMarkerPosition(matchResultElementList);
    }

    private int extractMarkerPosition(List<String> matchResultElementList) {
        Log.debug("MatchResultAnalyzer :: extractMarkerPosition");
        if (matchResultElementList.contains(WALKOVER_L)) {
            return matchResultElementList.indexOf(WALKOVER_L);
        }
        return matchResultElementList.indexOf(markerValue);
    }

    private String extractMarkerValue(List<String> matchResultElementList) throws HtmlParserException {
        Log.debug("MatchResultAnalyzer ::");
        if (matchResultElementList.contains(WINNER_MARKER)) {
            return WINNER_MARKER;
        } else if (this.matchResultElementList.contains(LOSER_MARKER)
                || this.matchResultElementList.contains(WALKOVER_L)) {
            return LOSER_MARKER;
        }
        throw new HtmlParserException(ParsedComponent.MATCH, "Match does not contain W-L-Marker!");
    }

    public boolean isByeMatch() {
        Log.debug("MatchResultAnalyzer :: isByeMatch");
        return matchResultElementList.contains(BYE_MARKER_DE);
    }

    public boolean isWalkOverMatch() {
        Log.debug("MatchResultAnalyzer :: isWalkOverMatch");
        return matchResultElementList.contains(WALKOVER_MARKER)
                || matchResultElementList.contains(WALKOVER_L);
    }

    public String getMarker() {
        return markerValue;
    }

    public int getMarkerPosition() {
        return markerPosition;
    }

    public List<Integer> getMatchResultScores() {
        return matchResultElementList.stream().filter(this::isNumeric).map(Integer::parseInt).toList();
    }

    public String[] getMatchResultElements() {
        return matchResultElements;
    }

    public List<String> getMatchResultElementList() {
        return matchResultElementList;
    }

    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        return str.trim().matches("\\d+");
    }

    public List<String> getPlayerNames() {
        Log.debug("MatchResultAnalyzer :: getPlayerNames");
        if (playerNames.isEmpty()) {
            playerNames.addAll(matchResultElementList.stream()
                    .filter(this::isValidName)
                    .toList());
        }
        return playerNames;
    }

    public String getFirstPlayerName(boolean isMarked) throws HtmlParserException {
        return getPlayerName(PlayerPosition.FIRST, isMarked);
    }

    public String getSecondPlayerName(boolean isMarked) throws HtmlParserException {
        return getPlayerName(PlayerPosition.SECOND, isMarked);
    }

    public String getThirdPlayerName(boolean isMarked) throws HtmlParserException {
        return getPlayerName(PlayerPosition.THIRD, isMarked);
    }

    public String getFourthPlayerName(boolean isMarked) throws HtmlParserException {
        return getPlayerName(PlayerPosition.FOURTH, isMarked);
    }

    private String getPlayerName(PlayerPosition position, boolean isMarked) throws HtmlParserException {
        Log.debug("MatchResultAnalyzer :: get" + position.name + "PlayerName");
        var playerName = position.getPlayerName(getPlayerNames());
        return isMarked ? formatNameWithMarker(playerName) : playerName;
    }

    private enum PlayerPosition {
        FIRST("First"),
        SECOND("Second"),
        THIRD("Third"),
        FOURTH("Fourth");

        private final String name;

        PlayerPosition(String name) {
            this.name = name;
        }

        public String getPlayerName(List<String> names) {
            return switch (this) {
                case FIRST -> names.get(0);
                case SECOND -> names.get(1);
                case THIRD -> names.get(2);
                case FOURTH -> names.get(3);
            };
        }
    }

    private String formatNameWithMarker(String playerName) throws HtmlParserException {
        return playerName + " (" + getMarker() + ")";
    }

    private boolean isValidName(String name) {
        if (name == null
                || name.isEmpty()
                || name.equalsIgnoreCase(NO_GAME)) {
            return false;
        }

        // Split by one or more whitespace characters
        String[] nameParts = name.trim().split("\\s+");

        // Need at least two parts (first name and last name)
        if (nameParts.length < 2) {
            return false;
        }

        // Check if each part starts with uppercase and contains only letters
        for (String part : nameParts) {
            if (!part.matches("[A-Zvon][-øáäüöa-zA-Z]*")) {
                return false;
            }
        }
        return true;
    }
}
