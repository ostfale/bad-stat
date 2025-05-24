package de.ostfale.qk.data.player.model;

import de.ostfale.qk.domain.player.PlayerId;
import de.ostfale.qk.domain.player.PlayerTournamentId;
import jakarta.annotation.Nonnull;

import java.util.Objects;

/**
 * Record representing favorite player data with mandatory ID and name.
 * Performs validation of both fields during construction.
 */
public record FavPlayerData(
        @Nonnull PlayerId playerId,
        @Nonnull PlayerTournamentId playerTournamentId,
        @Nonnull String playerName
) {
    private static final String PLAYER_ID_ERROR = "PlayerId must not be null";
    private static final String PLAYER_NAME_ERROR = "playerName must not be null or blank";
    private static final String PLAYER_TOURNAMENT_ID_ERROR = "playerTournamentId must not be null";

    public FavPlayerData {
        validatePlayerId(playerId);
        validatePlayerName(playerName);
        validatePlayerTournamentId(playerTournamentId);
    }

    private static void validatePlayerId(PlayerId playerId) {
        if (Objects.isNull(playerId)) {
            throw new IllegalArgumentException(PLAYER_ID_ERROR);
        }
    }

    private static void validatePlayerName(String playerName) {
        if (Objects.isNull(playerName) || playerName.trim().isBlank()) {
            throw new IllegalArgumentException(PLAYER_NAME_ERROR);
        }
    }

    private static void validatePlayerTournamentId(PlayerTournamentId playerTournamentId) {
        if (Objects.isNull(playerTournamentId)) {
            throw new IllegalArgumentException(PLAYER_TOURNAMENT_ID_ERROR);
        }
    }

    @Override
    @Nonnull
    public String toString() {
        return playerName;
    }
}
