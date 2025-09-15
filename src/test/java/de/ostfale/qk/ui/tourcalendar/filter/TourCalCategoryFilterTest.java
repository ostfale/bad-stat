package de.ostfale.qk.ui.tourcalendar.filter;

import de.ostfale.qk.domain.tourcal.TourCategory;
import de.ostfale.qk.parser.BaseTournamentTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Test TourCalCategoryFilter")
@Tag("unittest")
class TourCalCategoryFilterTest extends BaseTournamentTest {

    private TourCalCategoryFilter sut;

    @BeforeEach
    void setUp() {
        sut = new TourCalCategoryFilter();
    }

    @ParameterizedTest(name = "For Category {0} should be {1} tournaments")
    @MethodSource("provideLookupTestCases")
    @DisplayName("Filter a list of planned tournaments b")
    void test(TourCategory category, int expectedNumberOfTournaments) {
        // given
        var allTournaments = createPlannedTournamentsWithAllCategories();
        sut.setCheckedAgeClasses(Set.of(category));

        // when then
        assertThat(sut.filterTournaments(allTournaments).size()).isEqualTo(expectedNumberOfTournaments);
    }

    private static Stream<Arguments> provideLookupTestCases() {
        return Stream.of(
                Arguments.of(TourCategory.A, 1),
                Arguments.of(TourCategory.B, 1),
                Arguments.of(TourCategory.BEC, 3),
                Arguments.of(TourCategory.BWF, 1),
                Arguments.of(TourCategory.C, 4),
                Arguments.of(TourCategory.D, 4),
                Arguments.of(TourCategory.E, 1)
        );
    }
}
