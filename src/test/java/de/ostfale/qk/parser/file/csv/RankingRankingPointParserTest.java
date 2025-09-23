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

        // when
        var result = sut.getTourPointsViewModel();

        // then
        assertAll("Grouped assertions for parsing ranking points file",
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getTourTypePointsList().isEmpty()).isFalse()
        );
    }
}
