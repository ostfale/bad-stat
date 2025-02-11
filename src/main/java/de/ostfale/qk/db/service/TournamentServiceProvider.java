package de.ostfale.qk.db.service;

import de.ostfale.qk.db.api.TournamentRepository;
import de.ostfale.qk.db.api.tournament.TournamentService;
import de.ostfale.qk.db.internal.Tournament;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class TournamentServiceProvider implements TournamentService {

    private static final Logger log = Logger.getLogger(TournamentServiceProvider.class);

    @Inject
    TournamentRepository tournamentRepository;

    @Override
    public List<Tournament> getAllTournamentsForYearAndPlayer(Integer year, String player) {
        List<Tournament> tournaments = tournamentRepository.findByTournamentYear(year);
        var result = tournaments.stream().filter(tournament -> tournament.containsPlayer(player)).toList();
        log.infof("Found %d tournaments for year %d and player %s", result.size(), year, player);
        return result;
    }
}
