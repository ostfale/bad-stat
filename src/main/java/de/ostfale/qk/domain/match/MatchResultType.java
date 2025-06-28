package de.ostfale.qk.domain.match;

public enum MatchResultType {
    REGULAR ("Default"), BYE ("Rast"), WALKOVER ("Walkover"), RETIRED ("Retired");

    private final String displayName;

    MatchResultType(String displayName) {
        this.displayName = displayName;
    }

    public static MatchResultType lookup(String displayName) {
        return switch (displayName) {
            case "Default" -> REGULAR;
            case "Rast" -> BYE;
            case "Walkover" -> WALKOVER;
            case "Retired" -> RETIRED;
            default -> REGULAR;
        };
    }

    public String getDisplayName() {
        return displayName;
    }
}
