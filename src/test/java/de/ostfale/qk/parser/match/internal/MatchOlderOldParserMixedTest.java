package de.ostfale.qk.parser.match.internal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.htmlunit.html.HtmlDivision;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import de.ostfale.qk.parser.BaseParserTest;
import de.ostfale.qk.parser.match.internal.model.MixedMatchRawModel;

@DisplayName("Test reading the mixed match information ")
@Tag("unittest")
public class MatchOlderOldParserMixedTest extends BaseParserTest {

    MatchParserService parser = new MatchParserService();

    @Test
    @DisplayName("Parse mixed match information")
    void parseMixedMatch() {
        // given
        String testFileName = "matches/MixedMatch.txt";
        HtmlPage page = loadHtmlPage(testFileName);
        HtmlDivision content = (HtmlDivision) page.getActiveElement().getFirstChild();

        // when
        MixedMatchRawModel result = parser.parseMixedMatch(content);

        // then
        assertAll("Test mixed match information",
                () -> assertEquals("Louis Sauerbrei", result.getFirstMixedPlayerOne().name),
                () -> assertEquals("Victoria Braun", result.getFirstMixedPlayerTwo().name),
                () -> assertEquals("Malthe Heilesen", result.getSecondMixedPlayerOne().name),
                () -> assertEquals("Madeleine Dam", result.getSecondMixedPlayerTwo().name),
                () -> assertFalse(result.hasFirstPlayerWon()),
                () -> assertEquals(19, result.getPlayersSets().getFirst().getFirstValue()),
                () -> assertEquals(21, result.getPlayersSets().getFirst().getSecondValue()),
                () -> assertEquals(17, result.getPlayersSets().get(1).getFirstValue()),
                () -> assertEquals(21, result.getPlayersSets().get(1).getSecondValue())
        );
    }
}
