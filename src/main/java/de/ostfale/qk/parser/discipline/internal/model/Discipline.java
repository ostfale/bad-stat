package de.ostfale.qk.parser.discipline.internal.model;

public enum Discipline {
    SINGLE, DOUBLE, MIXED;

    public static Discipline fromString(String discipline) {
        if (discipline == null || discipline.isEmpty()) {
            throw new IllegalArgumentException("Discipline cannot be null or empty.");
        }

        return switch (discipline) {
            case "JE","DE" -> SINGLE;
            case "JD","DD" -> DOUBLE;
            case "MX" -> MIXED;
            default -> throw new IllegalArgumentException("Unknown discipline found: " + discipline + ".");
        };
    }
}
