package de.ostfale.qk.parser;

import de.ostfale.qk.domain.player.PlayerTournamentId;
import io.quarkus.logging.Log;

import java.util.Objects;

public abstract class BaseWebParser implements WebParser {

    private static final String URL_YEAR_SEPARATOR = "/";

    @Override
    public String preparePlayerTournamentsUrl(PlayerTournamentId playerTournamentId) {
        Objects.requireNonNull(playerTournamentId, "playerTournamentId must not be null");

        Log.debugf("BaseWebParser :: preparePlayerTournamentsUrl: %s", playerTournamentId.tournamentId());
        return PLAYER_TOURNAMENTS_URL.replace("{player-tournament-id}", playerTournamentId.tournamentId());
    }

    @Override
    public String preparePlayerTournamentsUrl(PlayerTournamentId playerTournamentId, int year) {
        Objects.requireNonNull(playerTournamentId, "playerTournamentId must not be null");

        Log.debugf("BaseWebParser :: preparePlayerTournamentsUrl: %s, %d", playerTournamentId.tournamentId(), year);
        return preparePlayerTournamentsUrl(playerTournamentId) + URL_YEAR_SEPARATOR + year;
    }
}
