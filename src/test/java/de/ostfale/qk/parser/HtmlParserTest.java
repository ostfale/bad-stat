package de.ostfale.qk.parser;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
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
        List<HtmlElement> tournamentModules = htmlParser.getAllTournaments(content);

        // then
        assertEquals(18, tournamentModules.size());
    }

    @Test
    @DisplayName("Get all disciplines within a tournament")
    void getAllDisciplinesInTournament() {
        // given
        HtmlElement tournamentModule = htmlParser.getAllTournaments(content).getFirst();

        // when
        List<HtmlElement> disciplineGroups = htmlParser.getAllDisciplines(tournamentModule);

        // then
        assertEquals(3, disciplineGroups.size());
    }

    @Test
    @DisplayName("Get all matches within a discipline")
    void getAllMatchesInDiscipline() {
        // given
        HtmlElement tournamentModule = htmlParser.getAllTournaments(content).getFirst();
        List<HtmlElement> disciplineGroups = htmlParser.getAllDisciplines(tournamentModule);

        // when
        var matches = htmlParser.getAllMatchesForDisciplineContainer(disciplineGroups.getFirst());

        // then
        assertEquals(3, matches.size());
    }

    @Test
    @DisplayName("Get header information for a single match")
    void getMatchHeaderInfoForSingleMatch() {
        // given
        HtmlElement tournamentModule = htmlParser.getAllTournaments(content).getFirst();
        List<HtmlElement> disciplineGroups = htmlParser.getAllDisciplines(tournamentModule);
        var matches = htmlParser.getAllMatchesForDisciplineContainer(disciplineGroups.getFirst());

        // when
        var headerInfo = htmlParser.getMatchHeaderElement(matches.getFirst());

        // then
        assertEquals("Round of 16", headerInfo.asNormalizedText());
    }

    @Test
    @DisplayName("Get footer information for a single match")
    void getMatchFooterInfoForSingleMatch() {
        // given
        HtmlElement tournamentModule = htmlParser.getAllTournaments(content).getFirst();
        List<HtmlElement> disciplineGroups = htmlParser.getAllDisciplines(tournamentModule);
        var matches = htmlParser.getAllMatchesForDisciplineContainer(disciplineGroups.getFirst());

        // when
        var footerInfo = htmlParser.getMatchFooterElement(matches.getFirst());

        // then
        assertEquals("Sa 30.11.2024", footerInfo.asNormalizedText());
    }
}
