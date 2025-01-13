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
        String testFileName = "tournaments/TournamentDMBonn.txt";
        HtmlPage page = loadHtmlPage(testFileName);
        content = page.getActiveElement();
    }

    @Test
    @DisplayName("Parse tournaments for 2024")
    void parseTournament() {
        // given
        var expectedYear = "2024";
        var expectedTournaments = 1;

        var expectedTournamentId = "8FE45CBC-7603-47EA-A189-2CD5A6FC6505";
        var expectedTournamentName = "Deutsche Einzelmeisterschaft U15 und U17 Bonn 2024";
        var expectedTournamentOrga = "Deutscher Badminton Verband (U19)";
        var expectedTournamentLocation = "Bonn [01-0027]";
        var expectedTournamentDate = "29.11.2024 bis 01.12.2024";

        var expectedTournamentDisciplinesSize = 3;
        var expectedTournamentDisciplineAgeGroup = "U15";
        var expectedTournamentFirstDisciplineName = "JD";
        var expectedTournamentSecondDisciplineName = "JE";
        var expectedTournamentThirdDisciplineName = "MX";

        // when
        TournamentYearDTO result = parser.parseTournamentYear("2024", content);
        
        var firstTournament = result.tournaments().getFirst();

        // then
        assertAll("Parse complete tournament for DM Bonn 2024",
                () -> assertEquals(expectedYear, result.year()),
                () -> assertEquals(expectedTournaments, result.tournaments().size()),
                () -> assertEquals(expectedTournamentId, firstTournament.getTournamentInfo().tournamentId()),
                () -> assertEquals(expectedTournamentName, firstTournament.getTournamentInfo().tournamentName()),
                () -> assertEquals(expectedTournamentOrga, firstTournament.getTournamentInfo().tournamentOrganisation()),
                () -> assertEquals(expectedTournamentLocation, firstTournament.getTournamentInfo().tournamentLocation()),
                () -> assertEquals(expectedTournamentDate, firstTournament.getTournamentInfo().tournamentDate()),
                () -> assertEquals(expectedTournamentDisciplinesSize, firstTournament.getTournamentDisciplines().size()),
                () -> assertEquals(expectedTournamentFirstDisciplineName, firstTournament.getTournamentDisciplines().getFirst().getDisciplineName()),
                () -> assertEquals(expectedTournamentDisciplineAgeGroup, firstTournament.getTournamentDisciplines().getFirst().getDisciplineAgeGroup()),
                () -> assertEquals(expectedTournamentSecondDisciplineName, firstTournament.getTournamentDisciplines().get(1).getDisciplineName()),
                () -> assertEquals(expectedTournamentDisciplineAgeGroup, firstTournament.getTournamentDisciplines().get(1).getDisciplineAgeGroup()),
                () -> assertEquals(expectedTournamentThirdDisciplineName, firstTournament.getTournamentDisciplines().get(2).getDisciplineName()),
                () -> assertEquals(expectedTournamentDisciplineAgeGroup, firstTournament.getTournamentDisciplines().get(2).getDisciplineAgeGroup())
        );
    }
}

