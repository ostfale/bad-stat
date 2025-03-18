package de.ostfale.qk.ui.app;

import java.time.Year;

public enum RecentYears {
    CURRENT_YEAR(Year.now().getValue()),
    YEAR_MINUS_1(Year.now().getValue() - 1),
    YEAR_MINUS_2(Year.now().getValue() - 2),
    YEAR_MINUS_3(Year.now().getValue() - 3);

    private final int value;

    RecentYears(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public int getValue() {
        return value;
    }
}
