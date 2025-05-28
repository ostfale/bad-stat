package de.ostfale.qk.data.player.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Test FavPlayerYearStat record")
@Tag("unittest")
class FavPlayerYearStatTest {

    @Test
    @DisplayName("Should create FavPlayerYearStat with valid values")
    void shouldCreateFavPlayerYearStatWithValidValues() {
        // Given
        int year = 2024;
        int played = 10;
        int loaded = 5;

        // When
        FavPlayerYearStat yearStat = new FavPlayerYearStat(year, played, loaded);

        // Then
        assertThat(yearStat.year()).isEqualTo(2024);
        assertThat(yearStat.played()).isEqualTo(10);
        assertThat(yearStat.loaded()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should be equal when all fields match")
    void shouldBeEqualWhenAllFieldsMatch() {
        // Given
        FavPlayerYearStat stat1 = new FavPlayerYearStat(2024, 10, 5);
        FavPlayerYearStat stat2 = new FavPlayerYearStat(2024, 10, 5);

        // Then
        assertThat(stat1)
                .isEqualTo(stat2)
                .hasSameHashCodeAs(stat2);
    }

    @Test
    @DisplayName("Should not be equal when fields differ")
    void shouldNotBeEqualWhenFieldsDiffer() {
        // Given
        FavPlayerYearStat stat1 = new FavPlayerYearStat(2024, 10, 5);
        FavPlayerYearStat stat2 = new FavPlayerYearStat(2024, 10, 6);

        // Then
        assertThat(stat1).isNotEqualTo(stat2);
    }

    @Test
    @DisplayName("Should have correct string representation")
    void shouldHaveCorrectStringRepresentation() {
        // Given
        FavPlayerYearStat stat = new FavPlayerYearStat(2024, 10, 5);

        // When
        String result = stat.toString();

        // Then
        assertThat(result)
                .contains("year=2024")
                .contains("played=10")
                .contains("loaded=5");
    }
}