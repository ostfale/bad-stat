package de.ostfale.qk.parser.pointstable;

import de.ostfale.qk.app.async.SyncDataLoaderService;
import de.ostfale.qk.domain.discipline.AgeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Test CSVPointsTableService")
@Tag("unittest")
class CSVPointsTableServiceTest {

    private static final String U09_TEST_FILE_NAME = "pointstable/u09points_test.csv";
    private static final String U17_TEST_FILE_NAME = "pointstable/u17points_test.csv";

    private CSVPointsTableService sut;

    @BeforeEach
    void setUp() {
        var syncDataLoaderService = new SyncDataLoaderService();
        var dsvPointsTableParser = new CSVPointsTableParser();
        sut = new CSVPointsTableService(syncDataLoaderService, dsvPointsTableParser);
    }

    @Test
    @DisplayName("Read age class points table files")
    void testReadingAgeClassFiles() {
        // given
        List<String> ageClassPointsFilePaths = List.of(U09_TEST_FILE_NAME, U17_TEST_FILE_NAME);

        var expectedNumberOfAgeClasses = 2;
        var expectedAgeClass_U17 = AgeClass.U17;
        var expectedAgeClass_U09 = AgeClass.U9;
        var expectedTournamentType_U17 = 17;
        var expectedTournamentType_U09 = 15;

        // when
        var result = sut.readTablePoints(ageClassPointsFilePaths);

        // then
        assertAll("Grouped assertions for parsing points table files",
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.ageClassTablePointsMap().size()).isEqualTo(expectedNumberOfAgeClasses),
                () -> assertThat(result.ageClassTablePointsMap().containsKey(expectedAgeClass_U09)).isTrue(),
                () -> assertThat(result.ageClassTablePointsMap().containsKey(expectedAgeClass_U17)).isTrue(),
                () -> assertThat(result.ageClassTablePointsMap().get(expectedAgeClass_U17).size()).isEqualTo(expectedTournamentType_U17),
                () -> assertThat(result.ageClassTablePointsMap().get(expectedAgeClass_U09).size()).isEqualTo(expectedTournamentType_U09)
        );
    }
}
