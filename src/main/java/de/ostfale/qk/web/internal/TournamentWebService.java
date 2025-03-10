package de.ostfale.qk.web.internal;

import de.ostfale.qk.parser.tournament.api.TournamentParser;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.htmlunit.html.HtmlPage;
import org.jboss.logging.Logger;

@ApplicationScoped
public class TournamentWebService extends BaseWebService {

    private static final Logger log = Logger.getLogger(TournamentWebService.class);

    @Inject
    TournamentParser parser;

    @Override
    public Integer getNumberOfTournamentsForYearAndPlayer(Integer year, String player) {
        String playerTournamentsURI = preparePlayerTournamentsUrl(player);
        HtmlPage thisYearsTournament = cookieDialogHandler.loadWebsite(playerTournamentsURI);
        Integer nofTournaments = parser.parseNofTournaments("2025", thisYearsTournament.getActiveElement());
        log.debugf("Found %d tournaments for year %d and player %s", nofTournaments, year, player);
        return nofTournaments;
    }

    private String preparePlayerTournamentsURIForYear(String tournamentsPlayerID, Integer year) {
        return String.format("/tournaments/%d/players", year);
    }
}
