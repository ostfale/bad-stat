package de.ostfale.qk.parser.web.match;

import de.ostfale.qk.domain.match.DisciplineMatch;
import de.ostfale.qk.domain.match.MatchResultType;
import io.quarkus.logging.Log;

import java.util.Arrays;

public class MatchByeParserService {

    private static final String BYE_MARKER = MatchResultType.BYE.getDisplayName();

    public boolean isByeMatch(String[] matchStringElements) {
        Log.debug("MatchByeParserService :: isBye (Rast)");
        var germanBye = MatchResultType.BYE.getDisplayName();
        return Arrays.stream(matchStringElements)
                .anyMatch(element -> element.equalsIgnoreCase(BYE_MARKER));
    }

    public void processSinglePlayerBye(DisciplineMatch disciplineMatch, String[] matchStringElements) {
        Log.debug("MatchByeParserService :: processSinglePlayerBye");
    }
}
