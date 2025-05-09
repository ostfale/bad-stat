package de.ostfale.qk.web.api;

import de.ostfale.qk.parser.tournament.model.TournamentParserModel;

import java.util.List;

public interface WebService extends WebUrlFacade {

    Integer getNumberOfTournamentsForYearAndPlayer(Integer year, String playerTournamentId);

    List<TournamentParserModel> getTournamentsForYearAndPlayer(Integer year, String playerTournamentId);
}
