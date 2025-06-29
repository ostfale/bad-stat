package de.ostfale.qk.parser.match.internal;

import de.ostfale.qk.parser.BaseParserTest;
import de.ostfale.qk.parser.set.MatchSetParserService;
import de.ostfale.qk.parser.web.player.MatchPlayerParserService;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Tag("unittest")
@DisplayName("Test parsing a complete match")
class WebMatchParserServiceTest extends BaseParserTest {

    private WebMatchParserService sut;

    @BeforeEach
    void setUp() {
        MatchSetParserService matchSetParserService = new MatchSetParserService();
        MatchPlayerParserService matchPlayerParserService = new MatchPlayerParserService();
        sut = new WebMatchParserService(matchSetParserService, matchPlayerParserService);
    }

    @Test
    @DisplayName("Test parsing a complete Single standard match")
    void testSingleMatchStandard() {
        // given
        String testFileName = "matches/SingleMatchStandard.html";
        HtmlPage page = loadHtmlPage(testFileName);

        var expectedPlayerOneName = "Victoria Braun";
        var expectedPlayerTwoName = "Elina Uhlmann";
        var expectedRoundName = "Round of 16";
        var expectedFirstSetFirstValue = 21;
        var expectedFirstSetSecondValue = 10;
        var expectedSecondSetFirstValue = 21;
        var expectedSecondSetSecondValue = 8;

        // when
        var result = sut.parseSingleMatch(page.getActiveElement());

        // then
        assertAll("Test extraction of a standard single match",
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getPlayerOneName()).isEqualTo(expectedPlayerOneName),
                () -> assertThat(result.getPlayerTwoName()).isEqualTo(expectedPlayerTwoName),
                () -> assertThat(result.getRoundName()).isEqualTo(expectedRoundName),
                () -> assertThat(result.getMatchSets().getFirst().isRegularSet()).isTrue(),
                () -> assertThat(result.getMatchSets().getFirst().getFirstValue()).isEqualTo(expectedFirstSetFirstValue),
                () -> assertThat(result.getMatchSets().getFirst().getSecondValue()).isEqualTo(expectedFirstSetSecondValue),
                () -> assertThat(result.getMatchSets().getLast().getFirstValue()).isEqualTo(expectedSecondSetFirstValue),
                () -> assertThat(result.getMatchSets().getLast().getSecondValue()).isEqualTo(expectedSecondSetSecondValue)
        );
    }
}
