package de.ostfale.qk.parser.player;

import de.ostfale.qk.domain.match.DisciplineMatch;
import de.ostfale.qk.parser.BaseParserTest;
import de.ostfale.qk.parser.web.player.MatchPlayerParserService;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Tag("unittest")
@DisplayName("Test parsing of players in a match")
class MatchPlayerParserServiceTest extends BaseParserTest {

    private MatchPlayerParserService sut;

    private DisciplineMatch disciplineMatch;

    @BeforeEach
    void setUp() {
        sut = new MatchPlayerParserService();
        disciplineMatch = new DisciplineMatch();
    }

    @Test
    @DisplayName("Test retrieving players from a standard single match")
    void testExtractingSinglePlayersStandardMatch() {
        // given
        String testFileName = "matches/SingleMatchStandardFirstPlayerWins.html";
        HtmlPage page = loadHtmlPage(testFileName);

        var expectedPlayerOneName = "Victoria Braun";
        var expectedPlayerTwoName = "Elina Uhlmann";

        // when
        sut.parseMatchPlayers(disciplineMatch, page.getActiveElement());

        // then
        assertAll("Test extraction of player name",
                () -> assertThat(disciplineMatch.getPlayerOneName()).isEqualTo(expectedPlayerOneName),
                () -> assertThat(disciplineMatch.getPlayerTwoName()).isEqualTo(expectedPlayerTwoName),
                () -> assertThat(disciplineMatch.getFirstPlayerOrWithPartnerName()).isEqualTo(expectedPlayerOneName),
                () -> assertThat(disciplineMatch.getSecondPlayerOrWithPartnerName()).isEqualTo(expectedPlayerTwoName)
        );
    }

    @Test
    @DisplayName("Test retrieving players from a single match with Rast")
    void testExtractingSinglePlayersRastMatch() {
        // given
        String testFileName = "matches/SingleMatchRast.html";
        HtmlPage page = loadHtmlPage(testFileName);

        var expectedPlayerOneName = "Victoria Braun";
        var expectedPlayerTwoName = "Rast";

        // when
        sut.parseMatchPlayers(disciplineMatch, page.getActiveElement());

        // then
        assertAll("Test extraction of player name",
                () -> assertThat(disciplineMatch.getPlayerOneName()).isEqualTo(expectedPlayerOneName),
                () -> assertThat(disciplineMatch.getPlayerTwoName()).isEqualTo(expectedPlayerTwoName)
        );
    }


    @Test
    @DisplayName("Test retrieving players from a standard double match")
    void testExtractingDoublePlayersStandardMatch() {
        // given
        String testFileName = "matches/DoubleMatchStandardSecondTeamWins.html";
        HtmlPage page = loadHtmlPage(testFileName);

        var expectedPlayerOneName = "Yara Metzlaff";
        var expectedPartnerOneName = "Wilhelmine Witthus";
        var expectedPlayerTwoName = "Emily Bischoff";
        var expectedPartnerTwoName = "Victoria Braun";
        var expectedTeamOneName = "Yara Metzlaff / Wilhelmine Witthus";
        var expectedTeamTwoName = "Emily Bischoff / Victoria Braun";

        // when
        sut.parseMatchPlayers(disciplineMatch, page.getActiveElement());

        // then
        assertAll("Test extraction of player name",
                () -> assertThat(disciplineMatch.getPlayerOneName()).isEqualTo(expectedPlayerOneName),
                () -> assertThat(disciplineMatch.getPartnerOneName()).isEqualTo(expectedPartnerOneName),
                () -> assertThat(disciplineMatch.getPlayerTwoName()).isEqualTo(expectedPlayerTwoName),
                () -> assertThat(disciplineMatch.getPartnerTwoName()).isEqualTo(expectedPartnerTwoName),
                () -> assertThat(disciplineMatch.getFirstPlayerOrWithPartnerName()).isEqualTo(expectedTeamOneName),
                () -> assertThat(disciplineMatch.getSecondPlayerOrWithPartnerName()).isEqualTo(expectedTeamTwoName)
        );
    }

    @Test
    @DisplayName("Test retrieving players from a double match with Rast")
    void testExtractingDoublePlayersRastMatch() {
        // given
        String testFileName = "matches/DoubleMatchRast.html";
        HtmlPage page = loadHtmlPage(testFileName);

        var expectedPlayerOneName = "Rast";
        var expectedPlayerTwoName = "Emily Bischoff";
        var expectedPartnerTwoName = "Victoria Braun";
        var expectedTeamOneName = "Rast";
        var expectedTeamTwoName = "Emily Bischoff / Victoria Braun";

        // when
        sut.parseMatchPlayers(disciplineMatch, page.getActiveElement());

        // then
        assertAll("Test extraction of player name",
                () -> assertThat(disciplineMatch.getPlayerOneName()).isEqualTo(expectedPlayerOneName),
                () -> assertThat(disciplineMatch.getPartnerOneName()).isNull(),
                () -> assertThat(disciplineMatch.getPlayerTwoName()).isEqualTo(expectedPlayerTwoName),
                () -> assertThat(disciplineMatch.getPartnerTwoName()).isEqualTo(expectedPartnerTwoName),
                () -> assertThat(disciplineMatch.getFirstPlayerOrWithPartnerName()).isEqualTo(expectedTeamOneName),
                () -> assertThat(disciplineMatch.getSecondPlayerOrWithPartnerName()).isEqualTo(expectedTeamTwoName)
        );
    }


}
