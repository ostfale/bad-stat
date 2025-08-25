package de.ostfale.qk.domain.tourcal.filter;

public enum ViewRange {
    ALL("Alle Turniere"),
    REMAINING("Verbleibende Turniere"),
    THIS_YEAR("Verbleibend dieses Jar"),
    NEXT_YEAR("Verbleibend nächstes Jahr");

    private final String displayName;

    ViewRange(String displayString) {
        this.displayName = displayString;
    }

    public String getDisplayName() {
        return displayName;
    }


    @Override
    public String toString() {
        return displayName;
    }
}
