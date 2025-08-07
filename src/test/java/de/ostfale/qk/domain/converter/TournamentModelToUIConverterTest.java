package de.ostfale.qk.domain.converter;

import de.ostfale.qk.domain.tournament.Tournament;
import de.ostfale.qk.parser.BaseParserTest;
import de.ostfale.qk.parser.web.tournament.WebTournamentParserService;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Test TournamentModelToUIConverter for a combined match")
class TournamentModelToUIConverterTest extends BaseParserTest {

    private static final String TEST_FILE_NAME = "tournaments/TournamentSingleWithGroup.html";

    TournamentModelToUIConverter sut;
    WebTournamentParserService parser = prepareWebTournamentParser();

    private HtmlPage page;

    @BeforeEach
    void setUp() {
        page = loadHtmlPage(TEST_FILE_NAME);
        sut = new TournamentModelToUIConverter();
    }

    @Test
    @DisplayName("Test converting a combined tournament into UI display format")
    void testConvertingCombinedTournament() {
        // given
        var playerName = "Victoria Braun";
        Tournament tournament = parser.parseAllYearlyTournamentsForPlayer(playerName, page).getFirst();

        var expectedTournamentName = "VICTOR International Junior Cup (HAM, U09-U19, U22) 2025";
        var expectedTournamentLocation = "Hamburg";
        var expectedTournamentDate = "07.06.2025 bis 09.06.2025";

        var expectedNofMatches = 6;
        var expectedFirstMatchDisciplineName = "Mixed (U17)";
        var expectedLastMatchDisciplineName = "Mixed (Gruppe B)";

        // when
        var result = sut.convertTo(tournament);
        var firstMatch = result.getMatchDetails().getFirst();
        var lastMatch = result.getMatchDetails().getLast();

        // then
        assertAll("Verify tournament mapping data",
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getTournamentName()).isEqualTo(expectedTournamentName),
                () -> assertThat(result.getTournamentLocation()).isEqualTo(expectedTournamentLocation),
                () -> assertThat(result.getTournamentDate()).isEqualTo(expectedTournamentDate),
                () -> assertThat(result.getMatchDetails().size()).isEqualTo(expectedNofMatches),
                () -> assertThat(firstMatch.getDisciplineName()).isEqualTo(expectedFirstMatchDisciplineName),
                () -> assertThat(lastMatch.getDisciplineName()).isEqualTo(expectedLastMatchDisciplineName)
        );
    }
}
