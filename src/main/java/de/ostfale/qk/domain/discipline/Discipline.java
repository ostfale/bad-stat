package de.ostfale.qk.domain.discipline;

public enum Discipline {
    SINGLE, DOUBLE, MIXED;

    public static Discipline lookup(String discipline) {
        if (discipline == null || discipline.isEmpty()) {
            throw new IllegalArgumentException("Discipline cannot be null or empty.");
        }

        return switch (discipline) {
            case "JE", "DE", "HE", "ME", "Boys Singles", "Girls Singles" -> SINGLE;
            case "JD", "DD", "HD", "MD", "Doubles", "Boys Doubles", "Girls Doubles" -> DOUBLE;
            case "MX", "DM", "HM", "Mixed", "Mixed Doubles" -> MIXED;
            default -> throw new IllegalArgumentException("Unknown discipline found: " + discipline);
        };
    }
}
