package de.ostfale.qk.domain.converter;

import de.ostfale.qk.domain.tourcal.PlannedTournament;
import de.ostfale.qk.ui.tourcalendar.model.TourCalUIModel;
import io.quarkus.logging.Log;

public class PlannedTournamentModelToUIConverter implements Converter<PlannedTournament, TourCalUIModel> {

    @Override
    public TourCalUIModel convertTo(PlannedTournament source) {
        Log.debug("PlannedTournamentModelToUIConverter :: Convert domain planned tournament model to UI model");
        return new TourCalUIModel(
                source.startDate(),
                source.closingDate(),
                source.tournamentName(),
                source.tourCategory().getDisplayName(),
                source.location(),
                source.organizer(),
                source.webLinkUrl(),
                source.pdfLinkUrl()
        );
    }
}
