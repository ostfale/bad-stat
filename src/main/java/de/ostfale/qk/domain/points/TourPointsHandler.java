package de.ostfale.qk.domain.points;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TourPointsHandler {

    private final List<TourTypePointsList> tourTypePointsList = new ArrayList<>();
}
