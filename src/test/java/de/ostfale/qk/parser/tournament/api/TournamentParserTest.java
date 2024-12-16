package de.ostfale.qk.parser.tournament.api;

import de.ostfale.qk.parser.tournament.internal.HtmlTournamentParser;
import de.ostfale.qk.parser.tournament.internal.TournamentHeaderInfo;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.htmlunit.html.HtmlDivision;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Test reading the header information from a tournament")
@QuarkusTest
class TournamentParserTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(TournamentParserTest.class);

    @Inject
    HtmlTournamentParser parser;

    @Test
    void parseHeader() {
        // given
        String testFileName = "SingleTournamentMatches.txt";
        HtmlPage page = loadHtmlPage(testFileName);
        var expectedID = "DDAD417D-28AD-4C58-A5BD-38D34E647136";
        var expectedDate = "02.03.2024";
        var expectedName = "2. C-RLT MVP U13_U17 Sassnitz 2024";
        var expectedLocation = "Sassnitz";
        var expectedOrganisation = "Badminton-Verband Mecklenburg-Vorpommern";

        // when
        List<HtmlDivision> tournamentsList = getPlayersTournaments(page);
        TournamentHeaderInfo headerInfo = parser.parseHeader(tournamentsList.getFirst());

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

