package de.ostfale.qk.data.player.model;

import de.ostfale.qk.domain.player.PlayerId;
import jakarta.annotation.Nonnull;

import java.util.Objects;

/**
 * Record representing favorite player data with mandatory ID and name.
 * Performs validation of both fields during construction.
 */
public record FavPlayerData(
        @Nonnull PlayerId playerId,
        @Nonnull String playerName
) {
    private static final String PLAYER_ID_ERROR = "PlayerId must not be null";
    private static final String PLAYER_NAME_ERROR = "playerName must not be null or blank";

    public FavPlayerData {
        validatePlayerId(playerId);
        validatePlayerName(playerName);
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

    @Override
    @Nonnull
    public String toString() {
        return playerName;
    }
}
