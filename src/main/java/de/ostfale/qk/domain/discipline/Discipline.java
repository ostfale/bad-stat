package de.ostfale.qk.domain.discipline;

import java.util.Set;

public enum Discipline {
    SINGLE, DOUBLE, MIXED;

    private static final Set<String> SINGLE_CODES = Set.of(
            "JE", "DE", "HE", "ME","HE-A",
            "Boys Singles", "Girls Singles",
            "Herreneinzel", "Dameneinzel",
            "Jungeneinzel", "Mädcheneinzel"
    );

    private static final Set<String> DOUBLE_CODES = Set.of(
            "JD", "DD", "HD", "MD",
            "Doubles", "Boys Doubles", "Girls Doubles",
            "Herrendoppel", "Damendoppel", "Mädchendoppel"
    );

    private static final Set<String> MIXED_CODES = Set.of(
            "MX", "DM", "HM",
            "Mixed", "Mixed Doubles"
    );

    /**
     * Looks up the Discipline enum based on the provided discipline code.
     *
     * @param discipline the discipline code to look up
     * @return the corresponding Discipline enum value
     * @throws IllegalArgumentException if the discipline is null, empty, or unknown
     */
    public static Discipline lookup(String discipline) {
        validateDiscipline(discipline);

        if (SINGLE_CODES.contains(discipline)) return SINGLE;
        if (DOUBLE_CODES.contains(discipline)) return DOUBLE;
        if (MIXED_CODES.contains(discipline)) return MIXED;

        throw new IllegalArgumentException("Unknown discipline found: " + discipline);
    }

    private static void validateDiscipline(String discipline) {
        if (discipline == null || discipline.isEmpty()) {
            throw new IllegalArgumentException("Discipline cannot be null or empty.");
        }
    }
}

