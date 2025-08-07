package de.ostfale.qk.parser.web.match;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Test MatchPlayers record")
@Tag("unittest")
class MatchPlayersTest {

    private static final String FIRST_PLAYER = "Victoria Braun";
    private static final String SECOND_PLAYER = "Emily Bischoff";
    private static final String THIRD_PLAYER = "Isabelle Deng";
    private static final String FOURTH_PLAYER = "Mira Sun";

    @Test
    @DisplayName("For a single match the comparison should never be equal")
    void testIdenticalMatchPlayersWithNullSecondPlayer() {
        // given
        var matchPlayers1 = new MatchPlayers(FIRST_PLAYER, null);
        var matchPlayers2 = new MatchPlayers(SECOND_PLAYER, null);

        // when
        boolean result = matchPlayers1.equals(matchPlayers2);

        // then
        assertThat(result).isFalse();
        assertThat(matchPlayers1.isSingleMatch()).isTrue();
        assertThat(matchPlayers2.isSingleMatch()).isTrue();
    }

    @Test
    @DisplayName("When comparing MatchPlayers with one null second player they should not be equal")
    void testDifferentMatchPlayersWithOneNullSecondPlayer() {
        // given
        var matchPlayers1 = new MatchPlayers(FIRST_PLAYER, SECOND_PLAYER);
        var matchPlayers2 = new MatchPlayers(THIRD_PLAYER, FOURTH_PLAYER);

        // when
        boolean result = matchPlayers1.equals(matchPlayers2);

        // then
        assertThat(result).isFalse();
        assertThat(matchPlayers1.isSingleMatch()).isFalse();
        assertThat(matchPlayers2.isSingleMatch()).isFalse();
    }
}
