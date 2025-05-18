package de.ostfale.qk.domain.player;

import jakarta.annotation.Nonnull;

import java.util.Objects;

public record PlayerId(
        @Nonnull String playerId) {

    private static final String PLAYER_ID_ERROR = "playerId must not be null or blank";

    public PlayerId {
        validatePlayerId(playerId);
    }

    private static void validatePlayerId(String playerId) {
        if (Objects.isNull(playerId) || playerId.trim().isBlank()) {
            throw new IllegalArgumentException(PLAYER_ID_ERROR);
        }
    }
}
