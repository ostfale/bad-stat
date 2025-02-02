package de.ostfale.qk.parser.match.internal;

import de.ostfale.qk.parser.BaseParserTest;
import de.ostfale.qk.parser.match.internal.model.SingleMatchDTO;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.htmlunit.html.HtmlDivision;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test reading the single match information")
@Tag("unittest")
@QuarkusTest
public class MatchParserSingleTest extends BaseParserTest {

    @Inject
    MatchParserService parser;

    @Test
    @DisplayName("Parse single match information")
    void parseSingleMatch() {
        // given
        String testFileName = "matches/SingleMatch.txt";
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
    @DisplayName("Parse single match with walkover")
    void parseSingleMatchWalkover() {
        // given
        String testFileName = "matches/SingleMatchWalkover.txt";
        HtmlPage page = loadHtmlPage(testFileName);
        HtmlDivision content = (HtmlDivision) page.getActiveElement().getFirstChild();

        // then
        SingleMatchDTO result = parser.parseSingleMatch(content);

        // then
        assertAll("Test  single match with walkover information",
                () -> assertEquals("Ivan Tutic", result.getFirstPlayer().name),
                () -> assertEquals("Louis Sauerbrei", result.getSecondPlayer().name),
                () -> assertTrue(result.hasFirstPlayerWon())
        );
    }

    @Test
    @DisplayName("Parse single match retired lost")
    void parseSingleMatchRetiredLost() {
        // given
        String testFileName = "matches/SingleMatchRetiredLost.txt";
        HtmlPage page = loadHtmlPage(testFileName);
        HtmlDivision content = (HtmlDivision) page.getActiveElement().getFirstChild();

        // then
        SingleMatchDTO result = parser.parseSingleMatch(content);

        // then
        assertAll("Test  single match with retired information",
                () -> assertEquals("Louis Sauerbrei", result.getFirstPlayer().name),
                () -> assertEquals("Joris Meyer", result.getSecondPlayer().name),
                () -> assertFalse(result.hasFirstPlayerWon()),
                () -> assertTrue(result.getMatchRetired())
        );
    }

    @Test
    @DisplayName("Parse single match retired won")
    void parseSingleMatchRetiredWon() {
        // given
        String testFileName = "matches/SingleMatchRetiredWon.txt";
        HtmlPage page = loadHtmlPage(testFileName);
        HtmlDivision content = (HtmlDivision) page.getActiveElement().getFirstChild();

        // then
        SingleMatchDTO result = parser.parseSingleMatch(content);

        // then
        assertAll("Test  single match with retired information",
                () -> assertEquals("Soheyl Safari Araghi", result.getFirstPlayer().name),
                () -> assertEquals("Hannes Merget", result.getSecondPlayer().name),
                () -> assertTrue(result.hasFirstPlayerWon()),
                () -> assertTrue(result.getMatchRetired())
        );
    }

    @Test
    @DisplayName("Parse single match retired won - kein Spiel")
    void parseSingleMatchRetiredWonNoMatch() {
        // given
        String testFileName = "matches/SingleMatchRetiredKeinSpiel.txt";
        HtmlPage page = loadHtmlPage(testFileName);
        HtmlDivision content = (HtmlDivision) page.getActiveElement().getFirstChild();

        // then
        SingleMatchDTO result = parser.parseSingleMatch(content);

        // then
        assertAll("Test  single match with retired information = Kein Spiel",
                () -> assertEquals("Soheyl Safari Araghi", result.getFirstPlayer().name),
                () -> assertEquals("Aaron Winter", result.getSecondPlayer().name),
                () -> assertTrue(result.hasFirstPlayerWon()),
                () -> assertTrue(result.getMatchRetired()),
                () -> assertTrue(result.getPlayersSets().isEmpty())
        );
    }
}
