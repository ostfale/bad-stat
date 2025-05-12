package de.ostfale.qk.persistence.ranking;

import de.ostfale.qk.data.dashboard.model.RankingPlayerCacheData;
import de.ostfale.qk.domain.player.Player;
import de.ostfale.qk.parser.BaseParserTest;
import de.ostfale.qk.parser.ranking.internal.ExcelRankingParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unittest")
@DisplayName("Test the ranking player cache service functions")
class RankingPlayerCacheDataTest extends BaseParserTest {

    private static final String FILE_TEST_NAME = "ranking/Ranking_2025_Part.xlsx";

    private final ExcelRankingParser parser = new ExcelRankingParser();

    private List<Player> players;

    @BeforeEach
    void setUp() throws URISyntaxException, FileNotFoundException {
        File file = readFile(FILE_TEST_NAME);
        players = parser.parseRankingFileToPlayers(new FileInputStream(file));
    }

    @Test
    @DisplayName("Test getNumberOfPlayers should be  0 for empty list")
    void testGetNumberOfPlayersWithEmptyList() {
        // given
        RankingPlayerCacheData cache = new RankingPlayerCacheData(List.of());

        // when
        var result = cache.getNumberOfPlayers();

        assertEquals(0, result);
    }

    @Test
    @DisplayName("Test number of players")
    void testGetNumberOfPlayer() {
        // given
        RankingPlayerCacheData cache = new RankingPlayerCacheData(players);
        Long expectedNofPlayers = 38L;

        // when
        var result = cache.getNumberOfPlayers();

        // then
        assertEquals(expectedNofPlayers, result);
    }

    @Test
    @DisplayName("Test number of female players")
    void testGetNumberOfFemalePlayer() {
        // given
        RankingPlayerCacheData cache = new RankingPlayerCacheData(players);
        Long expectedNofPlayers = 20L;

        // when
        var result = cache.getNumberOfFemalePlayers();

        // then
        assertEquals(expectedNofPlayers, result);
    }

    @Test
    @DisplayName("Test number of male players")
    void testGetNumberOfMalePlayer() {
        // given
        RankingPlayerCacheData cache = new RankingPlayerCacheData(players);
        Long expectedNofPlayers = 18L;

        // when
        var result = cache.getNumberOfMalePlayers();

        // then
        assertEquals(expectedNofPlayers, result);
    }

 /*   @Test
    void testGetNumberOfPlayersWithNullList() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new RankingPlayerCache(null);
        });
        assertEquals("Players list cannot be null", exception.getMessage());
    }*/
}
