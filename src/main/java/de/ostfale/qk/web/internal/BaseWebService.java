package de.ostfale.qk.web.internal;

import de.ostfale.qk.web.api.WebService;
import io.quarkus.logging.Log;

import java.util.Objects;

public abstract class BaseWebService implements WebService {

    @Override
    public String preparePlayerTournamentsUrl(String playerTournamentId) {
        Log.debugf("Prepare player tournaments url for %s", playerTournamentId);
        Objects.requireNonNull(playerTournamentId, "playerTournamentId must not be null");
        return PLAYER_TOURNAMENTS_URL.replace("{player-tournament-id}", playerTournamentId);
    }

    @Override
    public String preparePlayerTournamentsUrl(String playerTournamentId, String year) {
        Log.debugf("Prepare player tournaments url for %s and year %s", playerTournamentId, year);
        Objects.requireNonNull(year, "year must not be null");
        return preparePlayerTournamentsUrl(playerTournamentId) + "/" + year;
    }
}
