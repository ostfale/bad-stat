package de.ostfale.qk.domain.points;

import de.ostfale.qk.domain.discipline.AgeClass;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class TourPointsViewModel {

    private final List<TourTypePointsList> tourTypePointsList = new ArrayList<>();

    public List<TourTypePointsList> getTourTypePointsList() {
        return tourTypePointsList;
    }

    public void addTourTypePointsList(List<TourTypePointsList> tourTypePointsList) {
        Objects.requireNonNull(tourTypePointsList, "Ranking points list must not be null");
        this.tourTypePointsList.addAll(tourTypePointsList);
    }

    public List<TourTypePointsList> getAllTournamentTypesForAgeClass(AgeClass ageClass) {
        Log.debugf("TourPointsViewModel :: Get all tournament types for age class %s", ageClass);
        return tourTypePointsList
                .stream()
                .filter(tourTypePointsList1 -> tourTypePointsList1.ageClass().equals(ageClass))
                .toList();
    }

    public List<String> getHeaderNamesForAgeClass(AgeClass ageClass) {
        Log.debugf("TourPointsViewModel :: Get header names for age class %s", ageClass);
        return tourTypePointsList.stream()
                .filter(tpList -> tpList.ageClass().equals(ageClass))
                .map(tpPlist -> tpPlist.pointsTourType().getDisplayName())
                .toList();
    }

    public List<List<RankingPoint>> getPointsForAgeClass(AgeClass ageClass) {
        Log.debugf("TourPointsViewModel :: Get points for age class %s", ageClass);
        var tourTypesForAgeClass = tourTypePointsList.stream()
                .filter(tpList -> tpList.ageClass().equals(ageClass)).toList();

        if (tourTypesForAgeClass.isEmpty()) {
            return List.of();
        }

        var numberOfRankingPositions = tourTypesForAgeClass.getFirst().rankingPointList().size();
        return transposeRankingPointsMatrix(tourTypesForAgeClass, numberOfRankingPositions);
    }

    private List<List<RankingPoint>> transposeRankingPointsMatrix(List<TourTypePointsList> tourTypesForAgeClass, int numberOfRankingPositions) {
        List<List<RankingPoint>> transposedMatrix = new ArrayList<>();
        for (int rankingPosition = 0; rankingPosition < numberOfRankingPositions; rankingPosition++) {
            List<RankingPoint> pointsForPosition = new ArrayList<>();
            transposedMatrix.add(pointsForPosition);
            pointsForPosition.add(new RankingPoint(rankingPosition + 1));
            for (TourTypePointsList tourTypePoints : tourTypesForAgeClass) {
                pointsForPosition.add(tourTypePoints.rankingPointList().get(rankingPosition));
            }
        }
        return transposedMatrix;
    }
}
