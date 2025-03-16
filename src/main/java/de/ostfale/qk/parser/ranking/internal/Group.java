package de.ostfale.qk.parser.ranking.internal;

public enum Group {
    NORTH("DBV-Gruppe Nord"),
    SOUTH_EAST("DBV-Gruppe Südost"),
    WEST("DBV-Gruppe West"),
    CENTER("DBV-Gruppe Mitte"),
    NO_GROUP("No group");

    private final String displayName;

    Group(String displayName) {
        this.displayName = displayName;
    }

    public static Group lookup(String displayName) {
        return switch (displayName) {
            case "DBV-Gruppe Nord" -> NORTH;
            case "DBV-Gruppe Südost", "DBV-Gruppe SüdOst" -> SOUTH_EAST;
            case "DBV-Gruppe West" -> WEST;
            case "DBV-Gruppe Mitte" -> CENTER;
            default -> NO_GROUP;
        };
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
