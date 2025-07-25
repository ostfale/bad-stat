package de.ostfale.qk.parser.web.tournament;

import de.ostfale.qk.parser.BaseParserTest;
import de.ostfale.qk.parser.HtmlParserException;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

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
    void testParseAllTournaments() throws HtmlParserException {
        // given
        var expectedNumberOfTournaments = 1;

        // when
        var result = sut.parseAllYearlyTournamentsForPlayer(content);

        // then
        AssertionsForClassTypes.assertThat(result.size()).isEqualTo(expectedNumberOfTournaments);
    }
}
