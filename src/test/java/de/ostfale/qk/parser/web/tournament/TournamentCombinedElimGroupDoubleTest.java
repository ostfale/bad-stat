package de.ostfale.qk.parser.web.tournament;

import de.ostfale.qk.parser.BaseParserTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Tag("unittest")
@DisplayName("Test tournament double with elimination and group phase")
public class TournamentCombinedElimGroupDoubleTest extends BaseParserTest {

    private static final String TEST_FILE_NAME = "tournaments/CombinedElimGroupDoubleVictorHahn.html";

    private WebTournamentParserService sut;

    @BeforeEach
    void setUp() {
        sut = prepareWebTournamentParser();
        content = loadHtmlPage(TEST_FILE_NAME);
    }

    @Test
    @DisplayName("Test parsing a single tournament")
    void testParseAllTournaments() {
        // given
        var playerName = "Max Hahn";
        var expectedNumberOfTournaments = 1;

        // when
        var result = sut.parseAllYearlyTournamentsForPlayer(playerName, content);

        // then
        assertThat(result.size()).isEqualTo(expectedNumberOfTournaments);
    }
}
