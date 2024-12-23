package de.ostfale.qk.parser.tournament.api;

import de.ostfale.qk.parser.tournament.internal.model.TournamentDisciplineInfoDTO;
import de.ostfale.qk.parser.tournament.internal.model.TournamentHeaderInfoDTO;
import de.ostfale.qk.parser.tournament.internal.TournamentParserService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.htmlunit.html.HtmlDivision;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Test reading the header information from a tournament")
@QuarkusTest
@Tag("unittest")
class TournamentParserTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(TournamentParserTest.class);

    @Inject
    TournamentParserService parser;

    @Test
    @DisplayName("Parse tournament header information")
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
        TournamentHeaderInfoDTO headerInfo = parser.parseHeader(tournamentsList.getFirst());

        // then
        assertAll("Test tournament header information",
                () -> assertEquals(expectedID, headerInfo.tournamentId(), "Players tournament id failed"),
                () -> assertEquals(expectedName, headerInfo.tournamentName(), "Players tournament name failed"),
                () -> assertEquals(expectedDate, headerInfo.tournamentDate(), "Players tournament date failed"),
                () -> assertEquals(expectedLocation, headerInfo.tournamentLocation(), "Players tournament location failed"),
                () -> assertEquals(expectedOrganisation, headerInfo.tournamentOrganisation(), "Players tournament organisation failed")
        );
    }

    @Test
    @DisplayName("Parse tournament discipline information")
    void parseDiscipline() {
        // given
        String testFileName = "SingleTournamentMatches.txt";
        HtmlPage page = loadHtmlPage(testFileName);
        var expectedDisciplineName = "JE";
        var expectedDisciplineAgeGroup = "U17";
        var expectedTournamentMatchesSize = 4;
        var expectedFirstRoundName = "Round of 16";
        var expectedSecondRoundName = "Quarter final";
        var expectedThirdRoundName = "Semi final";

        // when
        TournamentDisciplineInfoDTO result = parser.parseDisciplines(getPlayersTournaments(page).getFirst()).getFirst();
        // then
        assertAll("Test tournament discipline information",
                () -> assertEquals(expectedDisciplineName, result.getDisciplineName()),
                () -> assertEquals(expectedDisciplineAgeGroup, result.getDisciplineAgeGroup()),
                () -> assertEquals(expectedTournamentMatchesSize, result.getTournamentMatchInfos().size()),
                () -> assertEquals(expectedFirstRoundName, result.getTournamentMatchInfos().getFirst().getRoundName()),
                () -> assertEquals(expectedSecondRoundName, result.getTournamentMatchInfos().get(1).getRoundName()),
                () -> assertEquals(expectedThirdRoundName, result.getTournamentMatchInfos().get(2).getRoundName())
        );
    }

    private List<HtmlDivision> getPlayersTournaments(HtmlPage page) {
        final String FIRST_MODULE_CARD = "//div[contains(@class, 'module module--card')]";
        List<HtmlDivision> moduleCards = page.getByXPath(FIRST_MODULE_CARD);
        log.info("Found {} tournaments", moduleCards.size());
        return moduleCards;
    }
}

