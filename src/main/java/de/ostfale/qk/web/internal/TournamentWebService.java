package de.ostfale.qk.web.internal;

import de.ostfale.qk.domain.tournament.Tournament;
import de.ostfale.qk.parser.HtmlParserException;
import de.ostfale.qk.parser.web.tournament.WebTournamentParserService;
import de.ostfale.qk.web.common.CookieDialogHandler;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.htmlunit.html.HtmlPage;

import java.time.Year;
import java.util.List;

@ApplicationScoped
public class TournamentWebService extends BaseWebService {

    @Inject
    WebTournamentParserService webTournamentParserService;

    @Inject
    CookieDialogHandler cookieDialogHandler;

    @Override
    public Integer getNumberOfTournamentsForYearAndPlayer(Integer year, String playerTournamentsId) throws HtmlParserException {
        Log.debugf("Get number of tournaments for year %d and player tournament id %s", year, playerTournamentsId);

        String tournamentsURI = null;
        if (year == Year.now().getValue()) {
            tournamentsURI = preparePlayerTournamentsUrl(playerTournamentsId);
        } else {
            tournamentsURI = preparePlayerTournamentsUrl(playerTournamentsId, year.toString());
        }

        Log.debugf("Load tournaments page %s", tournamentsURI);
        HtmlPage tournamentPage = cookieDialogHandler.loadWebsite(tournamentsURI);
        return webTournamentParserService.getNumberOfTournaments(tournamentPage);
    }

    @Override
    public List<Tournament> scrapeAllTournamentsForPlayerAndYear(Integer year, String playerTournamentId) {
        String tournamentsURI = preparePlayerTournamentsUrl(playerTournamentId, year.toString());
        HtmlPage tournamentPage = cookieDialogHandler.loadWebsite(tournamentsURI);

        try {
            List<Tournament> tournaments = webTournamentParserService.parseAllYearlyTournamentsForPlayer(tournamentPage);
            Log.debugf("TournamentWebService :: Successfully scraped %d tournaments for player %s and year %d", tournaments.size(), playerTournamentId, year);
            return tournaments;
        } catch (HtmlParserException e) {
            Log.errorf("TournamentWebService :: Failed to parse tournaments page %s", e);
            throw new RuntimeException(e);
        }
    }
}
