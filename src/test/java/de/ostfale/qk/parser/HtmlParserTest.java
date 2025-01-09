package de.ostfale.qk.parser;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.htmlunit.html.HtmlDivision;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Test parsing HTML structure")
@Tag("unittest")
@QuarkusTest
class HtmlParserTest extends BaseParserTest {

    @Inject
    HtmlParser htmlParser;

    HtmlElement content;

    @BeforeEach
    void setUp() {
        String testFileName = "tournaments/Tournaments24.txt";
        HtmlPage page = loadHtmlPage(testFileName);
        content = page.getActiveElement();
    }

    @Test
    @DisplayName("Get all tournaments divs for the year")
    void getAllTournamentsDivsForTheYear() {
        // when
        List<HtmlDivision> tournamentModules = htmlParser.getAllTournaments(content);

        // then
        assertEquals(18, tournamentModules.size());
    }

    @Test
    @DisplayName("Get all disciplines within a tournament")
    void getAllDisciplinesInTournament() {
        // given
        HtmlDivision tournamentModule = htmlParser.getAllTournaments(content).getFirst();

        // when
        List<HtmlDivision> disciplineGroups = htmlParser.getAllDisciplines(tournamentModule);

        // then
        assertEquals(3, disciplineGroups.size());
    }

    @Test
    @DisplayName("Get all matches within a discipline")
    void getAllMatchesInDiscipline() {
        // given
        HtmlDivision tournamentModule = htmlParser.getAllTournaments(content).getFirst();
        List<HtmlDivision> disciplineGroups = htmlParser.getAllDisciplines(tournamentModule);

        // when
        var matches = htmlParser.getFullMatchInfo(disciplineGroups.getFirst());

        // then
        assertEquals(3, matches.size());
    }
}
