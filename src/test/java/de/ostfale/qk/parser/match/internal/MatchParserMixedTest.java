package de.ostfale.qk.parser.match.internal;

import de.ostfale.qk.parser.BaseParserTest;
import de.ostfale.qk.parser.match.internal.model.MixedMatchDTO;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.htmlunit.html.HtmlDivision;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Test reading the mixed match information ")
@Tag("unittest")
@QuarkusTest
public class MatchParserMixedTest extends BaseParserTest {

    @Inject
    MatchParserService parser;

    @Test
    @DisplayName("Parse mixed match information")
    void parseMixedMatch() {
        // given
        String testFileName = "MixedMatch.txt";
        HtmlPage page = loadHtmlPage(testFileName);
        HtmlDivision content = (HtmlDivision) page.getActiveElement().getFirstChild();

        // when
        MixedMatchDTO result = parser.parseMixedMatch(content);

        // then
        assertAll("Test mixed match information",
                () -> assertEquals("Louis Sauerbrei", result.getFirstDoublePlayerOne().name),
                () -> assertEquals("Victoria Braun", result.getFirstDoublePlayerTwo().name),
                () -> assertEquals("Malthe Heilesen", result.getSecondDoublePlayerOne().name),
                () -> assertEquals("Madeleine Dam", result.getSecondDoublePlayerTwo().name),
                () -> assertFalse(result.hasFirstPlayerWon()),
                () -> assertEquals(19, result.getPlayersSets().getFirst().getFirstValue()),
                () -> assertEquals(21, result.getPlayersSets().getFirst().getSecondValue()),
                () -> assertEquals(17, result.getPlayersSets().get(1).getFirstValue()),
                () -> assertEquals(21, result.getPlayersSets().get(1).getSecondValue())
        );
    }
}
