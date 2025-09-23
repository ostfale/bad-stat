package de.ostfale.qk.domain.points;

import de.ostfale.qk.domain.discipline.AgeClass;

import java.util.ArrayList;
import java.util.List;

public record TourTypePointsList(
        AgeClass ageClass,
        PointsTourType pointsTourType,
        List<RankingPoint> rankingPointList
) {

    public TourTypePointsList(AgeClass ageClass, PointsTourType pointsTourType) {
        this(ageClass, pointsTourType, new ArrayList<>(33));
    }
}
