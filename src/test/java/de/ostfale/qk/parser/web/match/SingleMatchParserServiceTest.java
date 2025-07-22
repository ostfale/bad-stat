package de.ostfale.qk.parser.web.match;

import de.ostfale.qk.parser.BaseParserTest;
import de.ostfale.qk.parser.HtmlParserException;
import de.ostfale.qk.parser.web.set.SetParserService;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static de.ostfale.qk.domain.discipline.DisciplineType.SINGLE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Tag("unittest")
@DisplayName("Test parsing all types of single matches")
public class SingleMatchParserServiceTest extends BaseParserTest {

    private static final String SINGLE_MATCH_STANDARD_FIRST_WINS = "matches/SingleMatchStandardFirstPlayerWins.html";
    private static final String SINGLE_MATCH_STANDARD_SECOND_WINS = "matches/SingleMatchStandardSecondPlayerWins.html";
    private static final String SINGLE_MATCH_RAST = "matches/SingleMatchRast.html";
    private static final String SINGLE_MATCH_SECOND_PLAYER_RAST = "matches/SingleMatchSecondPlayerHasRast.html";
    private static final String SINGLE_MATCH_SECOND_PLAYER_WINS_BOY = "matches/SingleMatchBoysSecondPlayerWins.html";

    private MatchParser sut;

    @BeforeEach
    void setUp() {
        sut = new MatchParserService(new SetParserService());
    }

    @Test
    @DisplayName("Test a single standard match -> first player wins")
    void testSingleStandardMatchFirstWins() throws HtmlParserException {
        // given
        HtmlPage page = loadHtmlPage(SINGLE_MATCH_STANDARD_FIRST_WINS);
        var expectedRoundName = "Round of 16";
        var expectedPlayerOneName = "Victoria Braun (W)";
        var expectedPlayerTwoName = "Elina Uhlmann";
        var expectedNumberOfSets = 2;

        // when
        var result = sut.parseMatch(SINGLE, page.getActiveElement());

        // then
        assertAll("Test all standard match data",
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getRoundName()).isEqualTo(expectedRoundName),
                () -> assertThat(result.getPlayerOneName()).isEqualTo(expectedPlayerOneName),
                () -> assertThat(result.getPlayerTwoName()).isEqualTo(expectedPlayerTwoName),
                () -> assertThat(result.getMatchSets().size()).isEqualTo(expectedNumberOfSets)
        );
    }

    @Test
    @DisplayName("Test a single standard match -> second player wins")
    void testSingleStandardMatchSecondWins() throws HtmlParserException {
        // given
        HtmlPage page = loadHtmlPage(SINGLE_MATCH_STANDARD_SECOND_WINS);
        var expectedRoundName = "Quarter final";
        var expectedPlayerOneName = "Victoria Braun (L)";
        var expectedPlayerTwoName = "Jule Jensen";
        var expectedNumberOfSets = 3;

        // when
        var result = sut.parseMatch(SINGLE, page.getActiveElement());

        // then
        assertAll("Test all standard match data",
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getRoundName()).isEqualTo(expectedRoundName),
                () -> assertThat(result.getPlayerOneName()).isEqualTo(expectedPlayerOneName),
                () -> assertThat(result.getPlayerTwoName()).isEqualTo(expectedPlayerTwoName),
                () -> assertThat(result.getMatchSets().size()).isEqualTo(expectedNumberOfSets)
        );
    }

    @Test
    @DisplayName("Test a single standard boys match -> second player wins")
    void testSingleStandardMatchBoysSecondWins() throws HtmlParserException {
        // given
        HtmlPage page = loadHtmlPage(SINGLE_MATCH_SECOND_PLAYER_WINS_BOY);
        var expectedRoundName = "Round of 16";
        var expectedPlayerOneName = "Nils Barion";
        var expectedPlayerTwoName = "Frederik Volkert (W)";
        var expectedNumberOfSets = 2;

        // when
        var result = sut.parseMatch(SINGLE, page.getActiveElement());

        // then
        assertAll("Test all standard match data",
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getRoundName()).isEqualTo(expectedRoundName),
                () -> assertThat(result.getPlayerOneName()).isEqualTo(expectedPlayerOneName),
                () -> assertThat(result.getPlayerTwoName()).isEqualTo(expectedPlayerTwoName),
                () -> assertThat(result.getMatchSets().size()).isEqualTo(expectedNumberOfSets)
        );
    }

    @Test
    @DisplayName("Test a single match with Rast status")
    void testSingleMatchRast() throws HtmlParserException {
        // given
        HtmlPage page = loadHtmlPage(SINGLE_MATCH_RAST);
        var expectedRoundName = "Round of 32";
        var expectedPlayerOneName = "Victoria Braun (W)";
        var expectedPlayerTwoName = "Rast";

        // when
        var result = sut.parseMatch(SINGLE, page.getActiveElement());

        // then
        assertAll("Test single match rast data",
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getRoundName()).isEqualTo(expectedRoundName),
                () -> assertThat(result.getFirstPlayerOrWithPartnerName()).isEqualTo(expectedPlayerOneName),
                () -> assertThat(result.getSecondPlayerOrWithPartnerName()).isEqualTo(expectedPlayerTwoName)
        );
    }

    @Test
    @DisplayName("Test a single match with Rast for the second player")
    void testSingleMatchRastSecondPlayer() throws HtmlParserException {
        // given
        HtmlPage page = loadHtmlPage(SINGLE_MATCH_SECOND_PLAYER_RAST);
        var expectedRoundName = "Round of 32";
        var expectedPlayerOneName = "Rast";
        var expectedPlayerTwoName = "Frederik Volkert (W)";

        // when
        var result = sut.parseMatch(SINGLE, page.getActiveElement());

        // then
        assertAll("Test single match rast data",
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getRoundName()).isEqualTo(expectedRoundName),
                () -> assertThat(result.getFirstPlayerOrWithPartnerName()).isEqualTo(expectedPlayerOneName),
                () -> assertThat(result.getSecondPlayerOrWithPartnerName()).isEqualTo(expectedPlayerTwoName)
        );
    }
}
