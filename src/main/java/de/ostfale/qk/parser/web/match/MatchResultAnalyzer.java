package de.ostfale.qk.parser.web.match;

import de.ostfale.qk.domain.match.MatchResultType;
import de.ostfale.qk.parser.HtmlParserException;
import de.ostfale.qk.parser.ParsedComponent;
import io.quarkus.logging.Log;

import java.util.List;

public class MatchResultAnalyzer {

    private static final String WINNER_MARKER = "W";
    private static final String LOSER_MARKER = "L";

    private static final String BYE_MARKER_DE = MatchResultType.BYE.getDisplayName();

    private final String[] matchResultElements;
    private final List<String> matchResultElementList;

    public MatchResultAnalyzer(String[] matchResultElements) {
        Log.debugf("MatchResultAnalyzer :: init -> %s", String.join(",", matchResultElements));
        this.matchResultElements = matchResultElements;
        this.matchResultElementList = List.of(matchResultElements);
    }

    public boolean isByeMatch() {
        Log.debug("MatchResultAnalyzer :: isByeMatch");
        return matchResultElementList.contains(BYE_MARKER_DE);
    }

    public String getMarker() throws HtmlParserException {
        Log.debug("MatchResultAnalyzer :: getMarker");
        if (matchResultElementList.contains(WINNER_MARKER)) {
            return WINNER_MARKER;
        } else if (matchResultElementList.contains(LOSER_MARKER)) {
            return LOSER_MARKER;
        }
        throw new HtmlParserException(ParsedComponent.MATCH, "Match does not contain W-L-Marker!");
    }
    
    public int getMarkerPosition(String marker) {
        Log.debugf("MatchResultAnalyzer :: getMarkerPosition -> %s", marker);
        return matchResultElementList.indexOf(marker);
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
        return matchResultElementList.stream()
                .filter(this::isValidName)
                .toList();
    }

    private boolean isValidName(String name) {
        if (name == null || name.isEmpty()) {
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
            if (!part.matches("[A-Z][-äüöa-zA-Z]*")) {
                return false;
            }
        }
        return true;
    }
}
