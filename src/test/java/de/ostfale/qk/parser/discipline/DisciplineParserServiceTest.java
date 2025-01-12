package de.ostfale.qk.parser.discipline;

import de.ostfale.qk.parser.BaseParserTest;
import de.ostfale.qk.parser.match.internal.MatchParserService;
import de.ostfale.qk.parser.tournament.internal.model.TournamentDisciplineDTO;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.htmlunit.html.HtmlDivision;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test reading the single discipline information")
@Tag("unittest")
@QuarkusTest
class DisciplineParserServiceTest extends BaseParserTest {

    @Inject
    MatchParserService parser;

    @Inject
    DisciplineParserService disciplineParser;

    @Test
    @DisplayName("Parse discipline")
    void parseDiscipline() {
        // given
        String testFileName = "disciplines/SingleDiscipline.txt";
        HtmlPage page = loadHtmlPage(testFileName);
        HtmlDivision content = (HtmlDivision) page.getActiveElement().getFirstChild();

        String expectedDiscipline = "JE";
        String expectedAge = "U17";

        // when
        List<TournamentDisciplineDTO> disciplineDTOs = disciplineParser.parseTournamentDisciplines(content);

        // then
        assertAll("Test parsing tournament discipline",
                () -> assertNotNull(disciplineDTOs.getFirst()),
                () -> assertEquals(expectedDiscipline, disciplineDTOs.getFirst().getDisciplineName()),
                () -> assertEquals((expectedAge), disciplineDTOs.getFirst().getDisciplineAgeGroup())
        );
    }

}
