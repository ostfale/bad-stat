package de.ostfale.qk.domain.pointstable;

import de.ostfale.qk.domain.discipline.AgeClass;
import io.quarkus.logging.Log;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record AgeClassTablePoints(
        Map<AgeClass, List<TourTypeTablePoints>> ageClassTablePointsMap
) {
    public List<String> getTournamentTypesForAgeClass(AgeClass ageClass) {
        Log.debugf("AgeClassTablePoints :: Get tournament types for age class %s", ageClass);
        return ageClassTablePointsMap.get(ageClass)
                .stream()
                .map(tourTypeTablePoints -> tourTypeTablePoints.pointsDisplayTypes().getDisplayName())
                .toList();
    }

    public List<List<String>> transposePointListsForAgeClass(AgeClass ageClass) {
        Log.debugf("AgeClassTablePoints :: Transpose points lists for age class %s", ageClass);

        List<TourTypeTablePoints> tournamentTypePointsList = ageClassTablePointsMap.get(ageClass);
        if (isEmptyOrNull(tournamentTypePointsList)) {
            return List.of();
        }
        return createTransposedMatrix(tournamentTypePointsList);
    }

    private boolean isEmptyOrNull(List<TourTypeTablePoints> list) {
        return list == null || list.isEmpty();
    }

    private List<List<String>> createTransposedMatrix(List<TourTypeTablePoints> tournamentTypePointsList) {
        Log.trace("AgeClassTablePoints :: Create transposed matrix");
        int minPointsListLength = findMinimumPointsListLength(tournamentTypePointsList);

        return IntStream.range(0, minPointsListLength)
                .mapToObj(rowIndex -> extractPointsAtPosition(tournamentTypePointsList, rowIndex))
                .collect(Collectors.toList());
    }

    private List<String> extractPointsAtPosition(List<TourTypeTablePoints> tournamentTypePointsList, int position) {
        return tournamentTypePointsList.stream()
                .map(tourTypePoints -> tourTypePoints.rankingPoints().get(position))
                .collect(Collectors.toList());
    }

    private int findMinimumPointsListLength(List<TourTypeTablePoints> tournamentTypePointsList) {
        Log.trace("AgeClassTablePoints :: Finding minimum length of points list");
        return tournamentTypePointsList.stream()
                .mapToInt(tourTypePoints -> tourTypePoints.rankingPoints().size())
                .min()
                .orElse(0);
    }
}
