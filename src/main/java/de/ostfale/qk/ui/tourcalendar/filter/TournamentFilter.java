package de.ostfale.qk.ui.tourcalendar.filter;

import de.ostfale.qk.domain.tourcal.PlannedTournament;

import java.util.List;

public interface TournamentFilter {

    List<PlannedTournament> filterTournaments(List<PlannedTournament> tournaments);

    void resetFilter();

}
