package de.ostfale.qk.ui.tourcalendar.model;

import de.ostfale.qk.domain.discipline.AgeClass;

public record TourCalAgeDiscipline(
        AgeClass ageClass,
        boolean isSingle,
        boolean isDouble,
        boolean isMixed
) {

    public boolean anyDisciplineForThisAgeClass() {
        return isSingle || isDouble || isMixed;
    }
}
