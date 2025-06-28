package de.ostfale.qk.parser.web.discipline;

import de.ostfale.qk.domain.tournament.Tournament;
import de.ostfale.qk.domain.tournament.TournamentInfo;
import de.ostfale.qk.parser.BaseParserTest;
import de.ostfale.qk.parser.match.api.WebMatchParser;
import de.ostfale.qk.parser.match.internal.WebMatchParserService;
import de.ostfale.qk.parser.player.MatchPlayerParser;
import de.ostfale.qk.parser.set.MatchSetParser;
import de.ostfale.qk.parser.web.HtmlStructureParser;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Tag("unittest")
@DisplayName("Test parsing of disciplines from web page")
class WebDisciplineParserTest extends BaseParserTest {

    private WebDisciplineParser sut;

    @BeforeEach
    void setUp() {
        HtmlStructureParser htmlStructureParser = new HtmlStructureParser();
        WebDisciplineInfoParser webDisciplineInfoParser = new WebDisciplineInfoParser();
        MatchSetParser matchSetParser = new MatchSetParser(htmlStructureParser);
        MatchPlayerParser matchPlayerParser = new MatchPlayerParser();
        WebMatchParser webMatchParser = new WebMatchParserService(htmlStructureParser, matchSetParser, matchPlayerParser);
        sut = new WebDisciplineParser(htmlStructureParser, webDisciplineInfoParser, webMatchParser);
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
    }
}
