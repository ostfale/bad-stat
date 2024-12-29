package de.ostfale.qk.parser.match.internal;

import de.ostfale.qk.parser.BaseTest;
import de.ostfale.qk.parser.match.internal.model.DoubleMatchDTO;
import de.ostfale.qk.parser.match.internal.model.SingleMatchDTO;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.htmlunit.html.HtmlDivision;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Test reading the match information from a tournament")
@Tag("unittest")
@QuarkusTest
class MatchParserServiceTest extends BaseTest {

    @Inject
    MatchParserService parser;

    @Test
    @DisplayName("Parse single match information")
    void parseSingleMatch() {
        // given
        String testFileName = "SingleMatch.txt";
        HtmlPage page = loadHtmlPage(testFileName);
        HtmlDivision content = (HtmlDivision) page.getActiveElement().getFirstChild();

        // then
        SingleMatchDTO result = parser.parseSingleMatch(content);

        // then
        assertAll("Test  single match information",
                () -> assertEquals("Konrad Möller", result.getFirstPlayer().name),
                () -> assertEquals("Louis Sauerbrei", result.getSecondPlayer().name),
                () -> assertFalse(result.hasFirstPlayerWon()),
                () -> assertEquals(4, result.getPlayersSets().getFirst().getFirstValue()),
                () -> assertEquals(21, result.getPlayersSets().getFirst().getSecondValue()),
                () -> assertEquals(7, result.getPlayersSets().getLast().getFirstValue()),
                () -> assertEquals(21, result.getPlayersSets().getLast().getSecondValue())
        );
    }

    @Test
    @DisplayName("Parse double match information")
    void parseDoubleMatch() {
        // given
        String testFileName = "DoubleMatch.txt";
        HtmlPage page = loadHtmlPage(testFileName);
        HtmlDivision content = (HtmlDivision) page.getActiveElement().getFirstChild();

        // when
        DoubleMatchDTO result = parser.parseDoubleMatch(content);

        // then
        assertAll("Test double match information",
                () -> assertEquals("Louis Sauerbrei", result.getFirstDoublePlayerOne().name),
                () -> assertEquals("Tony Chengxi Wang", result.getFirstDoublePlayerTwo().name),
                () -> assertEquals("Joey Kobylanski", result.getSecondDoublePlayerOne().name),
                () -> assertEquals("Artur Plaisant", result.getSecondDoublePlayerTwo().name),
                () -> assertTrue(result.hasFirstPlayerWon()),
                () -> assertEquals(10, result.getPlayersSets().getFirst().getFirstValue()),
                () -> assertEquals(21, result.getPlayersSets().getFirst().getSecondValue()),
                () -> assertEquals(24, result.getPlayersSets().get(1).getFirstValue()),
                () -> assertEquals(22, result.getPlayersSets().get(1).getSecondValue()),
                () -> assertEquals(21, result.getPlayersSets().get(2).getFirstValue()),
                () -> assertEquals(18, result.getPlayersSets().get(2).getSecondValue())
        );
    }
}
