package de.ostfale.qk.app.cache;

import de.ostfale.qk.domain.discipline.AgeClass;
import de.ostfale.qk.domain.player.GenderType;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Tag("unittest")
@DisplayName("Test the ranking player cache ")
class RankingPlayerCacheTest extends BaseParserTest {

    private static final String FILE_TEST_NAME = "ranking/Ranking_2025_Part.xlsx";

    private final ExcelRankingParser parser = new ExcelRankingParser();
    private List<Player> players;
    private RankingPlayerCache sut;

    @BeforeEach
    void setUp() throws URISyntaxException, FileNotFoundException {
        File file = readFile(FILE_TEST_NAME);
        players = parser.parseRankingFileToPlayers(new FileInputStream(file));
        sut = new RankingPlayerCache();
    }

    @Test
    @DisplayName("Test getNumberOfPlayers should be  0 for empty list")
    void testGetNumberOfPlayersWithEmptyList() {
        // given
        sut.getPlayerList().clear();

        // when
        var result = sut.getNumberOfPlayers();

        assertThat(result).isEqualTo(0);
    }

    @Test
    @DisplayName("Test number of players")
    void testGetNumberOfPlayer() {
        // given
        sut.getPlayerList().addAll(players);
        Long expectedNofPlayers = 38L;

        // when
        var result = sut.getNumberOfPlayers();

        // then
        assertThat(result).isEqualTo(expectedNofPlayers);
    }

    @Test
    @DisplayName("Test number of female players")
    void testGetNumberOfFemalePlayer() {
        // given
        sut.getPlayerList().addAll(players);
        Long expectedNofPlayers = 20L;

        // when
        var result = sut.getNumberOfFemalePlayers();

        // then
        assertThat(result).isEqualTo(expectedNofPlayers);
    }

    @Test
    @DisplayName("Test number of male players")
    void testGetNumberOfMalePlayer() {
        // given
        sut.getPlayerList().addAll(players);
        Long expectedNofPlayers = 18L;

        // when
        var result = sut.getNumberOfMalePlayers();

        // then
        assertThat(result).isEqualTo(expectedNofPlayers);
    }

    @Test
    @DisplayName("Test retrieving a player by its player-id")
    void testGettingPlayerById() {
        // given
        String playerId = "01-149945";
        sut.getPlayerList().addAll(players);
        String expectedName = "Kalliope Hermel";

        // when
        var result = sut.getPlayerById(playerId);

        // then
        assertThat(result.getFullName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Test retrieving a player by its name")
    void testGettingPlayerByName() {
        // given
        String playerId = "Leon Kaschura";
        sut.getPlayerList().addAll(players);
        String expectedPlayerId = "01-150083";

        // when
        var result = sut.getPlayerByName(playerId);

        // then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.getFirst().getPlayerId().playerId()).isEqualTo(expectedPlayerId);
    }

    @Test
    @DisplayName("Test filter by gender and age class for female players under U17")
    void testFilterByGenderAndAgeClassForFemalePlayersUnder18() {
        // given
        sut.getPlayerList().addAll(players);
        String ageClass = AgeClass.U17.name();
        String gender = GenderType.FEMALE.getDisplayName();
        int expectedSize = 4;

        // when
        var result = sut.filterByGenderAndAgeClass(ageClass, gender);

        // then
        assertThat(result.size()).isEqualTo(expectedSize);
    }

}
