package de.ostfale.qk.parser.discipline;

import de.ostfale.qk.parser.BaseParserTest;
import de.ostfale.qk.parser.discipline.internal.model.DisciplineDTO;
import de.ostfale.qk.parser.match.internal.MatchParserService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.htmlunit.html.HtmlDivision;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

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

        // when
        DisciplineDTO disciplineDTO = disciplineParser.parseDiscipline(content);

        // then
    }

}
