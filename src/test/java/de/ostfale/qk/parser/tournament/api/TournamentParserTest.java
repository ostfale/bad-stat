package de.ostfale.qk.parser.tournament.api;

import de.ostfale.qk.parser.BaseTest;
import de.ostfale.qk.parser.tournament.internal.TournamentParserService;
import de.ostfale.qk.parser.tournament.internal.model.TournamentYearDTO;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Read all Information DM 2024")
@QuarkusTest
@Tag("unittest")
class TournamentParserTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(TournamentParserTest.class);

    @Inject
    TournamentParserService parser;

    HtmlElement content;

    @BeforeEach
    void setUp() {
        String testFileName = "tournaments/TournamentDMBonn.txt";
        HtmlPage page = loadHtmlPage(testFileName);
        content = page.getActiveElement();
    }

    @Test
    @DisplayName("Parse all tournament infos for DM 2024 Bonn")
    void parseTournamentInfos() {
        // given
        var expectedYear = "2024";
        var expectedTournaments = 1;

        var expectedTournamentId = "8FE45CBC-7603-47EA-A189-2CD5A6FC6505";
        var expectedTournamentName = "Deutsche Einzelmeisterschaft U15 und U17 Bonn 2024";
        var expectedTournamentOrga = "Deutscher Badminton Verband (U19)";
        var expectedTournamentLocation = "Bonn [01-0027]";
        var expectedTournamentDate = "29.11.2024 bis 01.12.2024";

        // when
        TournamentYearDTO result = parser.parseTournamentYear("2024", content);
        var firstTournament = result.tournaments().getFirst();

        // then
        assertAll("Parse tournament general information",
                () -> assertEquals(expectedYear, result.year()),
                () -> assertEquals(expectedTournaments, result.tournaments().size()),
                () -> assertEquals(expectedTournamentId, firstTournament.getTournamentInfo().tournamentId()),
                () -> assertEquals(expectedTournamentName, firstTournament.getTournamentInfo().tournamentName()),
                () -> assertEquals(expectedTournamentOrga, firstTournament.getTournamentInfo().tournamentOrganisation()),
                () -> assertEquals(expectedTournamentLocation, firstTournament.getTournamentInfo().tournamentLocation()),
                () -> assertEquals(expectedTournamentDate, firstTournament.getTournamentInfo().tournamentDate())
        );
    }

    @Test
    @DisplayName("Parse general information for all disciplines played")
    void parseTournamentDisciplines() {
        // given
        var expectedTournamentDisciplinesSize = 3;
        var expectedTournamentDisciplineAgeGroup = "U15";
        var expectedTournamentFirstDisciplineName = "JD";
        var expectedTournamentFirstDiscipline = "DOUBLE";
        var expectedTournamentSecondDisciplineName = "JE";
        var expectedTournamentSecondDiscipline = "SINGLE";
        var expectedTournamentThirdDisciplineName = "MX";
        var expectedTournamentThirdDiscipline = "MIXED";


        // when
        TournamentYearDTO result = parser.parseTournamentYear("2024", content);
        var firstTournament = result.tournaments().getFirst();
        var doubleDiscipline = firstTournament.getTournamentDisciplines().getFirst();
        var singleDiscipline = firstTournament.getTournamentDisciplines().get(1);
        var mixedDiscipline = firstTournament.getTournamentDisciplines().get(2);

        // then
        assertAll("Test parameter for general discipline information",
                () -> assertEquals(expectedTournamentDisciplinesSize, firstTournament.getTournamentDisciplines().size()),
                () -> assertEquals(expectedTournamentDisciplineAgeGroup, doubleDiscipline.getAgeClass().name()),
                () -> assertEquals(expectedTournamentFirstDisciplineName, doubleDiscipline.getDisciplineName()),
                () -> assertEquals(expectedTournamentFirstDiscipline, doubleDiscipline.getDiscipline().name()),
                () -> assertEquals(expectedTournamentSecondDisciplineName, singleDiscipline.getDisciplineName()),
                () -> assertEquals(expectedTournamentSecondDiscipline, singleDiscipline.getDiscipline().name()),
                () -> assertEquals(expectedTournamentDisciplineAgeGroup, singleDiscipline.getAgeClass().name()),
                () -> assertEquals(expectedTournamentThirdDisciplineName, mixedDiscipline.getDisciplineName()),
                () -> assertEquals(expectedTournamentThirdDiscipline, mixedDiscipline.getDiscipline().name()),
                () -> assertEquals(expectedTournamentDisciplineAgeGroup, mixedDiscipline.getAgeClass().name())
        );
    }


    @Test
    @DisplayName("Parse discipline match information")
    void parseDisciplineGroups() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("Parse all matches information")
    void parseMatches() {
        // given

        // when

        // then
    }
}

