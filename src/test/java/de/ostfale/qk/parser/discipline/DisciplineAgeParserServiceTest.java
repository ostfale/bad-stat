package de.ostfale.qk.parser.discipline;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;

@DisplayName("Test parsing the age class and discipline")
@Tag("unittest")
class DisciplineAgeParserServiceTest {
/*
    private DisciplineAgeParserService parser;

    @BeforeEach
    void setUp() {
        parser = new DisciplineAgeParserService();
    }

    private static Stream<Arguments> irregularDiscAgeProvider() {
        return DisciplineAgeParserService.IRREGULAR_DISC_AGE.entrySet().stream()
                .map(entry -> Arguments.of(entry.getKey(), entry.getValue()));
    }

    @ParameterizedTest(name = "Parse irregular discipline age token: {0}")
    @MethodSource("irregularDiscAgeProvider")
    @DisplayName("Test parsing irregular discipline age tokens")
    void testGetDisciplineAgeModelByToken(String token, DisciplineAgeModel expected) {
        DisciplineAgeModel result = parser.getDisciplineAgeModelByToken(token);
        assertEquals(expected, result);
    }

    @ParameterizedTest(name = "Parse header element token: {0}")
    @CsvSource({
            "Konkurrenz: JD U15,DOUBLE,U15",
            "Konkurrenz: ME U19,SINGLE,U19",
            "Konkurrenz: DE U19,SINGLE,U19",
            "Konkurrenz: MX U17 A,MIXED,U17",
            "Konkurrenz: U13 JD,DOUBLE,U13",
            "Konkurrenz: Mixed Doubles,MIXED,UOX",
            "Konkurrenz: JD-U15 A,DOUBLE,U15",
            "Konkurrenz: U13MX,MIXED,U13",
            "Konkurrenz: U11 [SG] Mädcheneinzel Samstag,SINGLE,U11",
            "Konkurrenz: U11 Mädcheneinzel Samstag,SINGLE,U11",
            "Konkurrenz: DD,DOUBLE,UOX",
    })
    @DisplayName("Test discipline token from HTMLElement header split result")
    void testDisciplineTokenSplitResult(String token, DisciplineType expectedDisciplineType, AgeClass expectedAgeClass) {
        var result = parser.parseDisciplineInfos(token);
        assertThat(result.getDiscipline()).isEqualTo(expectedDisciplineType);
        assertThat(result.getAgeClass()).isEqualTo(expectedAgeClass);
    }*/
}
