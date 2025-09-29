package de.ostfale.qk.domain.points;

import de.ostfale.qk.domain.discipline.AgeClass;
import de.ostfale.qk.parser.BaseTest;
import de.ostfale.qk.parser.file.csv.RankingPointsParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DisplayName("Test record and filter functions")
@Tag("unittest")
class TourPointsViewModelTest extends BaseTest {

    private static final String TEST_FILE_NAME = "ranking/u17points_test.csv";

    private final RankingPointsParser rankingPointsParser = new RankingPointsParser();

    private TourPointsViewModel sut;

    @Test
    @DisplayName("Return all tournament types for the given age class")
    void testTournamentTypesExtraction() throws URISyntaxException {
        // given
        List<String> rankingPointsFileList = List.of(TEST_FILE_NAME);
        rankingPointsParser.setRankingPointsList(rankingPointsFileList);
        sut = rankingPointsParser.getTourPointsViewModel();

        var expectedTournamentTypes = List.of("E-RLT", "E-KM", "D2-RLT", "D2-BM", "D1-RLT", "D1-BM", "C2-RLT", "C2-LM", "C1-RLT",
                "C1-LM", "B-RLT", "B-GM", "A-RLT", "A-DM", "BEC-U17", "U17-EM");

        // when
        var result = sut.getHeaderNamesForAgeClass(AgeClass.U17);

        // then
        assertAll("Grouped assertions for testing TourPointsViewModel",
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.size()).isNotZero(),
                () -> assertThat(result).isEqualTo(expectedTournamentTypes)
        );
    }

    @Test
    @DisplayName("Return points for tournament type sorted descending")
    void testReadingSortedPoints() throws URISyntaxException {
        // given
        List<String> rankingPointsFileList = List.of(TEST_FILE_NAME);
        rankingPointsParser.setRankingPointsList(rankingPointsFileList);
        sut = rankingPointsParser.getTourPointsViewModel();
        var expectedPositionValueFirstRow = 1;
        var expectedNoColumns = 17;
        var expectedNoRows = 31;
        var firstValueERLT = 1691;
        var lastValueERLT = 152;
        var firstValueEM = 44745;
        var lastValueEM = 4027;

        // when
        var result = sut.getPointsForAgeClass(AgeClass.U17);

        // then
        assertAll("Grouped assertions for sorted point values",
                () -> assertThat(result.getFirst().size()).isEqualTo(expectedNoColumns),
                () -> assertThat(result.size()).isEqualTo(expectedNoRows),
                () -> assertThat(result.getFirst().getFirst().value()).isEqualTo(expectedPositionValueFirstRow),
                () -> assertThat(result.getFirst().get(1).value()).isEqualTo(firstValueERLT),
                () -> assertThat(result.getLast().get(1).value()).isEqualTo(lastValueERLT),
                () -> assertThat(result.getFirst().getLast().value()).isEqualTo(firstValueEM),
                () -> assertThat(result.getLast().getLast().value()).isEqualTo(lastValueEM)
        );
    }
}
