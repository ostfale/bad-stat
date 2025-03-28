package de.ostfale.qk.web.api;

import de.ostfale.qk.parser.tournament.internal.model.TournamentRawModel;

import java.util.List;

public interface WebService extends WebUrlFacade {

    Integer getNumberOfTournamentsForYearAndPlayer(Integer year, String playerTournamentId);

    List<TournamentRawModel> getTournamentsForYearAndPlayer(Integer year, String playerTournamentId);
}
