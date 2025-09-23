package de.ostfale.qk.domain.points;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test PointsTourType enum")
@Tag( "unittest")
class RankingPointTourTypeTest {

    /**
     * Tests for the PointsTourType enum method `fromDisplayName`.
     * The method resolves a PointsTourType based on its display name,
     * performing a case-insensitive search.
     */

    @Test
    void fromDisplayName_ShouldReturnCorrectEnum_WhenValidDisplayNameProvided() {
        // Given
        String displayName = "E-RLT";

        // When
        Optional<PointsTourType> result = PointsTourType.fromDisplayName(displayName);

        // Then
        assertTrue(result.isPresent());
        assertEquals(PointsTourType.E_RLT, result.get());
    }

    @Test
    void fromDisplayName_ShouldReturnCorrectEnum_WhenDisplayNameInLowerCase() {
        // Given
        String displayName = "e-rlt";

        // When
        Optional<PointsTourType> result = PointsTourType.fromDisplayName(displayName);

        // Then
        assertTrue(result.isPresent());
        assertEquals(PointsTourType.E_RLT, result.get());
    }

    @Test
    void fromDisplayName_ShouldReturnCorrectEnum_WhenDisplayNameInMixedCase() {
        // Given
        String displayName = "E-rLt";

        // When
        Optional<PointsTourType> result = PointsTourType.fromDisplayName(displayName);

        // Then
        assertTrue(result.isPresent());
        assertEquals(PointsTourType.E_RLT, result.get());
    }

    @Test
    void fromDisplayName_ShouldReturnEmptyOptional_WhenUnknownDisplayNameProvided() {
        // Given
        String displayName = "UNKNOWN-TYPE";

        // When
        Optional<PointsTourType> result = PointsTourType.fromDisplayName(displayName);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void fromDisplayName_ShouldReturnEmptyOptional_WhenNullProvided() {
        // Given
        String displayName = null;

        // When
        Optional<PointsTourType> result = PointsTourType.fromDisplayName(displayName);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void fromDisplayName_ShouldReturnEmptyOptional_WhenEmptyStringProvided() {
        // Given
        String displayName = "";

        // When
        Optional<PointsTourType> result = PointsTourType.fromDisplayName(displayName);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void fromDisplayName_ShouldReturnEmptyOptional_WhenWhitespaceProvided() {
        // Given
        String displayName = "   ";

        // When
        Optional<PointsTourType> result = PointsTourType.fromDisplayName(displayName);

        // Then
        assertFalse(result.isPresent());
    }
}
