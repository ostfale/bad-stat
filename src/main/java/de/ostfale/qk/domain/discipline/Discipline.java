package de.ostfale.qk.domain.discipline;

public enum Discipline {
    SINGLE, DOUBLE, MIXED;

    public static Discipline lookup(String discipline) {
        if (discipline == null || discipline.isEmpty()) {
            throw new IllegalArgumentException("Discipline cannot be null or empty.");
        }

        return switch (discipline) {
            case "JE", "DE", "HE", "ME" -> SINGLE;
            case "JD", "DD", "HD", "MD" -> DOUBLE;
            case "MX", "DM", "HM" -> MIXED;
            default -> throw new IllegalArgumentException("Unknown discipline found: " + discipline);
        };
    }
}
