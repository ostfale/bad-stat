package de.ostfale.qk.parser.web.tournament;

import de.ostfale.qk.parser.BaseParserTest;
import de.ostfale.qk.parser.HtmlParserException;
import de.ostfale.qk.parser.web.discipline.WebDisciplineParserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Tag("unittest")
@DisplayName("Test Forza Cup 2025 Bischoff parsing with groups")
public class TestForzaCup25Bischoff extends BaseParserTest {

    private static final String TEST_FILE_NAME = "tournaments/ForzaCupBischoff25.html";

    private WebDisciplineParserService sut;

    @BeforeEach
    void setUp() {
        sut = prepareWebDisciplineParser();
        content = loadHtmlPage(TEST_FILE_NAME);
    }

    @Test
    @DisplayName("Test parsing a single tournament")
    void testParseAllTournaments() throws HtmlParserException {
        // given
        var currentPlayer = "Emily Bischoff";

        // when
        var result = sut.parseTournamentDisciplines(currentPlayer, content.getActiveElement());

        // then
        assertThat(result.size()).isNotNull();
    }
}
