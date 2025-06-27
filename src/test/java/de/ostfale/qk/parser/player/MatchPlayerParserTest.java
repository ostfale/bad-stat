package de.ostfale.qk.parser.player;

import de.ostfale.qk.domain.match.DisciplineMatch;
import de.ostfale.qk.parser.BaseParserTest;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Tag("unit")
@DisplayName("Test parsing of players in a match")
class MatchPlayerParserTest extends BaseParserTest {

    private MatchPlayerParser sut;

    private DisciplineMatch disciplineMatch;

    @BeforeEach
    void setUp() {
        sut = new MatchPlayerParser();
        disciplineMatch = new DisciplineMatch();
    }

    @Test
    @DisplayName("Test retrieving players from a standard single match")
    void testExtractingSinglePlayersStandardMatch() {
        // given
        String testFileName = "matches/SingleMatchStandard.html";
        HtmlPage page = loadHtmlPage(testFileName);

        var expectedPlayerOneName = "Victoria Braun";
        var expectedPlayerTwoName = "Elina Uhlmann";

        // when
        sut.parseMatchPlayer(disciplineMatch, page.getActiveElement());

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
        sut.parseMatchPlayer(disciplineMatch, page.getActiveElement());

        // then
        assertAll("Test extraction of player name",
                () -> assertThat(disciplineMatch.getPlayerOneName()).isEqualTo(expectedPlayerOneName),
                () -> assertThat(disciplineMatch.getPlayerTwoName()).isEqualTo(expectedPlayerTwoName)
        );
    }
}
