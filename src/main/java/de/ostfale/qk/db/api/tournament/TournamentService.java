package de.ostfale.qk.db.api.tournament;

import java.util.List;

public interface TournamentService {

    List<Tournament> getAllTournamentsForYearAndPlayer(Integer year, String player);
}
