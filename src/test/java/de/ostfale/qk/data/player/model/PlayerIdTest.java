package de.ostfale.qk.data.player.model;

import de.ostfale.qk.domain.player.PlayerId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test player id")
@Tag( "unittest")
class PlayerIdTest {

    private PlayerId playerId;
    private static final String VALID_ID = "123456";

    @BeforeEach
    void setUp() {
        playerId = new PlayerId(VALID_ID);
    }

    @Test
    void shouldCreateValidPlayerId() {
        // then
        assertNotNull(playerId);
        assertEquals(VALID_ID, playerId.playerId());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void shouldThrowExceptionForInvalidId(String invalidId) {
        // when & then
        assertThrows(IllegalArgumentException.class, () -> new PlayerId(invalidId));
    }

    @Test
    void shouldImplementToString() {
        // when
        String result = playerId.playerId();

        // then
        assertEquals(VALID_ID, result);
    }
}
