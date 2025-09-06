package de.ostfale.qk.domain.tourcal;

import de.ostfale.qk.parser.BaseParserTest;
import de.ostfale.qk.parser.file.csv.PlannedTournamentParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Tag("unittest")
@DisplayName("Test planned tournaments")
class PlannedTournamentsTest extends BaseParserTest {

    private static final String CALTOUR_TEST_FILE_Y25 = "caltour/CalTour_25.csv";
    private static final String CALTOUR_TEST_FILE_Y26 = "caltour/CalTour_26.csv";
    private static final String CALTOUR_TEST_FILE_EMPTY = "caltour/CalTour_Empty.csv";

    private final PlannedTournamentParser plannedTournamentParser = new PlannedTournamentParser();

    private PlannedTournaments sut;

    @Test
    @DisplayName("Get number of all tournaments ")
    void testNumberOfAllTournaments() throws URISyntaxException {
        // given
        File year25TournamentsFile = readFile(CALTOUR_TEST_FILE_Y25);
        File year26TournamentsFile = readFile(CALTOUR_TEST_FILE_Y26);

        var expectedNumberOfTournaments = 69;
        var expectedNofThisYearTournaments = 50;
        var expectedNofNextYearTournaments = 19;

        // when
        sut = plannedTournamentParser.parseTournamentCalendar(year25TournamentsFile);
        sut.setNextYearsTournaments(plannedTournamentParser.parseTournamentCalendar(year26TournamentsFile).getAllPlannedTournaments());

        var thisYearsTournaments = sut.getThisYearsTournaments();
        var nextYearsTournaments = sut.getNextYearsTournaments();
        var allTournaments = sut.getAllPlannedTournaments();

        // then
        assertAll("Grouped assertions for planned tournaments",
                () -> assertThat(allTournaments).isNotNull(),
                () -> assertThat(allTournaments.size()).isEqualTo(expectedNumberOfTournaments),
                () -> assertThat(thisYearsTournaments.size()).isEqualTo(expectedNofThisYearTournaments),
                () -> assertThat(nextYearsTournaments.size()).isEqualTo(expectedNofNextYearTournaments)
        );
    }

    @Test
    @DisplayName("Get number of all future tournaments ")
    void testNumberOfAllFutureTournaments() throws URISyntaxException {
        // given
        File file = readFile(CALTOUR_TEST_FILE_Y25);

        var expectedNumberOfTournaments25 = 14;

        // when
        sut = plannedTournamentParser.parseTournamentCalendar(file);
        var nofTournaments25 = sut.getAllRemainingTournaments(LocalDate.of(2025, 8, 30));

        // then
        assertAll("Grouped assertions for planned tournaments",
                () -> assertThat(nofTournaments25).isNotNull(),
                () -> assertThat(nofTournaments25.size()).isEqualTo(expectedNumberOfTournaments25)
        );
    }

    @Test
    @DisplayName("Read all tournaments if the second file is empty")
    void testAllTournamentsWithOneEmptyFile() throws URISyntaxException {
        // given
        File year25TournamentsFile = readFile(CALTOUR_TEST_FILE_Y25);
        File year26TournamentsFile = readFile(CALTOUR_TEST_FILE_EMPTY);

        var expectedNumberOfTournaments = 50;
        var expectedNofThisYearTournaments = 50;
        var expectedNofNextYearTournaments = 0;

        // when
        sut = plannedTournamentParser.parseTournamentCalendar(year25TournamentsFile);
        sut.setNextYearsTournaments(plannedTournamentParser.parseTournamentCalendar(year26TournamentsFile).getAllPlannedTournaments());

        var thisYearsTournaments = sut.getThisYearsTournaments();
        var nextYearsTournaments = sut.getNextYearsTournaments();
        var allTournaments = sut.getAllPlannedTournaments();

        // then
        assertAll("Grouped assertions for testing special case",
                () -> assertThat(allTournaments).isNotNull(),
                () -> assertThat(allTournaments.size()).isEqualTo(expectedNumberOfTournaments),
                () -> assertThat(thisYearsTournaments.size()).isEqualTo(expectedNofThisYearTournaments),
                () -> assertThat(nextYearsTournaments.size()).isEqualTo(expectedNofNextYearTournaments)
        );
    }

    @Test
    @DisplayName("Test default constructor preparing empty lists")
    void test() {
        // given
        var expectedNumberOfTournaments = 0;
        var expectedNofThisYearTournaments = 0;
        var expectedNofNextYearTournaments = 0;

        // when
        sut = new PlannedTournaments();

        var thisYearsTournaments = sut.getThisYearsTournaments();
        var nextYearsTournaments = sut.getNextYearsTournaments();
        var allTournaments = sut.getAllPlannedTournaments();

        // then
        assertAll("Grouped assertions for testing special case",
                () -> assertThat(allTournaments.size()).isEqualTo(expectedNumberOfTournaments),
                () -> assertThat(thisYearsTournaments.size()).isEqualTo(expectedNofThisYearTournaments),
                () -> assertThat(nextYearsTournaments.size()).isEqualTo(expectedNofNextYearTournaments)
        );
    }
}
