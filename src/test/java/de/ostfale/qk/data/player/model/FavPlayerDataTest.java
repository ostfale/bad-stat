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
    @DisplayName("Test valid FavPlayerData record initialization with player name:")
    void testFullObjectWithValidData() {
        // given
        PlayerId pId = new PlayerId(PLAYER_ID);
        PlayerTournamentId pTournamentId = new PlayerTournamentId(PLAYER_TOURNAMENT_ID);
        FavPlayerYearStat favPlayerYearStat = new FavPlayerYearStat(2025, 4, 0);

        // when
        var result = new FavPlayerData(pId, pTournamentId, PLAYER_NAME);
        result.addYearStat(favPlayerYearStat);

        // then
        assertAll(
                "Test all record values initialized",
                () -> assertThat(result.playerName()).isEqualTo(PLAYER_NAME),
                () -> assertThat(result.playerId().playerId()).isEqualTo(PLAYER_ID),
                () -> assertThat(result.playerTournamentId().tournamentId()).isEqualTo(PLAYER_TOURNAMENT_ID),
                () -> assertThat(result.getYearStat(2025)).isNotNull(),
                () -> assertThat(result.getYearStat(2025).year()).isEqualTo(2025),
                () -> assertThat(result.getYearStat(2025).played()).isEqualTo(4),
                () -> assertThat(result.getYearStat(2025).loaded()).isEqualTo(0)
        );

    }

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
    void testToStringWithEmptyPlayerNameThrowsException() {
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
        assertThrows(IllegalArgumentException.class, () -> new FavPlayerData(pId, pTournamentId, playerName));
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

    @Test
    @DisplayName("Test adding a valid year statistic")
    void testAddValidYearStat() {
        // given
        PlayerId pId = new PlayerId(PLAYER_ID);
        PlayerTournamentId pTournamentId = new PlayerTournamentId(PLAYER_TOURNAMENT_ID);
        FavPlayerData favPlayerData = new FavPlayerData(pId, pTournamentId, PLAYER_NAME);
        FavPlayerYearStat favPlayerYearStat = new FavPlayerYearStat(2025, 10, 2);

        // when
        favPlayerData.addYearStat(favPlayerYearStat);

        // then
        assertThat(favPlayerData.getYearStat(2025)).isEqualTo(favPlayerYearStat);
    }

    @Test
    @DisplayName("Test adding multiple year statistics and retrieving them")
    void testAddMultipleYearStats() {
        // given
        PlayerId pId = new PlayerId(PLAYER_ID);
        PlayerTournamentId pTournamentId = new PlayerTournamentId(PLAYER_TOURNAMENT_ID);
        FavPlayerData favPlayerData = new FavPlayerData(pId, pTournamentId, PLAYER_NAME);
        FavPlayerYearStat stat2025 = new FavPlayerYearStat(2025, 5, 1);
        FavPlayerYearStat stat2026 = new FavPlayerYearStat(2026, 3, 0);

        // when
        favPlayerData.addYearStat(stat2025);
        favPlayerData.addYearStat(stat2026);

        // then
        assertAll(
                () -> assertThat(favPlayerData.getYearStat(2025)).isEqualTo(stat2025),
                () -> assertThat(favPlayerData.getYearStat(2026)).isEqualTo(stat2026)
        );
    }

    @Test
    @DisplayName("Test adding a null year statistic throws NullPointerException")
    void testAddNullYearStat() {
        // given
        PlayerId pId = new PlayerId(PLAYER_ID);
        PlayerTournamentId pTournamentId = new PlayerTournamentId(PLAYER_TOURNAMENT_ID);
        FavPlayerData favPlayerData = new FavPlayerData(pId, pTournamentId, PLAYER_NAME);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> favPlayerData.addYearStat(null));
    }
}
