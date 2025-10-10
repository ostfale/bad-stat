package de.ostfale.qk.domain.pointstable;

import java.util.ArrayList;
import java.util.List;

public record TourTypeTablePoints(
        PointsDisplayTypes pointsDisplayTypes,
        List<String> rankingPoints
) {
    public TourTypeTablePoints(PointsDisplayTypes pointsDisplayTypes) {
        this(pointsDisplayTypes, new ArrayList<>(33));
    }
}
