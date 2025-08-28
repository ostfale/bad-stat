package de.ostfale.qk.domain.converter;

import de.ostfale.qk.domain.tourcal.PlannedTournament;
import de.ostfale.qk.ui.tourcalendar.model.TourCalUIModel;

public class PlannedTournamentModelToUIConverter implements Converter<PlannedTournament, TourCalUIModel> {

    @Override
    public TourCalUIModel convertTo(PlannedTournament source) {
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
