package de.ostfale.qk.web.internal;

import de.ostfale.qk.db.api.TournamentRepository;
import de.ostfale.qk.parser.tournament.api.TournamentParser;
import de.ostfale.qk.parser.tournament.internal.model.TournamentRawModel;
import de.ostfale.qk.parser.tournament.internal.model.TournamentYearRawModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.htmlunit.html.HtmlPage;
import org.jboss.logging.Logger;

import java.time.Year;
import java.util.List;

@ApplicationScoped
public class TournamentWebService extends BaseWebService {

    private static final Logger log = Logger.getLogger(TournamentWebService.class);

    @Inject
    TournamentParser parser;

    @Inject
    TournamentRepository tournamentRepository;

    @Override
    public Integer getNumberOfTournamentsForYearAndPlayer(Integer year, String playerTournamentsId) {
        log.debugf("Get number of tournaments for year %d and player tournament id %s", year, playerTournamentsId);

        String tournamentsURI = null;
        if (year == Year.now().getValue()) {
            tournamentsURI = preparePlayerTournamentsUrl(playerTournamentsId);
        } else {
            tournamentsURI = preparePlayerTournamentsUrl(playerTournamentsId, year.toString());
        }

        log.debugf("Load tournaments page %s", tournamentsURI);
        HtmlPage tournamentPage = cookieDialogHandler.loadWebsite(tournamentsURI);
        return parser.parseNofTournaments(tournamentPage.getActiveElement());
    }

    @Override
    public List<TournamentRawModel> getTournamentsForYearAndPlayer(Integer year, String playerTournamentId) {
        log.debugf("WebService :: Get tournaments for year %d and player tournament id %s", year, playerTournamentId);
        String tournamentsURI = preparePlayerTournamentsUrl(playerTournamentId, year.toString());
        HtmlPage tournamentPage = cookieDialogHandler.loadWebsite(tournamentsURI);
        TournamentYearRawModel tournamentYearRawModel = parser.parseTournamentYear(year.toString(), tournamentPage.getActiveElement());
        return tournamentYearRawModel.tournaments();
    }
}
