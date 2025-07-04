package de.ostfale.qk.web.api;

import de.ostfale.qk.domain.tournament.Tournament;

import java.util.List;

public interface WebService extends WebUrlFacade {

    Integer getNumberOfTournamentsForYearAndPlayer(Integer year, String playerTournamentId);

    List<Tournament> scrapeAllTournamentsForPlayerAndYear(Integer year, String playerTournamentId);
}
