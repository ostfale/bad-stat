package de.ostfale.qk.domain.tourcal.filter;

public enum ViewRange {
    ALL("Alle Turniere"),
    REMAINING("Verbleibende Turniere"),
    NEXT_YEAR("Turniere nächstes Jahr");

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
