package de.ostfale.qk.parser.web.tournament;

import de.ostfale.qk.parser.BaseParserTest;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Tag("unittest")
@DisplayName("Test parsing of a single standard tournament with all disciplines")
class WebTournamentParserServiceSingleTournamentTest extends BaseParserTest {

    private static final String TEST_FILE_NAME = "tournaments/SingleTournamentARL25_VB.html";

    private WebTournamentParserService sut;

    @BeforeEach
    void setUp() {
        sut = prepareWebTournamentParser();
    }

    @Test
    @DisplayName("Test parsing all tournaments for a year")
    void testParseAllTournaments() {
        // given
        var playerName = "Victoria Braun";
        HtmlPage page = loadHtmlPage(TEST_FILE_NAME);
        var expectedNumberOfTournaments = 1;

        // when
        var result = sut.parseAllYearlyTournamentsForPlayer(playerName, page);

        // then
        assertThat(result.size()).isEqualTo(expectedNumberOfTournaments);
    }

    @Test
    @DisplayName("Test tournament header information details")
    void testTournamentHeaderInfo() {
        // given
        var playerName = "Victoria Braun";
        HtmlPage page = loadHtmlPage(TEST_FILE_NAME);
        var expectedTournamentName = "5. DBV A-RLT U17 und U19 Kleinblittersdorf 2025";
        var expectedTournamentDate = "28.06.2025 bis 29.06.2025";
        var expectedTournamentLocation = "Kleinblittersdorf";
        var expectedTournamentOrganisation = "Deutscher Badminton Verband (U19)";

        // when
        var result = sut.parseAllYearlyTournamentsForPlayer(playerName, page);

        // then
        assertAll("Test tournament header data",
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getFirst().getTournamentInfo().tournamentName()).isEqualTo(expectedTournamentName),
                () -> assertThat(result.getFirst().getTournamentInfo().tournamentDate()).isEqualTo(expectedTournamentDate),
                () -> assertThat(result.getFirst().getTournamentInfo().tournamentLocation()).isEqualTo(expectedTournamentLocation),
                () -> assertThat(result.getFirst().getTournamentInfo().tournamentOrganizer()).isEqualTo(expectedTournamentOrganisation)
        );
    }

    @Test
    @DisplayName("Test single discipline matches")
    void testSingleMatchStandard() {
        // given
        var playerName = "Victoria Braun";
        HtmlPage page = loadHtmlPage(TEST_FILE_NAME);
        var expectedNumberOfEliminationMatches = 3;
        var expectedNumberOfGroupMatches = 0;
        var expectedFirstMatchRoundName = "Round of 32";
        var playerOneName = "Victoria Braun (W)";

        // when
        var result = sut.parseAllYearlyTournamentsForPlayer(playerName, page);
        var tournament = result.getFirst();
        var singleDiscipline = tournament.getSingleDiscipline();
        var firstMatch = singleDiscipline.getEliminationMatches().getFirst();
        var firstMatchSets = 0;

        // then
        assertAll("Test all data for single matches",
                () -> assertThat(singleDiscipline).isNotNull(),
                () -> assertThat(singleDiscipline.getEliminationMatches().size()).isEqualTo(expectedNumberOfEliminationMatches),
                () -> assertThat(singleDiscipline.getGroupMatches().size()).isEqualTo(expectedNumberOfGroupMatches),
                () -> assertThat(firstMatch.getRoundName()).isEqualTo(expectedFirstMatchRoundName),
                () -> assertThat(firstMatch.getFirstPlayerOrWithPartnerName()).isEqualTo(playerOneName),
                () -> assertThat(firstMatch.getMatchSets().size()).isEqualTo(firstMatchSets)
        );
    }
}
