package de.ostfale.qk.parser.match.internal;

import de.ostfale.qk.parser.BaseParserTest;
import de.ostfale.qk.parser.match.internal.model.MatchInfoDTO;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DisplayName("Test reading general match information ")
@Tag("unittest")
@QuarkusTest
public class MatchParserInfoTest extends BaseParserTest {

    @Inject
    MatchParserService parser;

    @Test
    @DisplayName("General match data")
    void parseMatchInfo() {
        // given
        String testFileName = "matches/MatchGroup.txt";
        HtmlPage page = loadHtmlPage(testFileName);
        HtmlElement content = page.getActiveElement();

        // when
        MatchInfoDTO matchInfoDTO = parser.parseMatchGroupInfo(content);

        // then
        assertAll("Test general match information ",
                () -> assertEquals("Round of 16", matchInfoDTO.getRoundName()),
                () -> assertEquals("Sa 02.03.2024", matchInfoDTO.getRoundDate()),
                () -> assertEquals("17m", matchInfoDTO.getRoundDuration()),
                () -> assertEquals("Sporthalle Dwasieden - 2", matchInfoDTO.getRoundLocation())
        );
    }
}
