package de.ostfale.qk.parser.web.discipline;

import de.ostfale.qk.domain.discipline.DisciplineInfo;
import de.ostfale.qk.parser.HtmlParserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Tag("unittest")
@DisplayName("Test parsing of discipline header information")
class WebDisciplineInfoParserServiceTest {

    private WebDisciplineInfoParserService sut;

    @BeforeEach
    void setUp() {
        sut = new WebDisciplineInfoParserService();
    }

    @ParameterizedTest(name = "Test extraction of age class and discipline from given header string {0}")
    @CsvSource({
            "Konkurrenz: MX-U17 A, Mixed, U17",
            "Konkurrenz: ME U15, Einzel, U15",
            "Konkurrenz: U19A Mixed,Mixed,U19",
            "Konkurrenz: DD,Doppel,UOX",
            "Konkurrenz: Mixed Doubles,Mixed,UOX",
            "Konkurrenz: MX U15 A,Mixed,U15",
            "Konkurrenz: Mixed Doubles U15,Mixed,U15"
    })
    @DisplayName("Test extraction of age class and discipline from given header strings")
    void testDisciplineAgeClassAndTypeExtractionFromHeaderStrings(String phrase, String expectedDisciplineType, String expectedAgeClass) throws HtmlParserException {
        // given

        // when
        DisciplineInfo result = sut.extractDisciplineHeaderInfo(phrase);
        // then
        assertAll("Test header string interpretation",
                () -> assertThat(result.originalString()).isEqualTo(phrase),
                () -> assertThat(result.disciplineType().getDisplayString()).isEqualTo(expectedDisciplineType),
                () -> assertThat(result.ageClass().name()).isEqualTo(expectedAgeClass)
        );
    }
}
