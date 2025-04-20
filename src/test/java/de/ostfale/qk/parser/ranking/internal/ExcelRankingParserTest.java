package de.ostfale.qk.parser.ranking.internal;

import de.ostfale.qk.domain.player.Group;
import de.ostfale.qk.domain.player.Player;
import de.ostfale.qk.parser.BaseParserTest;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("Test parsing a 2025 ranking file")
@Tag("unittest")
@QuarkusTest
class ExcelRankingParserTest extends BaseParserTest {

    @Inject
    ExcelRankingParser parser;

    @Test
    @DisplayName("Reading Excel File")
    void readExcelFile() throws URISyntaxException, FileNotFoundException {
        // given
        String fileName = "ranking/Ranking_2025_Part.xlsx";
        File file = readFile(fileName);

        var expectedFirstName = "Mats";
        var expectedLastName = "Wohlers";
        var expectedBirthYear = 2008;
        var expectedGeneralAgeClass = "U19";
        var expectedDetailedAgeClass = "U19-1";
        var expectedClub = "TSG Bergedorf";
        var expectedDistrict = "HAM-Hamburg";
        var expectedState = "HAM-Hamburg";
        var expectedStateGroup = Group.NORTH;
        var expectedSinglePoints = 131480;
        var expectedSingleTournaments = 21;
        var expectedSingleRanking = 2;
        var expectedDoublePoints = 122962;
        var expectedDoubleTournaments = 20;
        var expectedDoubleRanking = 11;
        var expectedMixedPoints = 115596;
        var expectedMixedTournaments = 15;
        var expectedMixedRanking = 9;

        // when
        List<Player> rankingPlayers = parser.parseRankingFileToPlayers(new FileInputStream(file));

        // then
        var playerOptional = rankingPlayers
                .stream()
                .filter(player -> player.getPlayerId().playerId().equalsIgnoreCase("06-153539"))
                .findFirst();

        assertAll("Test reading an excel ranking file",
                () -> assertNotNull(rankingPlayers),
                () -> assertEquals(38, rankingPlayers.size()),
                () -> assertTrue(playerOptional.isPresent()),
                () -> assertEquals(expectedFirstName, playerOptional.orElseThrow().getFirstName()),
                () -> assertEquals(expectedLastName, playerOptional.orElseThrow().getLastName()),
                () -> assertEquals(expectedBirthYear, playerOptional.orElseThrow().getYearOfBirth()),
                () -> assertEquals(expectedGeneralAgeClass, playerOptional.orElseThrow().getPlayerInfo().getAgeClassGeneral()),
                () -> assertEquals(expectedDetailedAgeClass, playerOptional.orElseThrow().getPlayerInfo().getAgeClassSpecific()),
                () -> assertEquals(expectedClub, playerOptional.orElseThrow().getPlayerInfo().getClubName()),
                () -> assertEquals(expectedDistrict, playerOptional.orElseThrow().getPlayerInfo().getDistrictName()),
                () -> assertEquals(expectedState, playerOptional.orElseThrow().getPlayerInfo().getStateName()),
                () -> assertEquals(expectedStateGroup, playerOptional.orElseThrow().getPlayerInfo().getGroupName()),
                () -> assertEquals(expectedSinglePoints, playerOptional.orElseThrow().getSingleRankingInformation().rankingPoints()),
                () -> assertEquals(expectedSingleRanking, playerOptional.orElseThrow().getSingleRankingInformation().rankingPosition()),
                () -> assertEquals(expectedSingleTournaments, playerOptional.orElseThrow().getSingleRankingInformation().tournaments()),
                () -> assertEquals(expectedDoublePoints, playerOptional.orElseThrow().getDoubleRankingInformation().rankingPoints()),
                () -> assertEquals(expectedDoubleRanking, playerOptional.orElseThrow().getDoubleRankingInformation().rankingPosition()),
                () -> assertEquals(expectedDoubleTournaments, playerOptional.orElseThrow().getDoubleRankingInformation().tournaments()),
                () -> assertEquals(expectedMixedPoints, playerOptional.orElseThrow().getMixedRankingInformation().rankingPoints()),
                () -> assertEquals(expectedMixedRanking, playerOptional.orElseThrow().getMixedRankingInformation().rankingPosition()),
                () -> assertEquals(expectedMixedTournaments, playerOptional.orElseThrow().getMixedRankingInformation().tournaments())
        );
    }
}
