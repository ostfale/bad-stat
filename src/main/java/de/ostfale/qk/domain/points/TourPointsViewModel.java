package de.ostfale.qk.domain.points;

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
        Objects.requireNonNull(tourTypePointsList,"Ranking points list must not be null");
        this.tourTypePointsList.addAll(tourTypePointsList);
    }
}
