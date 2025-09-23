package de.ostfale.qk.domain.discipline;

import io.quarkus.logging.Log;

public enum AgeClass {
    U9, U11, U13, U15, U17, U19, U22, O19, O35, UOX;

    public static AgeClass[] getFilterValues() {
        return new AgeClass[]{U9, U11, U13, U15, U17, U19, U22, O19, O35};
    }

    public static AgeClass fromString(String ageClass) {
        if (ageClass == null || ageClass.isEmpty()) {
            throw new IllegalArgumentException("Age class cannot be null or empty.");
        }

        String trimmedAgeClass = ageClass.trim();

        if (!trimmedAgeClass.startsWith("U")
                && !trimmedAgeClass.startsWith("O")
                && !trimmedAgeClass.startsWith("u")
                && !trimmedAgeClass.startsWith("o")) {
            Log.warnf("Age class %s does not start with U or O. Using UOX as default.", ageClass);
            return UOX;
        }

        if (ageClass.length() > 3) {
            trimmedAgeClass = trimmedAgeClass.substring(0, 3);
        }

        return switch (trimmedAgeClass) {
            case "U9", "u09" -> U9;
            case "U11", "u11" -> U11;
            case "U13", "u13" -> U13;
            case "U15", "u15" -> U15;
            case "U17", "u17" -> U17;
            case "U19", "u19" -> U19;
            case "U22", "u22" -> U22;
            case "O19", "o19" -> O19;
            case "O35", "o35" -> O35;
            case "UOX" -> UOX;
            default -> throw new IllegalArgumentException("Unknown age class found: " + ageClass + ".");
        };
    }
}
