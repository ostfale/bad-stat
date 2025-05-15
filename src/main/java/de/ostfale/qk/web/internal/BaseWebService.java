package de.ostfale.qk.web.internal;

import de.ostfale.qk.web.common.CookieDialogHandler;
import de.ostfale.qk.web.api.WebService;
import org.jboss.logging.Logger;

import java.util.Objects;

public abstract class BaseWebService implements WebService {

    private static final Logger log = Logger.getLogger(TournamentWebService.class);

    protected final CookieDialogHandler cookieDialogHandler = new CookieDialogHandler();

    @Override
    public String preparePlayerTournamentsUrl(String playerTournamentId) {
        log.debugf("Prepare player tournaments url for %s", playerTournamentId);
        Objects.requireNonNull(playerTournamentId, "playerTournamentId must not be null");
        return PLAYER_TOURNAMENTS_URL.replace("{player-tournament-id}", playerTournamentId);
    }

    @Override
    public String preparePlayerTournamentsUrl(String playerTournamentId, String year) {
        log.debugf("Prepare player tournaments url for %s and year %s", playerTournamentId, year);
        Objects.requireNonNull(year, "year must not be null");
        return preparePlayerTournamentsUrl(playerTournamentId) + "/" + year;
    }
}
