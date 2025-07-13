package de.ostfale.qk.parser.web.discipline;

import de.ostfale.qk.domain.discipline.AgeClass;
import de.ostfale.qk.domain.discipline.DisciplineType;
import de.ostfale.qk.domain.discipline.TournamentDiscipline;
import de.ostfale.qk.parser.BaseParserTest;
import de.ostfale.qk.parser.HtmlParserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Tag("unittest")
@DisplayName("Test parsing of disciplines from web page")
class WebDisciplineParserServiceTest extends BaseParserTest {

    private static final String TEST_FILE_NAME = "tournaments/TournamentEliminationVB25.html";

    private WebDisciplineParserService sut;

    @BeforeEach
    void setUp() {
        sut = prepareWebDisciplineParser();
        content = loadHtmlPage(TEST_FILE_NAME);
    }

    @Test
    @DisplayName("Test complete tournament with only elimination matches")
    void testEliminationTournamentMatchesOnly() throws HtmlParserException {
        // given
        var expectedNumberOfDisciplines = 3;
        var expectedNumberOfMixedMatches = 3;
        var expectedNumberOfDoubleMatches = 2;
        var expectedNumberOfSingleMatches = 2;

        // when
        List<TournamentDiscipline> result = sut.parseTournamentDisciplines(content.getActiveElement());

        // then
        assertAll("Verify extraction of a complete tournament",
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.size()).isEqualTo(expectedNumberOfDisciplines),
                () -> assertThat(result.getFirst().getDisciplineType()).isEqualTo(DisciplineType.MIXED),
                () -> assertThat(result.getFirst().getDisciplineAgeClass()).isEqualTo(AgeClass.U17),
                () -> assertThat(result.getFirst().getEliminationMatches().size()).isEqualTo(expectedNumberOfMixedMatches),
                () -> assertThat(result.get(1).getDisciplineType()).isEqualTo(DisciplineType.DOUBLE),
                () -> assertThat(result.get(1).getDisciplineAgeClass()).isEqualTo(AgeClass.U17),
                () -> assertThat(result.get(1).getEliminationMatches().size()).isEqualTo(expectedNumberOfDoubleMatches),
                () -> assertThat(result.getLast().getDisciplineType()).isEqualTo(DisciplineType.SINGLE),
                () -> assertThat(result.getLast().getDisciplineAgeClass()).isEqualTo(AgeClass.U17),
                () -> assertThat(result.getLast().getEliminationMatches().size()).isEqualTo(expectedNumberOfSingleMatches)
        );
    }
}
