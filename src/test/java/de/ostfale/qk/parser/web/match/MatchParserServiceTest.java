package de.ostfale.qk.parser.web.match;

import de.ostfale.qk.parser.BaseParserTest;
import de.ostfale.qk.parser.HtmlParserException;
import de.ostfale.qk.parser.web.set.SetParserService;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Tag("unittest")
@DisplayName("Test parsing a different types of matches")
class MatchParserServiceTest extends BaseParserTest {

    private static final String SINGLE_MATCH_STANDARD_FIRST_WINS = "matches/SingleMatchStandardFirstPlayerWins.html";
    private static final String SINGLE_MATCH_STANDARD_SECOND_WINS = "matches/SingleMatchStandardSecondPlayerWins.html";
    private static final String DOUBLE_MATCH_STANDARD_FIRST_TEAM_WINS = "matches/DoubleMatchStandardFirstTeamWins.html";
    private static final String DOUBLE_MATCH_STANDARD_FIRST_TEAM_LOST = "matches/DoubleMatchStandardFirstTeamLost.html";
    private static final String DOUBLE_MATCH_STANDARD_SECOND_TEAM_WINS = "matches/DoubleMatchStandardSecondTeamWins.html";

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
        var result = sut.parseMatch(page.getActiveElement());

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
        var result = sut.parseMatch(page.getActiveElement());

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
    @DisplayName("Test a double standard match -> first team wins")
    void testDoubleStandardMatchFirstWins() throws HtmlParserException {
        // given
        HtmlPage page = loadHtmlPage(DOUBLE_MATCH_STANDARD_FIRST_TEAM_WINS);
        var expectedRoundName = "Round of 16";
        var expectedPlayerOneName = "Emily Bischoff";
        var expectedPartnerOneName = "Victoria Braun (W)";
        var expectedPlayerTwoName = "Leona Carolin Frimmel";
        var expectedPartnerTwoName = "Swara Santosh Krishna";
        var expectedNumberOfSets = 3;
        var expectedFirstTeamName = "Emily Bischoff / Victoria Braun (W)";
        var expectedSecondTeamName = "Leona Carolin Frimmel / Swara Santosh Krishna";

        // when
        var result = sut.parseMatch(page.getActiveElement());

        // then
        assertAll("Test all standard match data",
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getRoundName()).isEqualTo(expectedRoundName),
                () -> assertThat(result.getPlayerOneName()).isEqualTo(expectedPlayerOneName),
                () -> assertThat(result.getPartnerOneName()).isEqualTo(expectedPartnerOneName),
                () -> assertThat(result.getPlayerTwoName()).isEqualTo(expectedPlayerTwoName),
                () -> assertThat(result.getPartnerTwoName()).isEqualTo(expectedPartnerTwoName),
                () -> assertThat(result.getMatchSets().size()).isEqualTo(expectedNumberOfSets),
                () -> assertThat(result.getFirstPlayerOrWithPartnerName()).isEqualTo(expectedFirstTeamName),
                () -> assertThat(result.getSecondPlayerOrWithPartnerName()).isEqualTo(expectedSecondTeamName)
        );
    }

    @Test
    @DisplayName("Test a double standard match -> second team wins")
    void testDoubleStandardMatchSecondWins() throws HtmlParserException {
        // given
        HtmlPage page = loadHtmlPage(DOUBLE_MATCH_STANDARD_SECOND_TEAM_WINS);
        var expectedRoundName = "Quarter final";
        var expectedPlayerOneName = "Yara Metzlaff";
        var expectedPartnerOneName = "Wilhelmine Witthus";
        var expectedPlayerTwoName = "Emily Bischoff";
        var expectedPartnerTwoName = "Victoria Braun (W)";
        var expectedNumberOfSets = 3;
        var expectedFirstTeamName = "Yara Metzlaff / Wilhelmine Witthus";
        var expectedSecondTeamName = "Emily Bischoff / Victoria Braun (W)";

        // when
        var result = sut.parseMatch(page.getActiveElement());

        // then
        assertAll("Test all standard match data",
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getRoundName()).isEqualTo(expectedRoundName),
                () -> assertThat(result.getPlayerOneName()).isEqualTo(expectedPlayerOneName),
                () -> assertThat(result.getPartnerOneName()).isEqualTo(expectedPartnerOneName),
                () -> assertThat(result.getPlayerTwoName()).isEqualTo(expectedPlayerTwoName),
                () -> assertThat(result.getPartnerTwoName()).isEqualTo(expectedPartnerTwoName),
                () -> assertThat(result.getMatchSets().size()).isEqualTo(expectedNumberOfSets),
                () -> assertThat(result.getFirstPlayerOrWithPartnerName()).isEqualTo(expectedFirstTeamName),
                () -> assertThat(result.getSecondPlayerOrWithPartnerName()).isEqualTo(expectedSecondTeamName)
        );
    }

    @Test
    @DisplayName("Test a double standard match -> first team lost")
    void testDoubleStandardMatchFirstLost() throws HtmlParserException {
        // given
        HtmlPage page = loadHtmlPage(DOUBLE_MATCH_STANDARD_FIRST_TEAM_LOST);
        var expectedRoundName = "Semi final";
        var expectedPlayerOneName = "Emily Bischoff";
        var expectedPartnerOneName = "Victoria Braun (L)";
        var expectedPlayerTwoName = "Sidonie Krüger";
        var expectedPartnerTwoName = "Miya-Melayn Salaria";
        var expectedNumberOfSets = 2;
        var expectedFirstTeamName = "Emily Bischoff / Victoria Braun (L)";
        var expectedSecondTeamName = "Sidonie Krüger / Miya-Melayn Salaria";

        // when
        var result = sut.parseMatch(page.getActiveElement());

        // then
        assertAll("Test all standard match data",
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getRoundName()).isEqualTo(expectedRoundName),
                () -> assertThat(result.getPlayerOneName()).isEqualTo(expectedPlayerOneName),
                () -> assertThat(result.getPartnerOneName()).isEqualTo(expectedPartnerOneName),
                () -> assertThat(result.getPlayerTwoName()).isEqualTo(expectedPlayerTwoName),
                () -> assertThat(result.getPartnerTwoName()).isEqualTo(expectedPartnerTwoName),
                () -> assertThat(result.getMatchSets().size()).isEqualTo(expectedNumberOfSets),
                () -> assertThat(result.getFirstPlayerOrWithPartnerName()).isEqualTo(expectedFirstTeamName),
                () -> assertThat(result.getSecondPlayerOrWithPartnerName()).isEqualTo(expectedSecondTeamName)
        );
    }
}
