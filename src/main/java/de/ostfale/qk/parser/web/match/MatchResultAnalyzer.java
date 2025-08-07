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

    private final String[] matchResultElements;

    private final List<String> playerNames = new ArrayList<>(4);

    public MatchResultAnalyzer(String[] matchResultElements) {
        Log.debugf("MatchResultAnalyzer :: init -> %s", String.join(",", matchResultElements));
        this.matchResultElements = matchResultElements;
    }

    public String[] getMatchResultElements() {
        return matchResultElements;
    }

    public List<String> getMatchResultElementList() {
        return List.of(matchResultElements);
    }

    public String getMatchResultType() {
        var matchResultType = MatchResultType.REGULAR.getDisplayName();

        if (isWalkOverMatch()) {
            matchResultType = MatchResultType.WALKOVER.getDisplayName();
        }

        if (isRetiredMatch()) {
            matchResultType = MatchResultType.RETIRED.getDisplayName();
        }

        if (isByeMatch()) {
            matchResultType = MatchResultType.BYE.getDisplayName();
        }

        Log.debugf("MatchResultAnalyzer :: getMatchResultType -> %s", matchResultType);
        return matchResultType;
    }

    public boolean isByeMatch() {
        var result = getMatchResultElementList().stream()
                .anyMatch(item -> MatchResultType.getByeList().contains(item));
        Log.debugf("MatchResultAnalyzer :: isByeMatch -> %s", result);
        return result;
    }

    public boolean isWalkOverMatch() {
        var result = getMatchResultElementList().stream()
                .anyMatch(item -> MatchResultType.getWalkoverList().contains(item));
        Log.debugf("MatchResultAnalyzer :: isWalkOverMatch -> %s", result);
        return result;
    }

    public boolean isRetiredMatch() {
        var result = getMatchResultElementList().stream()
                .anyMatch(item -> MatchResultType.getRetiredList().contains(item));
        Log.debugf("MatchResultAnalyzer :: isRetiredMatch -> %s", result);
        return result;
    }

    public List<Integer> getMatchResultScores() {
        return getMatchResultElementList().stream().filter(this::isNumeric).map(Integer::parseInt).toList();
    }

    public List<String> getPlayerNames() {
        var result = getMatchResultElementList().stream()
                .filter(this::isValidName)
                .toList();

        Log.debugf("MatchResultAnalyzer :: getPlayerNames -> %s", result);
        return result;

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

    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        return str.trim().matches("\\d+");
    }

    public int getMatchMarkerPosition() throws HtmlParserException {

        var markerValue = getMatchResultElementList().stream()
                .filter(item -> MatchResultType.getAllMatchMarkerTypes().contains(item))
                .findFirst().orElse(null);

        if (markerValue != null) {
            var positionIndex = getMatchResultElementList().indexOf(markerValue);
            Log.debugf("MatchResultAnalyzer :: extractMarkerPosition -> %s -> %d", markerValue, positionIndex);
            return positionIndex;
        }

        throw new HtmlParserException(ParsedComponent.MATCH, "Could not find any W-L marker position!");
    }

    public String getMatchMarker() throws HtmlParserException {
        if (hasWinnerMarker()) {
            return WINNER_MARKER;
        }
        if (hasLoserMarker()) {
            return LOSER_MARKER;
        }
        throw new HtmlParserException(ParsedComponent.MATCH, "Match does not contain W-L-Marker!");
    }

    private boolean hasWinnerMarker() {
        return getMatchResultElementList().stream()
                .anyMatch(item -> MatchResultType.getWinnerMarkerList().contains(item));
    }

    private boolean hasLoserMarker() {
        return getMatchResultElementList().stream()
                .anyMatch(item -> MatchResultType.getLoserMarkerList().contains(item));
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

    public int getRetiredPosition() throws HtmlParserException {
        var result = getMatchResultElementList().stream()
                .filter(item -> MatchResultType.getRetiredList().contains(item))
                .findFirst().orElse(null);

        if (result != null) {
            var positionIndex = getMatchResultElementList().indexOf(result);
            Log.debugf("MatchResultAnalyzer :: extractRetiredPosition -> %s -> %d", result, positionIndex);
            return positionIndex;
        }
        throw new HtmlParserException(ParsedComponent.MATCH, "Could not find any Retired marker position!");

    }

    private String formatNameWithMarker(String playerName) throws HtmlParserException {
        return playerName + " (" + getMatchMarker() + ")";
    }

    private boolean isValidName(String name) {

        if (MatchResultType.getAllMatchResultTypes().contains(name)) {
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
            if (!containsOnlyCharacters(part)) {
                return false;
            }
        }
        return true;
    }

    private boolean containsOnlyCharacters(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        return input.chars().noneMatch(Character::isDigit);
    }
}
