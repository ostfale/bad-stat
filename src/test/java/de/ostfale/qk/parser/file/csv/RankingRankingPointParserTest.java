package de.ostfale.qk.parser.file.csv;

import de.ostfale.qk.parser.BaseParserTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Test RankingPointsParser")
@Tag("unittest")
class RankingRankingPointParserTest extends BaseParserTest {

    private static final String TEST_FILE_NAME = "ranking/u17points_test.csv";

    private RankingPointsParser sut;

    @BeforeEach
    void setUp() {
        sut = new RankingPointsParser();
    }

    @Test
    @DisplayName("Read the u17 csv list into the domain model classes")
    void testReadingPointsFile() throws URISyntaxException {
        // given
        List<String> rankingPointsFileList = List.of(TEST_FILE_NAME);
        sut.setRankingPointsList(rankingPointsFileList);

        var expectedNumberOfTournamentTypes = 16;
        var expectedFirstTournamentType = "E-RLT";
        var expectedLastTournamentType = "U17-EM";
        var expectedRankingPointsFirstPlace1 = 1691;
        var expectedRankingPointsFirstPlace33 = 152;
        var expectedRankingPointsLastPlace1 = 44745;
        var expectedRankingPointsLastPlace33 = 4027;

        // when
        var result = sut.getTourPointsViewModel();

        // then
        assertAll("Grouped assertions for parsing ranking points file",
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getTourTypePointsList().isEmpty()).isFalse(),
                () -> assertThat(result.getTourTypePointsList().size()).isEqualTo(expectedNumberOfTournamentTypes),
                () -> assertThat(result.getTourTypePointsList().getFirst().pointsTourType().getDisplayName()).isEqualTo(expectedFirstTournamentType),
                () -> assertThat(result.getTourTypePointsList().getFirst().rankingPointList().getFirst().value()).isEqualTo(expectedRankingPointsFirstPlace1),
                () -> assertThat(result.getTourTypePointsList().getFirst().rankingPointList().get(30).value()).isEqualTo(expectedRankingPointsFirstPlace33),
                () -> assertThat(result.getTourTypePointsList().getLast().pointsTourType().getDisplayName()).isEqualTo(expectedLastTournamentType),
                () -> assertThat(result.getTourTypePointsList().getLast().rankingPointList().getFirst().value()).isEqualTo(expectedRankingPointsLastPlace1),
                () -> assertThat(result.getTourTypePointsList().getLast().rankingPointList().get(30).value()).isEqualTo(expectedRankingPointsLastPlace33)
        );
    }
}
