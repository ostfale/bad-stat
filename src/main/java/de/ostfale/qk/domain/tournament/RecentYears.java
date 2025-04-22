package de.ostfale.qk.domain.tournament;

import java.time.Year;

public enum RecentYears {
    CURRENT_YEAR(Year.now().getValue()),
    YEAR_MINUS_1(Year.now().getValue() - 1),
    YEAR_MINUS_2(Year.now().getValue() - 2),
    YEAR_MINUS_3(Year.now().getValue() - 3);

    private final int value;
    private final String displayValue;

    RecentYears(int value) {
        this.value = value;
        this.displayValue = String.valueOf(value);
    }

    public static RecentYears lookup(int value) {
        for (RecentYears year : RecentYears.values()) {
            if (year.getValue() == value) {
                return year;
            }
        }
        throw new IllegalArgumentException("Year " + value + " is not a recent year.");
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public int getValue() {
        return value;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
