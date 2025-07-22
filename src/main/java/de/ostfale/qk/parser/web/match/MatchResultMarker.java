package de.ostfale.qk.parser.web.match;

import de.ostfale.qk.parser.HtmlParserException;
import de.ostfale.qk.parser.ParsedComponent;
import io.quarkus.logging.Log;
import org.apache.commons.lang3.ArrayUtils;

public class MatchResultMarker {

    private static final String WINNER_MARKER = "W";
    private static final String LOSER_MARKER = "L";

    private String markerValue;
    private int markerIndex;

    public MatchResultMarker extractMarkerAndIndex(String[] matchStringElements) throws HtmlParserException {
        Log.debug("MatchResultMarker :: MatchResultMarker");
        for (var element : matchStringElements) {
            if (isWinnerOrLoserMarker(element)) {
                this.markerValue = element;
                this.markerIndex = ArrayUtils.indexOf(matchStringElements, element);
                return this;
            }
        }
        throw new HtmlParserException(ParsedComponent.MATCH, "Match does not contain W-L-Marker!");
    }

    public String toDisplayString(String playerName) {
        return String.format("%s (%s)", playerName, markerValue);
    }

    private boolean isWinnerOrLoserMarker(String element) {
        return element.equalsIgnoreCase(WINNER_MARKER) ||
                element.equalsIgnoreCase(LOSER_MARKER);
    }

    public String getMarkerValue() {
        return markerValue;
    }

    public void setMarkerValue(String markerValue) {
        this.markerValue = markerValue;
    }

    public int getMarkerIndex() {
        return markerIndex;
    }

    public void setMarkerIndex(int markerIndex) {
        this.markerIndex = markerIndex;
    }
}
