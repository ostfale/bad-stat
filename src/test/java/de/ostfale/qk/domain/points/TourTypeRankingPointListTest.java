package de.ostfale.qk.domain.points;

import de.ostfale.qk.domain.discipline.AgeClass;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test TourTypePointsList record")
@Tag("unittest")
class TourTypeRankingPointListTest {

    @Test
    @DisplayName("Given valid inputs when creating record then fields are accessible and correct")
    void givenValidInputs_whenCreatingRecord_thenFieldsAreCorrect() {
        // Given
        AgeClass ageClass = AgeClass.U13; // adjust to an existing enum constant
        PointsTourType tourType = PointsTourType.E_RLT; // adjust to an existing enum constant
        RankingPoint p1 = new RankingPoint(1, "67");
        RankingPoint p2 = new RankingPoint(2, "60");
        List<RankingPoint> points = List.of(p1, p2);

        // When
        TourTypePointsList dto = new TourTypePointsList(ageClass, tourType, points);

        // Then
        assertAll(
                () -> assertEquals(ageClass, dto.ageClass()),
                () -> assertEquals(tourType, dto.pointsTourType()),
                () -> assertEquals(points, dto.rankingPointList()),
                () -> assertEquals(2, dto.rankingPointList().size())
        );
    }

    @Test
    @DisplayName("Given same values when comparing instances then equals and hashCode match")
    void givenSameValues_whenComparing_thenEqualsAndHashCodeMatch() {
        // Given
        AgeClass ageClass = AgeClass.U13; // adjust if needed
        PointsTourType tourType = PointsTourType.E_RLT; // adjust if needed
        List<RankingPoint> points = List.of(new RankingPoint(1, "67"), new RankingPoint(2, "60"));

        // When
        TourTypePointsList a = new TourTypePointsList(ageClass, tourType, points);
        TourTypePointsList b = new TourTypePointsList(ageClass, tourType, points);

        // Then
        assertAll(
                () -> assertEquals(a, b),
                () -> assertEquals(a.hashCode(), b.hashCode()),
                () -> assertEquals(a.toString(), b.toString())
        );
    }

    @Test
    @DisplayName("Given record when trying to mutate internal list then external list remains unchanged")
    void givenRecord_whenMutatingExternalList_thenRecordRemainsUnchangedReference() {
        // Given
        AgeClass ageClass = AgeClass.U13; // adjust if needed
        PointsTourType tourType = PointsTourType.E_RLT; // adjust if needed
        List<RankingPoint> original = List.of(new RankingPoint(1, "67"));

        // When
        TourTypePointsList dto = new TourTypePointsList(ageClass, tourType, original);

        // Then
        assertThrows(UnsupportedOperationException.class, () -> dto.rankingPointList().add(new RankingPoint(2, "60")));
    }
}
