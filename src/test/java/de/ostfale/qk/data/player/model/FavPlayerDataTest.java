package de.ostfale.qk.data.player.model;

import de.ostfale.qk.domain.player.PlayerId;
import de.ostfale.qk.domain.player.PlayerTournamentId;
import de.ostfale.qk.parser.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Test FavPlayerData record initialization")
@Tag("unittest")
class FavPlayerDataTest extends BaseTest {



    @Test
    void testToStringReturnsPlayerName() {
        // given
        PlayerId pId = new PlayerId(PLAYER_ID);
        PlayerTournamentId pTournamentId = new PlayerTournamentId(PLAYER_TOURNAMENT_ID);

        FavPlayerData favPlayerData = new FavPlayerData(pId, pTournamentId, PLAYER_NAME);

        // when
        String result = favPlayerData.toString();

        // then
        assertAll(
                "Test valid FavPlayerData record initialization with player name:",
                () -> assertThat(result).isEqualTo(PLAYER_NAME)
        );
    }

    @Test
    void testToStringWithEmptyPlayerNameThrowsException () {
        // given
        PlayerId pId = new PlayerId(PLAYER_ID);
        PlayerTournamentId pTournamentId = new PlayerTournamentId(PLAYER_TOURNAMENT_ID);
        String playerName = " ";

        // when & then
        assertThrows(IllegalArgumentException.class, () -> new FavPlayerData(pId, pTournamentId, playerName));
    }

    @Test
    void testToStringWithNullPlayerNameThrowsException() {
        // given
        PlayerId pId = new PlayerId(PLAYER_ID);
        PlayerTournamentId pTournamentId = new PlayerTournamentId(PLAYER_TOURNAMENT_ID);
        String playerName = null;

        // when & then
        assertThrows(IllegalArgumentException.class, () ->  new FavPlayerData(pId, pTournamentId, playerName));
    }


    @Test
    void testToStringWithEmptyPlayerIdThrowsException() {
        // given
        String playerId = " ";

        // when & then
        assertThrows(IllegalArgumentException.class, () -> new PlayerId(playerId));
    }

    @Test
    void testToStringWithNullPlayerTournamentIdThrowsException() {
        // given
        PlayerId pId = new PlayerId(PLAYER_ID);
        PlayerTournamentId pTournamentId = null;

        // when & then
        assertThrows(IllegalArgumentException.class, () -> new FavPlayerData(pId, pTournamentId, PLAYER_NAME));
    }

    @Test
    void testToStringReturnsPlayerNameForValidInput() {
        // given
        PlayerId pId = new PlayerId(PLAYER_ID);
        PlayerTournamentId pTournamentId = new PlayerTournamentId(PLAYER_TOURNAMENT_ID);

        // when
        FavPlayerData favPlayerData = new FavPlayerData(pId, pTournamentId, PLAYER_NAME);

        // then
        assertThat(favPlayerData.toString()).isEqualTo(PLAYER_NAME);
    }
}
