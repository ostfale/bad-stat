package de.ostfale.qk.ui.tourcalendar;

import de.ostfale.qk.domain.converter.PlannedTournamentModelToUIConverter;
import de.ostfale.qk.ui.tourcalendar.filter.TournamentFilter;
import de.ostfale.qk.ui.tourcalendar.model.TourCalUIModel;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class TourCalService {

    @Inject
    PlannedTournamentsHandler handler;

    final PlannedTournamentModelToUIConverter converter = new PlannedTournamentModelToUIConverter();

    public List<TourCalUIModel> applyAllFilter(List<TournamentFilter> filters) {
        var allPlannedTournaments = handler.getAllPlannedTournamentsList();

        if (filters != null && !filters.isEmpty()) {
            for (TournamentFilter filter : filters) {
                allPlannedTournaments = filter.filterTournaments(allPlannedTournaments);
            }
        }

        var migratedTournaments = allPlannedTournaments.stream()
                .map(converter::convertTo)
                .toList();
        Log.debugf("TourCalService:: applyAllFilter : remaining %d ", migratedTournaments.size());
        return migratedTournaments;
    }
}
