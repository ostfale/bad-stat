package de.ostfale.qk.parser.file.csv;

import de.ostfale.qk.parser.BaseParserTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

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
    void testParsingValidTournamentLine() {
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
                () -> assertThat(result.tournamentName()).isEqualTo("4. C-RLT THÜ U13/U17")
        );

    }

}
