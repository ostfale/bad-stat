package de.ostfale.qk.domain.match;

import java.util.List;
import java.util.stream.Stream;

public enum MatchResultType {
    REGULAR("Default"), BYE("Rast"), WALKOVER("Walkover"), RETIRED("Retired");

    private final String displayName;

    private static final List<String> RETIRED_LIST = List.of("Retired", "Retired L", "Retired.", "Retired. L");
    private static final List<String> WALKOVER_LIST = List.of("Walkover", "Walkover L");
    private static final List<String> BYE_LIST = List.of("Rast","Kein Spiel");
    private static final List<String> LOSER_MARKER_LIST = List.of("L", "Retired L", "Retired. L", "Walkover L");
    private static final List<String> WINNER_MARKER_LIST = List.of("W");

    MatchResultType(String displayName) {
        this.displayName = displayName;
    }

    public static MatchResultType lookup(String displayName) {
        return switch (displayName) {
            case "Default" -> REGULAR;
            case "Rast" -> BYE;
            case "Walkover", "Walkover L" -> WALKOVER;
            case "Retired", "Retired L", "Retired.", "Retired. L" -> RETIRED;
            default -> REGULAR;
        };
    }

    public String getDisplayName() {
        return displayName;
    }

    public static List<String> getRetiredList() {
        return RETIRED_LIST;
    }

    public static List<String> getWalkoverList() {
        return WALKOVER_LIST;
    }

    public static List<String> getByeList() {
        return BYE_LIST;
    }

    public static List<String> getLoserMarkerList() {
        return LOSER_MARKER_LIST;
    }

    public static List<String> getWinnerMarkerList() {
        return WINNER_MARKER_LIST;
    }

    public static List<String> getAllMatchResultTypes() {
        return Stream.of(RETIRED_LIST, WALKOVER_LIST, BYE_LIST)
                .flatMap(List::stream)
                .toList();
    }

    public static List<String> getAllMatchMarkerTypes() {
        return Stream.of(LOSER_MARKER_LIST,WINNER_MARKER_LIST)
                .flatMap(List::stream)
                .toList();
    }


}
