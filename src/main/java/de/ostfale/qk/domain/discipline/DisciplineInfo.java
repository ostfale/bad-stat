package de.ostfale.qk.domain.discipline;

public record DisciplineInfo(
        String originalString,
        AgeClass ageClass,
        DisciplineType disciplineType,
        boolean isGroupSubHeader
) {
    public DisciplineInfo(String originalString, AgeClass ageClass, DisciplineType disciplineType) {
        this(originalString, ageClass, disciplineType, false);
    }

    public DisciplineInfo(String originalString) {
        this(originalString, AgeClass.UOX, DisciplineType.UNKNOWN, true);
    }
}
