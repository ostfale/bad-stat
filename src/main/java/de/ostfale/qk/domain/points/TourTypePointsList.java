package de.ostfale.qk.domain.points;

import de.ostfale.qk.domain.discipline.AgeClass;

import java.util.List;

public record TourTypePointsList(
        AgeClass ageClass,
        PointsTourType pointsTourType,
        List<Points> pointsList
) {
}
