package de.ostfale.qk.domain.tourcal;

import de.ostfale.qk.parser.ParsedComponent;
import de.ostfale.qk.parser.file.FileParserException;

import java.util.Arrays;

public enum TourCategory {
    A("A-Level", "A"),
    B("B-Level", "B"),
    BEC("BEC Event", "BEC"),
    BEC15("BEC-U15", "BEC"),
    BEC17("BEC-U17", "BEC"),
    BWF("BWF Event", "BWF"),
    C("C-Level", "C"),
    C1("C1-Level", "C"),
    C2("C2-Level", "C"),
    C3("C3-Level", "C"),
    D("D-Level", "D"),
    D1("D1-Level", "D"),
    D2("D2-Level", "D"),
    D3("D3-Level", "D"),
    E("E-Level", "E");

    private final String displayName;
    private final String baseCategory;

    TourCategory(String displayName, String baseCategory) {
        this.displayName = displayName;
        this.baseCategory = baseCategory;
    }

    public static TourCategory lookup(String displayName) throws FileParserException {
        return Arrays.stream(TourCategory.values())
                .filter(category -> category.displayName.equalsIgnoreCase(displayName))
                .findFirst()
                .orElseThrow(() -> new FileParserException(ParsedComponent.TOURNAMENT_CALENDAR, "Unknown TournamentCategory: " + displayName));
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBaseCategory() {
        return baseCategory;
    }
}
