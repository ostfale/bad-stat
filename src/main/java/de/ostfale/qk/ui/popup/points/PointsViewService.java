package de.ostfale.qk.ui.popup.points;

import de.ostfale.qk.domain.discipline.AgeClass;
import de.ostfale.qk.domain.points.RankingPoint;
import de.ostfale.qk.domain.points.TourPointsViewModel;
import de.ostfale.qk.domain.points.TourTypePointsList;
import de.ostfale.qk.parser.file.csv.RankingPointsParser;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class PointsViewService {

    @Inject
    RankingPointsParser rankingPointsParser;

    private TourPointsViewModel tourPointsViewModel;

    public List<TourTypePointsList> getTourPointsViewModelsForAgeClass(AgeClass ageClass) {
        Log.debugf("PointsViewService :: Get all tournament types for age class %s", ageClass);
        if (tourPointsViewModel == null) {
            Log.debug("PointsViewService :: initial read of tournament points data");
            try {
                tourPointsViewModel = readTournamentPointsData();
            } catch (URISyntaxException e) {
                Log.errorf("PointsViewService :: Could not read tournament points data -> %s", e.getMessage());
                return Collections.emptyList();
            }
        }
        return tourPointsViewModel.getTourTypePointsList()
                .stream()
                .filter(tourTypePointsList1 -> tourTypePointsList1.ageClass().equals(ageClass))
                .toList();
    }

    public List<String> getHeaderNames(AgeClass ageClass)  {
        Log.debugf("PointsViewService :: get header names for age class %s", ageClass);
        if (tourPointsViewModel == null) {
            Log.debug("PointsViewService :: initial read of header names data");
            try {
                tourPointsViewModel=readTournamentPointsData();
            } catch (URISyntaxException e) {
                Log.errorf("PointsViewService :: Could not read header names -> %s", e.getMessage());
                return Collections.emptyList();
            }
        }
        return tourPointsViewModel.getHeaderNamesForAgeClass(ageClass);
    }

    public List<List<RankingPoint>> getPointValues(AgeClass ageClass) {
        Log.debugf("PointsViewService :: get values for age class %s", ageClass);
        return tourPointsViewModel.getPointsForAgeClass(ageClass);
    }

    private TourPointsViewModel readTournamentPointsData() throws URISyntaxException {
        Log.info("PointsViewService ::readTournamentPointsData");
        return rankingPointsParser.getTourPointsViewModel();
    }
}
