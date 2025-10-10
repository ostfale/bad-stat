package de.ostfale.qk.domain.pointstable;

import de.ostfale.qk.domain.discipline.AgeClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test AgeClassTablePoints record")
@Tag("unittest")
class AgeClassTablePointsTest {

    @Test
    @DisplayName("Should retrieve list of tournament type display values for given age class")
    void testGetTournamentTypesForAgeClass() {
        // given
        TourTypeTablePoints tourType1 = new TourTypeTablePoints(PointsDisplayTypes.E_RLT);
        TourTypeTablePoints tourType2 = new TourTypeTablePoints(PointsDisplayTypes.D1_BM);
        TourTypeTablePoints tourType3 = new TourTypeTablePoints(PointsDisplayTypes.C1_LM);

        Map<AgeClass, List<TourTypeTablePoints>> ageClassMap = Map.of(
                AgeClass.U15, List.of(tourType1, tourType2, tourType3)
        );

        AgeClassTablePoints ageClassTablePoints = new AgeClassTablePoints(ageClassMap);

        // when
        List<String> result = ageClassTablePoints.getTournamentTypesForAgeClass(AgeClass.U15);

        // then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("E-RLT", result.get(0));
        assertEquals("D1-BM", result.get(1));
        assertEquals("C1-LM", result.get(2));
    }

    @Test
    @DisplayName("Transpose point elements for a give age class in 'rows' ")
    void testTransposingPoints() {
        // given
        List<TourTypeTablePoints> pList = List.of(
                createTourTypeTablePoints(PointsDisplayTypes.E_RLT),
                createTourTypeTablePoints(PointsDisplayTypes.D1_BM),
                createTourTypeTablePoints(PointsDisplayTypes.C1_LM),
                createTourTypeTablePoints(PointsDisplayTypes.A_RLT));
        var ageClassTablePoints = new AgeClassTablePoints(Map.of(AgeClass.U17, pList));

        var expectedRowsSize = 4;
        var expectedFirstRow = List.of("1691", "3691", "6712", "22372");
        var expectedSecondRow = List.of("1522", "3322", "6041", "20135");
        var expectedThirdRow = List.of("1353", "2953", "5369", "17898");
        var expectedFourthRow = List.of("1184", "2584", "4698", "15661");


        // when
        var result = ageClassTablePoints.transposePointListsForAgeClass(AgeClass.U17);

        // then
        assertAll("Grouped assertions for transposing points function",
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.size()).isNotZero(),
                () -> assertThat(result.getFirst().size()).isEqualTo(expectedRowsSize),
                () -> assertThat(result.getFirst()).isEqualTo(expectedFirstRow),
                () -> assertThat(result.get(1)).isEqualTo(expectedSecondRow),
                () -> assertThat(result.get(2)).isEqualTo(expectedThirdRow),
                () -> assertThat(result.get(3)).isEqualTo(expectedFourthRow)
        );
    }

    private TourTypeTablePoints createTourTypeTablePoints(PointsDisplayTypes pointsDisplayTypes) {
        switch (pointsDisplayTypes) {
            case E_RLT -> {
                return new TourTypeTablePoints(PointsDisplayTypes.E_RLT, List.of("1691", "1522", "1353", "1184"));
            }
            case D1_BM -> {
                return new TourTypeTablePoints(PointsDisplayTypes.D1_BM, List.of("3691", "3322", "2953", "2584"));
            }
            case C1_LM -> {
                return new TourTypeTablePoints(PointsDisplayTypes.C1_LM, List.of("6712", "6041", "5369", "4698"));
            }
            case A_RLT -> {
                return new TourTypeTablePoints(PointsDisplayTypes.A_RLT, List.of("22372", "20135", "17898", "15661"));
            }
            default -> throw new IllegalArgumentException("Invalid points tour type");
        }
    }
}
