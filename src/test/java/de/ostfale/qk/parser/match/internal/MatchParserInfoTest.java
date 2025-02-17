package de.ostfale.qk.parser.match.internal;

import de.ostfale.qk.parser.BaseParserTest;
import de.ostfale.qk.parser.match.internal.model.MatchInfoRawModel;
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
        MatchInfoRawModel matchInfoRawModel = parser.parseMatchGroupInfo(content);

        // then
        assertAll("Test general match information ",
                () -> assertEquals("Round of 16", matchInfoRawModel.roundName()),
                () -> assertEquals("Sa 02.03.2024", matchInfoRawModel.roundDate()),
                () -> assertEquals("17m", matchInfoRawModel.roundDuration()),
                () -> assertEquals("Sporthalle Dwasieden - 2", matchInfoRawModel.roundLocation())
        );
    }
}
