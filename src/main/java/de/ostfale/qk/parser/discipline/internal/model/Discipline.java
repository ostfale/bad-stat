package de.ostfale.qk.parser.discipline.internal.model;

public enum Discipline {
    SINGLE, DOUBLE, MIXED;

    public static Discipline lookup(String discipline) {
        if (discipline == null || discipline.isEmpty()) {
            throw new IllegalArgumentException("Discipline cannot be null or empty.");
        }

        return switch (discipline) {
            case "JE", "DE", "HE" -> SINGLE;
            case "JD", "DD", "HD" -> DOUBLE;
            case "MX", "DM", "HM" -> MIXED;
            default -> throw new IllegalArgumentException("Unknown discipline found: " + discipline);
        };
    }
}
