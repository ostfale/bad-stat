package de.ostfale.qk.web.internal;

import de.ostfale.qk.parser.tournament.api.TournamentParser;
import de.ostfale.qk.web.api.WebService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class TournamentWebService  implements WebService {

    private static final Logger log = Logger.getLogger(TournamentWebService.class);

    private static final String BASE_URL = "https://dbv.turnier.de/player-profile/";

    //https://dbv.turnier.de/player-profile/bd337124-44d1-42c1-9c30-8bed91781a9b/tournaments/2025

    @Inject
    TournamentParser parser;

    @Override
    public Integer getNumberOfTournamentsForYearAndPlayer(Integer year, String player) {
        log.debugf("Get number of tournaments for year %d and player %s", year, player);
        return 0;
    }

    private String preparePlayerTournamentsURIForYear(String tournamentsPlayerID, Integer year) {
        return String.format("/tournaments/%d/players", year);
    }
}
