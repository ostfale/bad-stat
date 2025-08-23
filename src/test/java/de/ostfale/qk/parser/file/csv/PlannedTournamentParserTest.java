package de.ostfale.qk.parser.file.csv;

import de.ostfale.qk.domain.tourcal.TourCategory;
import de.ostfale.qk.domain.tourcal.TournamentType;
import de.ostfale.qk.parser.BaseParserTest;
import de.ostfale.qk.parser.file.FileParserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Tag("unittest")
@DisplayName("Test planned tournament parser")
class PlannedTournamentParserTest extends BaseParserTest {

    private PlannedTournamentParser sut;


    @BeforeEach
    void setUp() {
        sut = new PlannedTournamentParser();
    }

    @Test
    @DisplayName("Parse a valid line with regular tournament")
    void testParsingValidTournamentLine() throws FileParserException {
        // given
        var line = """
                15.11.2025;15.11.2025;"4. C-RLT THÜ U13/U17";Rangliste;4;DE;offen;;;;THÜ;C2-Level;05.11.2025;
                https://dbv.turnier.de/tournament/E5B74B79-E6FB-40D5-84DC-3FC67118402A;;
                nein;25.05.2024;;;01.07.2024;;;Einzel/Mixed;;Einzel/Mixed;;;;
                """.trim();

        // when
        var result = sut.parseRow(line);

        // then
        assertAll("Grouped assertions for planned tournament",
                () -> assertThat(result.startDate()).isEqualTo("15.11.2025"),
                () -> assertThat(result.endDate()).isEqualTo("15.11.2025"),
                () -> assertThat(result.tournamentName()).isEqualTo("4. C-RLT THÜ U13/U17"),
                () -> assertThat(result.tournamentType()).isEqualTo(TournamentType.Ranking),
                () -> assertThat(result.tournamentOrderNo()).isEqualTo(4),
                () -> assertThat(result.countryCode()).isEqualTo("DE"),
                () -> assertThat(result.location()).isEqualTo("offen"),
                () -> assertThat(result.location()).isEqualTo("offen"),
                () -> assertThat(result.postalCode()).isEqualTo(EMPTY_STRING),
                () -> assertThat(result.region()).isEqualTo(EMPTY_STRING),
                () -> assertThat(result.openName()).isEqualTo(EMPTY_STRING),
                () -> assertThat(result.organizer()).isEqualTo("THÜ"),
                () -> assertThat(result.tourCategory()).isEqualTo(TourCategory.C2),
                () -> assertThat(result.closingDate()).isEqualTo("05.11.2025"),
                () -> assertThat(result.webLinkUrl()).isEqualTo("https://dbv.turnier.de/tournament/E5B74B79-E6FB-40D5-84DC-3FC67118402A"),
                () -> assertThat(result.pdfLinkUrl()).isEqualTo(EMPTY_STRING),
                () -> assertThat(result.pdfAvailable()).isEqualTo("nein"),
                () -> assertThat(result.tourCreationDate()).isEqualTo("25.05.2024"),
                () -> assertThat(result.tourVisibleDate()).isEqualTo(EMPTY_STRING),
                () -> assertThat(result.invitationCreationDate()).isEqualTo(EMPTY_STRING),
                () -> assertThat(result.tourLinkCreationDate()).isEqualTo("01.07.2024"),
                () -> assertThat(result.AK_U09()).isEqualTo(EMPTY_STRING),
                () -> assertThat(result.AK_U11()).isEqualTo(EMPTY_STRING),
                () -> assertThat(result.AK_U13()).isEqualTo("Einzel/Mixed"),
                () -> assertThat(result.AK_U15()).isEqualTo(EMPTY_STRING),
                () -> assertThat(result.AK_U17()).isEqualTo("Einzel/Mixed"),
                () -> assertThat(result.AK_U19()).isEqualTo(EMPTY_STRING),
                () -> assertThat(result.AK_O19()).isEqualTo(EMPTY_STRING),
                () -> assertThat(result.AK_U22()).isEqualTo(EMPTY_STRING),
                () -> assertThat(result.AK_O35()).isEqualTo(EMPTY_STRING)
        );
    }

    @Test
    @DisplayName("Read a complete tournament file")
    void testReadingTournamentFile() throws URISyntaxException {
        // given
        var fileName = "caltour/Tournament_2025_2025-08-18.csv";
        File file = readFile(fileName);
        var expectedNumberOfTournaments = 610;

        // when
        var result = sut.parseTournamentCalendar(file);

        // then
        assertAll("Grouped assertions for tournament calendar",
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getAllPlannedTournaments().size()).isEqualTo(expectedNumberOfTournaments)

        );

    }
}
