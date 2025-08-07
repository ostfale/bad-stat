package de.ostfale.qk.parser.web.tournament;

import de.ostfale.qk.domain.discipline.AgeClass;
import de.ostfale.qk.domain.discipline.DisciplineType;
import de.ostfale.qk.parser.BaseParserTest;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Test web tournament parser for a yearly tournament collection")
class WebTournamentParserServiceTest extends BaseParserTest {

    private static final String TEST_FILE_NAME = "tournaments/Tournaments2025VB.html";
    private static final String TEST_AVAILABLE_TOURNAMENT_YEARS = "tournaments/YearlyTournamentsTabsOverview.html";

    WebTournamentParserService sut;

    @BeforeEach
    void setUp() {
        sut = prepareWebTournamentParser();
    }

    @Test
    @DisplayName("Test parsing all tournaments for a year")
    void testParseAllTournaments() {
        // given
        HtmlPage page = loadHtmlPage(TEST_FILE_NAME);
        var expectedNumberOfTournaments = 9;
        var playerName = "Victoria Braun";

        // when
        var result = sut.parseAllYearlyTournamentsForPlayer(playerName, page);

        // then
        assertThat(result.size()).isEqualTo(expectedNumberOfTournaments);
    }

    @Test
    @DisplayName("Test Discipline header information for age class")
    void testDisciplineHeaders() {
        // given
        String playerName = "Victoria Braun";

        // when
        HtmlPage page = loadHtmlPage(TEST_FILE_NAME);
        var allTournaments = sut.parseAllYearlyTournamentsForPlayer(playerName, page);

        // then
        var firstTournament = allTournaments.getFirst();
        var mixedDiscipline = firstTournament.getDisciplineByType(DisciplineType.MIXED);
        assertThat(mixedDiscipline.getDisciplineInfo().ageClass()).isEqualTo(AgeClass.U17);

        var secondTournament = allTournaments.get(1);
        var doubleDiscipline = secondTournament.getDisciplineByType(DisciplineType.DOUBLE);
        assertThat(doubleDiscipline.getDisciplineInfo().ageClass()).isEqualTo(AgeClass.U19);
    }

    @Test
    @DisplayName("Test retrieving the available years for player with tournaments")
    void testPlayerTournamentYearsAvailable() {
        // given
        HtmlPage page = loadHtmlPage(TEST_AVAILABLE_TOURNAMENT_YEARS);
        var expectedNumberOfYears = 5;
        var expectedListOfYears = List.of(2024, 2023, 2022, 2021, 2020);

        // when
        var result = sut.getListOfYearsWithTournaments(page);

        // then
        assertAll("Verify tournament years",
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.size()).isEqualTo(expectedNumberOfYears),
                () -> assertThat(result).isEqualTo(expectedListOfYears)
        );
    }
}
