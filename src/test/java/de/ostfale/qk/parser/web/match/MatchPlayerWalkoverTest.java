package de.ostfale.qk.parser.web.match;

import de.ostfale.qk.domain.match.DisciplineMatch;
import de.ostfale.qk.parser.BaseParserTest;
import de.ostfale.qk.parser.web.player.MatchPlayerParserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unittest")
@DisplayName("Test matches with walkover result")
public class MatchPlayerWalkoverTest  extends BaseParserTest {

    private static final String TEST_FILE_NAME = "matches/DoubleMatchWalkoverVB.html";

    private MatchPlayerParserService sut;

    @BeforeEach
    void setUp() {
        sut = new MatchPlayerParserService();
        content = loadHtmlPage(TEST_FILE_NAME);
    }

    @Test
    @DisplayName("Test parsing a double match with a walkover")
    void testDoubleMatchWithWalkover() {
        // given
        DisciplineMatch disciplineMatch = new DisciplineMatch();

        // when
        sut.parseMatchPlayers(disciplineMatch, content.getActiveElement());

        // then

    }
}
