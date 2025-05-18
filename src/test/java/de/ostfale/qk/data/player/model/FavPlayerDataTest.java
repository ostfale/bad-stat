package de.ostfale.qk.data.player.model;

import de.ostfale.qk.domain.player.PlayerId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Test FavPlayerData record initialization")
@Tag("unittest")
class FavPlayerDataTest {

    @Test
    void testToStringReturnsPlayerName() {
        // given
        String playerId = "player123";
        String playerName = "John Doe";
        PlayerId pId = new PlayerId(playerId);

        FavPlayerData favPlayerData = new FavPlayerData(pId, playerName);

        // when
        String result = favPlayerData.toString();

        // then
        assertThat(result).isEqualTo(playerName);
    }

    @Test
    void testToStringWithEmptyPlayerNameThrowsException() {
        // given
        String playerId = "player123";
        String playerName = " ";
        PlayerId pId = new PlayerId(playerId);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> new FavPlayerData(pId, playerName));
    }

    @Test
    void testToStringWithNullPlayerNameThrowsException() {
        // given
        String playerId = "player123";
        String playerName = null;
        PlayerId pId = new PlayerId(playerId);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> new FavPlayerData(pId, playerName));
    }

    @Test
    void testToStringWithValidPlayerIdAndPlayerName() {
        // given
        String playerId = "validId123";
        String playerName = "Alice";
        PlayerId pId = new PlayerId(playerId);

        // when
        FavPlayerData favPlayerData = new FavPlayerData(pId, playerName);

        // then
        assertThat(favPlayerData.toString()).isEqualTo(playerName);
    }

    @Test
    void testToStringWithEmptyPlayerIdThrowsException() {
        // given
        String playerId = " ";

        // when & then
        assertThrows(IllegalArgumentException.class, () -> new PlayerId(playerId));
    }
}
