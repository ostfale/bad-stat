package de.ostfale.qk.domain.player;

import jakarta.annotation.Nonnull;

import java.util.Objects;

public record PlayerTournamentId(
        @Nonnull String tournamentId
) {

    private static final String PLAYER_TOURNAMENT_ID_ERROR = "playerId must not be null or blank";

    public PlayerTournamentId {
        validatePlayerId(tournamentId);
    }

    private static void validatePlayerId(String tournamentId) {
        if (Objects.isNull(tournamentId) || tournamentId.trim().isBlank()) {
            throw new IllegalArgumentException(PLAYER_TOURNAMENT_ID_ERROR);
        }
    }
}
