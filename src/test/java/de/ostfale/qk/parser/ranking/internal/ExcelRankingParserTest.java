package de.ostfale.qk.parser.ranking.internal;

import de.ostfale.qk.parser.BaseParserTest;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
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
    @DisplayName("")
    void readExcelFile() throws URISyntaxException {
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
        List<RankingPlayer> rankingPlayers = parser.parseRankingFile(file);

        // then
        var playerOptional = rankingPlayers.stream().filter(player -> player.getPlayerId().equalsIgnoreCase("06-153539"))
                .findFirst();

        assertAll("Test reading an excel ranking file",
                () -> assertNotNull(rankingPlayers),
                () -> assertEquals(37, rankingPlayers.size()),
                () -> assertTrue(playerOptional.isPresent()),
                () -> assertEquals(expectedFirstName, playerOptional.orElseThrow().getFirstName()),
                () -> assertEquals(expectedLastName, playerOptional.orElseThrow().getLastName()),
                () -> assertEquals(expectedBirthYear, playerOptional.orElseThrow().getYearOfBirth()),
                () -> assertEquals(expectedGeneralAgeClass, playerOptional.orElseThrow().getAgeClassGeneral()),
                () -> assertEquals(expectedDetailedAgeClass, playerOptional.orElseThrow().getAgeClassDetail()),
                () -> assertEquals(expectedClub, playerOptional.orElseThrow().getClubName()),
                () -> assertEquals(expectedDistrict, playerOptional.orElseThrow().getDistrictName()),
                () -> assertEquals(expectedState, playerOptional.orElseThrow().getStateName()),
                () -> assertEquals(expectedStateGroup, playerOptional.orElseThrow().getStateGroup()),
                () -> assertEquals(expectedSinglePoints, playerOptional.orElseThrow().getSinglePoints()),
                () -> assertEquals(expectedSingleRanking, playerOptional.orElseThrow().getSingleRanking()),
                () -> assertEquals(expectedSingleTournaments, playerOptional.orElseThrow().getSingleTournaments()),
                () -> assertEquals(expectedDoublePoints, playerOptional.orElseThrow().getDoublePoints()),
                () -> assertEquals(expectedDoubleRanking, playerOptional.orElseThrow().getDoubleRanking()),
                () -> assertEquals(expectedDoubleTournaments, playerOptional.orElseThrow().getDoubleTournaments()),
                () -> assertEquals(expectedMixedPoints, playerOptional.orElseThrow().getMixedPoints()),
                () -> assertEquals(expectedMixedRanking, playerOptional.orElseThrow().getMixedRanking()),
                () -> assertEquals(expectedMixedTournaments, playerOptional.orElseThrow().getMixedTournaments())
        );
    }
}
