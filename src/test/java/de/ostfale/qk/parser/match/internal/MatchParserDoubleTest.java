package de.ostfale.qk.parser.match.internal;

import de.ostfale.qk.parser.BaseParserTest;
import de.ostfale.qk.parser.match.internal.model.DoubleMatchRawModel;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.htmlunit.html.HtmlDivision;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Test reading double match information")
@Tag("unittest")
@QuarkusTest
class MatchParserDoubleTest extends BaseParserTest {

    @Inject
    MatchParserService parser;

    @Test
    @DisplayName("Parse double match information")
    void parseDoubleMatch() {
        // given
        String testFileName = "matches/DoubleMatch.txt";
        HtmlPage page = loadHtmlPage(testFileName);
        HtmlDivision content = (HtmlDivision) page.getActiveElement().getFirstChild();

        // when
        DoubleMatchRawModel result = parser.parseDoubleMatch(content);

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

    @Test
    @DisplayName("Parse double match with walkover")
    void parseDoubleMatchWalkover() {
        // given
        String testFileName = "matches/DoubleMatchWalkover.txt";
        HtmlPage page = loadHtmlPage(testFileName);
        HtmlDivision content = (HtmlDivision) page.getActiveElement().getFirstChild();

        // when
        DoubleMatchRawModel result = parser.parseDoubleMatch(content);

        // then
        assertAll("Test double match information",
                () -> assertEquals("Louis Sauerbrei", result.getFirstDoublePlayerOne().name),
                () -> assertEquals("Tony Chengxi Wang", result.getFirstDoublePlayerTwo().name),
                () -> assertEquals("Florian Soika", result.getSecondDoublePlayerOne().name),
                () -> assertEquals("Bjarne Pascal Stüve", result.getSecondDoublePlayerTwo().name),
                () -> assertTrue(result.hasFirstPlayerWon())
        );
    }
}
