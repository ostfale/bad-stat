package de.ostfale.qk.ui.tourcalendar;

import de.ostfale.qk.app.TimeHandlerFacade;
import de.ostfale.qk.domain.converter.PlannedTournamentModelToUIConverter;
import de.ostfale.qk.domain.tourcal.PlannedTournament;
import de.ostfale.qk.domain.tourcal.filter.ViewRange;
import de.ostfale.qk.ui.tourcalendar.model.TourCalUIModel;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class TourCalService implements TimeHandlerFacade{

    @Inject
    PlannedTournamentsHandler handler;


    public List<TourCalUIModel> updateRangeView(ViewRange selectedItem) {
        List<PlannedTournament> filteredTournaments = handler.getTournamensList(selectedItem);
        PlannedTournamentModelToUIConverter converter = new PlannedTournamentModelToUIConverter();
        var migratedTournaments = filteredTournaments.stream()
                .map(converter::convertTo)
                .toList();
        Log.debugf("TourCalService:: updateRangeView : for selected item: %s and found %d ",selectedItem, filteredTournaments.size());
        return migratedTournaments;
    }
}
