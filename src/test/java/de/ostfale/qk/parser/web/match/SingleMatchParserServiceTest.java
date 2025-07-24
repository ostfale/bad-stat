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

    private static final String SINGLE_MATCH_TEST = "matches/SingleMatchTest.html";
    private static final String SINGLE_MATCH_STANDARD_FIRST_WINS = "matches/SingleMatchStandardFirstPlayerWins.html";
    private static final String SINGLE_MATCH_STANDARD_SECOND_WINS = "matches/SingleMatchStandardSecondPlayerWins.html";
    private static final String SINGLE_MATCH_RAST = "matches/SingleMatchRast.html";
    private static final String SINGLE_MATCH_SECOND_PLAYER_RAST = "matches/SingleMatchSecondPlayerHasRast.html";
    private static final String SINGLE_MATCH_SECOND_PLAYER_WINS_BOY = "matches/SingleMatchBoysSecondPlayerWins.html";
    private static final String SINGLE_MATCH_KEIN_SPIEL = "matches/SingleMatchKeinSpiel.html";
    private static final String SINGLE_MATCH_WALKOVER_L = "matches/SingleWalkoverLost.html";

    private MatchParser sut;

    @BeforeEach
    void setUp() {
        sut = new MatchParserService(new SetParserService());
    }

    @Test
    @DisplayName("Test a single standard match -> first player wins")
    // ["Victoria Braun","W","Elina Uhlmann","21","10","21","8","H2H"]
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
    // ["Victoria Braun","L","Jule Jensen","13","21","21","10","18","21","H2H"]
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
    // ["Nils Barion","Frederik Volkert","W","12","21","10","21","H2H"]
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
    // ["Victoria Braun","W","Rast"]
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
    // ["Rast","Frederik Volkert","W"]
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

    @Test
    @DisplayName("Test a single match with 'kein Spiel' ")
    // ["Rast","Kein Spiel","Max Hahn","W"]
    void testSingleMatchWithKeinSpiel() throws HtmlParserException {
        // given
        HtmlPage page = loadHtmlPage(SINGLE_MATCH_KEIN_SPIEL);
        var expectedRoundName = "Round of 16";
        var expectedPlayerOneName = "Rast";
        var expectedPlayerTwoName = "Max Hahn (W)";

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
    @DisplayName("Test a single match with special name with 'von' ")
    // ["Titus von Hartrott","Max Hahn","L","21","14","21","16","H2H"]
    void testSingleMatchWithSpecialName() throws HtmlParserException {
        // given
        HtmlPage page = loadHtmlPage(SINGLE_MATCH_TEST);
        var expectedRoundName = "Round of 32";
        var expectedPlayerOneName = "Titus von Hartrott";
        var expectedPlayerTwoName = "Max Hahn (L)";

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
    @DisplayName("Test a single match with with 'Walkover L' combination ")
        // ["Soheyl Safari Araghi","Walkover L","Shager Thangjam","H2H"]
    void testSingleMatchWithWalkoverLost() throws HtmlParserException {
        // given
        HtmlPage page = loadHtmlPage(SINGLE_MATCH_WALKOVER_L);
        var expectedRoundName = "Round of 16";
        var expectedPlayerOneName = "Soheyl Safari Araghi (L)";
        var expectedPlayerTwoName = "Shager Thangjam";
        var expectedSetResult = "Walkover";

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
