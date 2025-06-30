package de.ostfale.qk.parser.web.tournament;

import de.ostfale.qk.domain.discipline.AgeClass;
import de.ostfale.qk.parser.BaseParserTest;
import de.ostfale.qk.parser.HtmlParserException;
import de.ostfale.qk.parser.match.api.WebMatchParser;
import de.ostfale.qk.parser.match.internal.WebMatchParserService;
import de.ostfale.qk.parser.set.MatchSetParserService;
import de.ostfale.qk.parser.web.discipline.WebDisciplineInfoParserService;
import de.ostfale.qk.parser.web.discipline.WebDisciplineParserService;
import de.ostfale.qk.parser.web.player.MatchPlayerParserService;
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
        WebTournamentInfoParser webTournamentInfoParser = new WebTournamentInfoParser();
        WebDisciplineInfoParserService webDisciplineInfoParserService = new WebDisciplineInfoParserService();
        MatchSetParserService matchSetParserService = new MatchSetParserService();
        WebMatchParser webMatchParser = new WebMatchParserService(matchSetParserService, new MatchPlayerParserService());
        WebDisciplineParserService webDisciplineParserService = new WebDisciplineParserService(webDisciplineInfoParserService, webMatchParser);
        sut = new WebTournamentParserService(webTournamentInfoParser, webDisciplineParserService);
    }

    @Test
    @DisplayName("Test parsing all tournaments for a year")
    void testParseAllTournaments() throws HtmlParserException {
        // given
        HtmlPage page = loadHtmlPage(TEST_FILE_NAME);
        var expectedNumberOfTournaments = 1;

        // when
        var result = sut.parseAllYearlyTournamentsForPlayer(page.getActiveElement());

        // then
        assertThat(result.size()).isEqualTo(expectedNumberOfTournaments);
    }

    @Test
    @DisplayName("Test tournament header information details")
    void testTournamentHeaderInfo() throws HtmlParserException {
        // given
        HtmlPage page = loadHtmlPage(TEST_FILE_NAME);
        var expectedTournamentName = "5. DBV A-RLT U17 und U19 Kleinblittersdorf 2025";
        var expectedTournamentDate = "28.06.2025 bis 29.06.2025";
        var expectedTournamentLocation = "Kleinblittersdorf";
        var expectedTournamentOrganisation = "Deutscher Badminton Verband (U19)";

        // when
        var result = sut.parseAllYearlyTournamentsForPlayer(page.getActiveElement());

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
    void testSingleMatchStandard() throws HtmlParserException {
        // given
        HtmlPage page = loadHtmlPage(TEST_FILE_NAME);
        var expectedNumberOfEliminationMatches = 3;
        var expectedNumberOfGroupMatches = 0;
        var expepectedDisciplineAgeClass = AgeClass.U17;
        var expectedFirstMatchRoundName = "Round of 32";
        var playerOneName = "Victoria Braun";

        // when
        var result = sut.parseAllYearlyTournamentsForPlayer(page.getActiveElement());
        var tournament = result.getFirst();
        var singleDiscipline = tournament.getSingleDiscipline();
        var firstMatch = singleDiscipline.getEliminationMatches().getFirst();
        var firstMatchSets = 1;

        // then
        assertAll("Test all data for single matches",
                () -> assertThat(singleDiscipline).isNotNull(),
                () -> assertThat(singleDiscipline.getEliminationMatches().size()).isEqualTo(expectedNumberOfEliminationMatches),
                () -> assertThat(singleDiscipline.getGroupMatches().size()).isEqualTo(expectedNumberOfGroupMatches),
                () -> assertThat(singleDiscipline.getDisciplineAgeClass()).isEqualTo(expepectedDisciplineAgeClass),
                () -> assertThat(firstMatch.getRoundName()).isEqualTo(expectedFirstMatchRoundName),
                () -> assertThat(firstMatch.getFirstPlayerOrWithPartnerName()).isEqualTo(playerOneName),
                () -> assertThat(firstMatch.getMatchSets().size()).isEqualTo(firstMatchSets),
                () -> assertThat(firstMatch.getMatchSets().getFirst().isRegularSet()).isFalse()
        );
    }
}
