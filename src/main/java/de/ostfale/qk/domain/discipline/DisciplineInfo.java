package de.ostfale.qk.domain.discipline;

public record DisciplineInfo(
        String originalString,
        AgeClass ageClass,
        DisciplineType disciplineType
) {
}
