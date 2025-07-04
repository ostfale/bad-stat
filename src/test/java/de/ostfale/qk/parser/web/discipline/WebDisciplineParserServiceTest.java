package de.ostfale.qk.parser.web.discipline;

import de.ostfale.qk.parser.BaseParserTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;

@Tag("unittest")
@DisplayName("Test parsing of disciplines from web page")
class WebDisciplineParserServiceTest extends BaseParserTest {

    private WebDisciplineParserService sut;

  /*  @BeforeEach
    void setUp() {
        WebDisciplineInfoParserService webDisciplineInfoParserService = new WebDisciplineInfoParserService();
        MatchSetParserService matchSetParserService = new MatchSetParserService();
        MatchPlayerParserService matchPlayerParserService = new MatchPlayerParserService();
        WebMatchParser webMatchParser = new WebMatchParserService(matchSetParserService, matchPlayerParserService);
        sut = new WebDisciplineParserService(webDisciplineInfoParserService, webMatchParser);
    }

    @Test
    @DisplayName("Test complete tournament with only elimination matches")
    void testEliminationTournamentMatchesOnly() {
        // given
        String testFileName = "tournaments/TournamentEliminationVB25.html";
        HtmlPage page = loadHtmlPage(testFileName);
        Tournament tournament = new Tournament(new TournamentInfo());

        // when
        sut.parseDisciplines(tournament, page.getActiveElement());

        // then
        assertAll("Verify extraction of a complete tournament",
                () -> assertThat(tournament.getSingleDiscipline()).isNotNull()
        );
    }*/
}
