package de.ostfale.qk.parser.discipline.model;

import de.ostfale.qk.domain.discipline.AgeClass;
import de.ostfale.qk.domain.discipline.Discipline;

public record DisciplineAgeModel(
        Discipline discipline,
        AgeClass ageClass
) {
}
