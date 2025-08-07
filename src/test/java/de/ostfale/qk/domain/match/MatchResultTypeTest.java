package de.ostfale.qk.domain.match;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Test match result type lookup")
class MatchResultTypeTest {

    @ParameterizedTest(name = "Test lookup of match result type for input: {0} should be: {1}")
    @MethodSource("provideLookupTestCases")
    @DisplayName("Test lookup of match result type")
    void testLookup(String input, MatchResultType expected) {
        assertThat(MatchResultType.lookup(input)).isEqualTo(expected);
    }

    private static Stream<Arguments> provideLookupTestCases() {
        return Stream.of(
                Arguments.of("Default", MatchResultType.REGULAR),
                Arguments.of("Rast", MatchResultType.BYE),
                Arguments.of("Walkover", MatchResultType.WALKOVER),
                Arguments.of("Walkover L", MatchResultType.WALKOVER),
                Arguments.of("Retired", MatchResultType.RETIRED),
                Arguments.of("Retired.", MatchResultType.RETIRED),
                Arguments.of("Retired L", MatchResultType.RETIRED),
                Arguments.of("Retired. L", MatchResultType.RETIRED)
        );
    }
}
