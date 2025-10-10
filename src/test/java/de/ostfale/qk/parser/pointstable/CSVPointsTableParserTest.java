package de.ostfale.qk.parser.pointstable;

import de.ostfale.qk.parser.BaseParserTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Test CSV ranking point parser")
@Tag("unittest")
class CSVPointsTableParserTest extends BaseParserTest {

    private static final String TEST_FILE_NAME = "ranking/u17points_test.csv";

    private CSVPointsTableParser sut;

    @BeforeEach
    void setUp() {
        sut = new CSVPointsTableParser();
    }

    @Test
    @DisplayName("Parse a valid ranking points file with regular tournament")
    void parse() throws URISyntaxException, IOException {
        // given
        var fileContent = readFileToString(TEST_FILE_NAME);

        var expectedNumberOfTournamentTypes = 17;
        var expectedFirstColHeaderName = "Platz";
        var expectedFirstRankingIndexNumber = "1";
        var expectedLastRankingIndexNumber = "33";
        var expectedSecondColHeaderName = "E-RLT";
        var expectedLastColHeaderName = "U17-EM";
        var expectedRankingPointsFirstPlace1 = "1691";
        var expectedRankingPointsFirstPlace33 = "152";
        var expectedRankingPointsLastPlace1 = "44745";
        var expectedRankingPointsLastPlace33 = "4027";

        // when
        var result = sut.parse(fileContent);

        // then
        assertAll("Grouped assertions for parsing ranking points file",
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.isEmpty()).isFalse(),
                () -> assertThat(result.size()).isEqualTo(expectedNumberOfTournamentTypes),
                () -> assertThat(result.getFirst().pointsDisplayTypes().getDisplayName()).isEqualTo(expectedFirstColHeaderName),
                () -> assertThat(result.get(1).pointsDisplayTypes().getDisplayName()).isEqualTo(expectedSecondColHeaderName),
                () -> assertThat(result.get(1).rankingPoints().getFirst()).isEqualTo(expectedRankingPointsFirstPlace1),
                () -> assertThat(result.get(1).rankingPoints().getLast()).isEqualTo(expectedRankingPointsFirstPlace33),
                () -> assertThat(result.getFirst().rankingPoints().getFirst()).isEqualTo(expectedFirstRankingIndexNumber),
                () -> assertThat(result.getFirst().rankingPoints().get(30)).isEqualTo(expectedLastRankingIndexNumber),
                () -> assertThat(result.getLast().pointsDisplayTypes().getDisplayName()).isEqualTo(expectedLastColHeaderName),
                () -> assertThat(result.getLast().rankingPoints().getFirst()).isEqualTo(expectedRankingPointsLastPlace1),
                () -> assertThat(result.getLast().rankingPoints().get(30)).isEqualTo(expectedRankingPointsLastPlace33)
        );
    }
}
