package de.ostfale.qk.parser.discipline;

import de.ostfale.qk.parser.BaseParserTest;
import de.ostfale.qk.parser.HtmlParserException;
import de.ostfale.qk.parser.discipline.model.DisciplineParserModel;
import de.ostfale.qk.parser.match.internal.MatchParserService;
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
    void parseDiscipline() throws HtmlParserException {
        // given
        String testFileName = "disciplines/SingleDiscipline.txt";
        HtmlPage page = loadHtmlPage(testFileName);
        HtmlDivision content = (HtmlDivision) page.getActiveElement().getFirstChild();

        String expectedDiscipline = "SINGLE";
        String expectedAge = "U17";

        // when
        List<DisciplineParserModel> disciplineDTOs = disciplineParser.parseDisciplines(content);

        // then
        assertAll("Test parsing tournament discipline",
                () -> assertNotNull(disciplineDTOs.getFirst()),
                () -> assertEquals(expectedDiscipline, disciplineDTOs.getFirst().getDiscipline().name()),
                () -> assertEquals((expectedAge), disciplineDTOs.getFirst().getAgeClass().name())
        );
    }


    @Test
    @DisplayName("Parse discipline with group phase")
    void parseDisciplineWithGroupPhase() throws HtmlParserException {
        // given
        String testFileName = "tournaments/TournamentWithGroupPhase.txt";
        HtmlPage page = loadHtmlPage(testFileName);
        HtmlDivision content = (HtmlDivision) page.getActiveElement().getFirstChild();

        // when
        List<DisciplineParserModel> disciplineDTOs = disciplineParser.parseDisciplines(content);

        // then
        assertAll("Test parsing a discipline with group phase",
                () -> assertNotNull(disciplineDTOs.getFirst())
        );
    }
}
