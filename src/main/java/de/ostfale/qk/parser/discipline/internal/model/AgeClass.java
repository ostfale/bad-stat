package de.ostfale.qk.parser.discipline.internal.model;

public enum AgeClass {
    AG_U13, AG_U15, AG_17, AG_U19, AG_O19;

    public static AgeClass fromString(String ageClass) {
        if (ageClass == null || ageClass.isEmpty()) {
            throw new IllegalArgumentException("Age class cannot be null or empty.");
        }

        return switch (ageClass) {
            case "U13" -> AG_U13;
            case "U15" -> AG_U15;
            case "U17" -> AG_17;
            case "U19" -> AG_U19;
            case "O19" -> AG_O19;
            default -> throw new IllegalArgumentException("Unknown age class found: " + ageClass + ".");
        };
    }
}
