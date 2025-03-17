package de.ostfale.qk.web.api;

public interface WebService extends WebUrlFacade {

    Integer getNumberOfTournamentsForYearAndPlayer(Integer year, String playerTournamentId);
}
