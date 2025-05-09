package de.ostfale.qk.domain.discipline;

public enum AgeClass {
    U13, U15, U17, U19, O19;

    public static AgeClass fromString(String ageClass) {
        if (ageClass == null || ageClass.isEmpty()) {
            throw new IllegalArgumentException("Age class cannot be null or empty.");
        }

        return switch (ageClass) {
            case "U13" -> U13;
            case "U15" -> U15;
            case "U17" -> U17;
            case "U19" -> U19;
            case "O19" -> O19;
            default -> throw new IllegalArgumentException("Unknown age class found: " + ageClass + ".");
        };
    }
}
