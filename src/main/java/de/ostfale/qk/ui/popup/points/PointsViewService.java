package de.ostfale.qk.ui.popup.points;

import de.ostfale.qk.domain.discipline.AgeClass;
import de.ostfale.qk.parser.pointstable.CSVPointsTableService;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class PointsViewService {

    @Inject
    CSVPointsTableService csvPointsTableService;

    public List<String> getHeaderNames(AgeClass ageClass) {
        Log.debugf("PointsViewService :: get header names for age class %s", ageClass);
        return csvPointsTableService.getAgeClassTablePoints().getTournamentTypesForAgeClass(ageClass);
    }

    public List<List<String>> getPointValues(AgeClass ageClass) {
        Log.debugf("PointsViewService :: get values for age class %s", ageClass);
        return csvPointsTableService.getAgeClassTablePoints().transposePointListsForAgeClass(ageClass);
    }
}
