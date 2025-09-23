package de.ostfale.qk.domain.points;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Test Points record")
@Tag("unittest")
class RankingPointTest {

    @Test
    @DisplayName("Test creation of Points record")
    void shouldCreatePointsRecord() {
        // given
        var pointsValue = 42;

        // when
        var points = new RankingPoint(pointsValue, "67");

        // then
        assertThat(points.value()).isEqualTo(pointsValue);
    }
}
