package de.ostfale.qk.parser.tournament.api;

import de.ostfale.qk.parser.BaseTest;
import de.ostfale.qk.parser.tournament.internal.TournamentParserService;
import de.ostfale.qk.parser.tournament.internal.model.TournamentInfoDTO;
import de.ostfale.qk.parser.tournament.internal.model.TournamentYearDTO;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.htmlunit.html.HtmlDivision;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Read all tournaments for a given year")
@QuarkusTest
@Tag("unittest")
class TournamentParserTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(TournamentParserTest.class);

    @Inject
    TournamentParserService parser;

    HtmlElement content;

    @BeforeEach
    void setUp() {
        String testFileName = "tournaments/Tournaments24.txt";
        HtmlPage page = loadHtmlPage(testFileName);
        content = page.getActiveElement();
    }

    @Test
    @DisplayName("Parse tournaments for 2024")
    void parseTournament() {
        // given
        String testYear = "2024";

        // when
        TournamentYearDTO result = parser.parseTournamentYear(testYear, content);

        // then
        assertAll("Parse all tournaments for 2024",
                () -> assertEquals(testYear, result.year()));

    }


    @Test
    @DisplayName("Parse tournament header information")
    void parseHeader() {
        // given
        String testFileName = "matches/SingleTournamentMatches.txt";
        HtmlPage page = loadHtmlPage(testFileName);
        var expectedID = "DDAD417D-28AD-4C58-A5BD-38D34E647136";
        var expectedDate = "02.03.2024";
        var expectedName = "2. C-RLT MVP U13_U17 Sassnitz 2024";
        var expectedLocation = "Sassnitz";
        var expectedOrganisation = "Badminton-Verband Mecklenburg-Vorpommern";

        // when
        List<HtmlDivision> tournamentsList = getPlayersTournaments(page);
        TournamentInfoDTO headerInfo = parser.parseTournamentInfo(tournamentsList.getFirst());

        // then
        assertAll("Test tournament header information",
                () -> assertEquals(expectedID, headerInfo.tournamentId(), "Players tournament id failed"),
                () -> assertEquals(expectedName, headerInfo.tournamentName(), "Players tournament name failed"),
                () -> assertEquals(expectedDate, headerInfo.tournamentDate(), "Players tournament date failed"),
                () -> assertEquals(expectedLocation, headerInfo.tournamentLocation(), "Players tournament location failed"),
                () -> assertEquals(expectedOrganisation, headerInfo.tournamentOrganisation(), "Players tournament organisation failed")
        );
    }

    private List<HtmlDivision> getPlayersTournaments(HtmlPage page) {
        final String FIRST_MODULE_CARD = "//div[contains(@class, 'module module--card')]";
        List<HtmlDivision> moduleCards = page.getByXPath(FIRST_MODULE_CARD);
        log.info("Found {} tournaments", moduleCards.size());
        return moduleCards;
    }
}

