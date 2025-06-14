package de.ostfale.qk.parser.discipline.model;

import de.ostfale.qk.domain.discipline.AgeClass;
import de.ostfale.qk.domain.discipline.DisciplineType;

public record DisciplineAgeModel(
        DisciplineType disciplineType,
        AgeClass ageClass
) {
}
