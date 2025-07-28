package de.ostfale.qk.domain.discipline;

import io.quarkus.logging.Log;

import java.util.Set;

public enum DisciplineType {
    SINGLE("Einzel"), DOUBLE("Doppel"), MIXED("Mixed"), UNKNOWN("Unbekannt");

    private static final Set<String> SINGLE_CODES = Set.of(
            "JE", "DE", "HE", "ME", "HE-A", "BS",
            "Boys Singles", "Girls Singles", "Men's Singles", "Women's Singles",
            "Herreneinzel", "Dameneinzel",
            "Jungeneinzel", "Mädcheneinzel"
    );

    private static final Set<String> DOUBLE_CODES = Set.of(
            "JD", "DD", "HD", "MD", "BD",
            "Doubles", "Boys Doubles", "Girls Doubles", "Men's Doubles", "Women's Doubles",
            "Herrendoppel", "Damendoppel", "Mädchendoppel", "Jungendoppel"
    );

    private static final Set<String> MIXED_CODES = Set.of(
            "MX", "DM", "HM",
            "Mixed", "Mixed Doubles"
    );

    private final String displayString;

    DisciplineType(String displayString) {
        this.displayString = displayString;
    }

    /**
     * Looks up the Discipline enum based on the provided discipline code.
     *
     * @param discipline the discipline code to look up
     * @return the corresponding Discipline enum value
     * @throws IllegalArgumentException if the discipline is null, empty, or unknown
     */
    public static DisciplineType lookup(String discipline) {
        validateDiscipline(discipline);

        if (SINGLE_CODES.contains(discipline)) return SINGLE;
        if (DOUBLE_CODES.contains(discipline)) return DOUBLE;
        if (MIXED_CODES.contains(discipline)) return MIXED;

        Log.errorf("Unknown discipline found: %s", discipline);
        return UNKNOWN;
    }

    private static void validateDiscipline(String discipline) {
        if (discipline == null || discipline.isEmpty()) {
            throw new IllegalArgumentException("Discipline cannot be null or empty.");
        }
    }

    public String getDisplayString() {
        return displayString;
    }
}

