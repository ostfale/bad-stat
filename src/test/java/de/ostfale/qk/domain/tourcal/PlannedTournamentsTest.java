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

    private final PlannedTournamentParser plannedTournamentParser = new PlannedTournamentParser();
    private static final String TEST_FILE_NAME = "caltour/Mixed_Tournaments_25_26.csv";

    private PlannedTournaments sut;

    @Test
    @DisplayName("Get number of all tournaments ")
    void testNumberOfAllTournaments() throws URISyntaxException {
        // given
        File file = readFile(TEST_FILE_NAME);
        var expectedNumberOfTournaments = 82;
        var expectedNofThisYearTournaments = 63;
        var expectedNofNextYearTournaments = 19;

        // when
        sut = plannedTournamentParser.parseTournamentCalendar(file);
        var nofTournaments = sut.getNumberOfAllPlannedTournaments();
        var thisYearsTournaments = sut.getAllTournamentsForThisYear();
        var nextYearsTournaments = sut.getAllTournamentsForNextYear();

        // then
        assertAll("Grouped assertions for planned tournaments",
                () -> assertThat(nofTournaments).isNotNull(),
                () -> assertThat(nofTournaments).isEqualTo(expectedNumberOfTournaments),
                () -> assertThat(thisYearsTournaments.size()).isEqualTo(expectedNofThisYearTournaments),
                () -> assertThat(nextYearsTournaments.size()).isEqualTo(expectedNofNextYearTournaments)
        );
    }

    @Test
    @DisplayName("Get number of all future tournaments ")
    void testNumberOfAllFutureTournaments() throws URISyntaxException {
        // given
        File file = readFile(TEST_FILE_NAME);
        var expectedNumberOfTournaments25 = 46;
        var expectedNumberOfTournaments26 = 19;

        // when
        sut = plannedTournamentParser.parseTournamentCalendar(file);
        var nofTournaments25 = sut.getAllNotYetFinishedTournaments(LocalDate.of(2025, 8, 30));
        var nofTournaments26 = sut.getAllNotYetFinishedTournaments(LocalDate.of(2025, 12, 31));

        // then
        assertAll("Grouped assertions for planned tournaments",
                () -> assertThat(nofTournaments25).isNotNull(),
                () -> assertThat(nofTournaments25.size()).isEqualTo(expectedNumberOfTournaments25),
                () -> assertThat(nofTournaments26.size()).isEqualTo(expectedNumberOfTournaments26)
        );
    }
}
