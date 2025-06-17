package de.ostfale.qk.parser.web.tournament;

import de.ostfale.qk.domain.discipline.AgeClass;
import de.ostfale.qk.domain.discipline.DisciplineType;
import de.ostfale.qk.domain.discipline.DoubleDiscipline;
import de.ostfale.qk.domain.discipline.MixedDiscipline;
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

@QuarkusTest
@DisplayName("Test web tournament parser for a yearly tournament collection")
class WebTournamentParserTest extends BaseParserTest {

    private static final String TEST_FILE_NAME = "tournaments/Tournaments2025VB.html";

    @Inject
    WebTournamentParser sut;

    private HtmlElement content;

    @BeforeEach
    void setUp() {
        HtmlPage page = loadHtmlPage(TEST_FILE_NAME);
        content = page.getActiveElement();
    }

    @Test
    @DisplayName("Test parsing all tournaments for a year")
    void testParseAllTournaments() throws HtmlParserException {
        // given
        var expectedNumberOfTournaments = 9;

        // when
        var result = sut.parseAllYearlyTournamentsForPlayer(content);

        // then
        assertThat(result.size()).isEqualTo(expectedNumberOfTournaments);
    }

    @Test
    @DisplayName("Test Discipline header information for age class")
    void testDisciplineHeaders() throws HtmlParserException{
        // when
        var allTournaments = sut.parseAllYearlyTournamentsForPlayer(content);

        // then
        var firstTournament = allTournaments.getFirst();
        MixedDiscipline mixedDiscipline = (MixedDiscipline) firstTournament.getDisciplineByType(DisciplineType.MIXED);
        assertThat(mixedDiscipline.getDisciplineAgeClass()).isEqualTo(AgeClass.U17);

        var secondTournament = allTournaments.get(1);
        DoubleDiscipline doubleDiscipline = (DoubleDiscipline) secondTournament.getDisciplineByType(DisciplineType.DOUBLE);
        assertThat(doubleDiscipline.getDisciplineAgeClass()).isEqualTo(AgeClass.U19);

        var thirdTournament = allTournaments.get(2);
        MixedDiscipline mixedDiscipline1 = (MixedDiscipline) thirdTournament.getDisciplineByType(DisciplineType.MIXED);
        assertThat(mixedDiscipline1.getDisciplineAgeClass()).isEqualTo(AgeClass.U19);
    }
}
