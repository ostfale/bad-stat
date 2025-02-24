package de.ostfale.qk.ui.statistics.model;

public enum SearchableYears {
    YEAR_2025("2025"),
    YEAR_2024("2024"),
    YEAR_2023("2023"),
    YEAR_2022("2022");

    private final String displayValue;

    SearchableYears(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return this.displayValue;
    }

    @Override
    public String toString() {
        return this.displayValue;
    }
}
