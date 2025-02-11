package de.ostfale.qk.db.api.tournament;

import de.ostfale.qk.db.internal.Tournament;

import java.util.List;

public interface TournamentService {

    List<Tournament> getAllTournamentsForYearAndPlayer(Integer year, String player);
}
