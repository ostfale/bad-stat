package de.ostfale.qk.domain.tourcal;

import java.util.Arrays;

public enum TournamentKind {

    Ranking("Rangliste"),
    Championship("Meisterschaft");

    private final String displayString;

    TournamentKind(String displayString) {
        this.displayString = displayString;
    }

    public static TournamentKind lookup(String aValue) {
        return Arrays.stream(values())
                .filter(kind -> kind.displayString.equalsIgnoreCase(aValue))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown TournamentKind: " + aValue));
    }

}
