package de.ostfale.qk.domain.player;

public enum GenderType {
    MALE("M"),
    FEMALE("F");

    private final String displayName;

    GenderType(String aDisplayName) {
        this.displayName = aDisplayName;
    }

    public static GenderType lookup(String displayName) {
        return switch (displayName.toUpperCase()) {
            case "M" -> MALE;
            case "F" -> FEMALE;
            default -> throw new IllegalArgumentException("GenderType: " + displayName);

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
