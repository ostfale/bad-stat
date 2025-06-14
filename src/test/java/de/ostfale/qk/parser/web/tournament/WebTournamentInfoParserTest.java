package de.ostfale.qk.parser.web.tournament;

import de.ostfale.qk.parser.BaseParserTest;
import de.ostfale.qk.parser.HtmlParserException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@QuarkusTest
@DisplayName("Test web tournament info parser")
class WebTournamentInfoParserTest extends BaseParserTest {

            private static final String TEST_FILE_NAME = "tournaments/TournamentInfoHeader.html";

    @Inject
    WebTournamentInfoParser sut;

    private HtmlElement content;

    @BeforeEach
    void setUp() {
        HtmlPage page = loadHtmlPage(TEST_FILE_NAME);
        content = page.getActiveElement();
    }

    @Test
    @DisplayName("Test parsing tournament info")
    void testParseTournamentInfo() throws HtmlParserException {
        // given
        var expectedTournamentName = "VICTOR International Junior Cup 2025";
        var expectedOrganisation = "HAM - TSG Bergedorf";
        var expectedLocation = "Hamburg";
        var expectedDate = "07.06.2025 bis 09.06.2025";
        var expectedYear = 2025;

        // when
        var result = sut.parseTournamentInfo(content);

        // then
        assertAll("Test content of tournament info header",
                () -> assertThat(result.tournamentName()).isEqualTo(expectedTournamentName),
                () -> assertThat(result.tournamentOrganizer()).isEqualTo(expectedOrganisation),
                () -> assertThat(result.tournamentLocation()).isEqualTo(expectedLocation),
                () -> assertThat(result.tournamentDate()).isEqualTo(expectedDate),
                () -> assertThat(result.tournamentYear()).isEqualTo(expectedYear)
        );
    }
}
