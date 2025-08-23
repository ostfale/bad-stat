package de.ostfale.qk.domain.tourcal;

import de.ostfale.qk.parser.ParsedComponent;
import de.ostfale.qk.parser.file.FileParserException;

import java.util.Arrays;

public enum TournamentType {

    Ranking("Rangliste"),
    Championship("Meisterschaft");

    private final String displayString;

    TournamentType(String displayString) {
        this.displayString = displayString;
    }

    public static TournamentType lookup(String aValue) throws FileParserException {
        return Arrays.stream(values())
                .filter(kind -> kind.displayString.equalsIgnoreCase(aValue))
                .findFirst()
                .orElseThrow(() -> new FileParserException(ParsedComponent.TOURNAMENT_CALENDAR, "Unknown TournamentType: " + aValue));
    }
}
