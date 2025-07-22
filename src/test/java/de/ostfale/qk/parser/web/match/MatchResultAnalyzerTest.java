package de.ostfale.qk.parser.web.match;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Tag("unittest")
@DisplayName("Test match result analyzer for different match scenario")
class MatchResultAnalyzerTest {

    private MatchResultAnalyzer sut;

    @Test
    @DisplayName("Test analyzing single standard match ")
    void testSingleStandardMatchLastWins() {
        // given
        String[] testData = new String[]{"Nils Barion", "Frederik Volkert", "W", "12", "21", "10", "21", "H2H"};
        var expectedMarkerPosition = 2;
        var expectedMatchScores = List.of(12, 21, 10, 21);
        var expectedMarker = "W";
        List<String> expectedPlayerNames = List.of("Nils Barion", "Frederik Volkert");

        // when
        sut = new MatchResultAnalyzer(testData);

        // then
        assertAll("Test analyzed single match data",
                () -> assertThat(sut.isByeMatch()).isFalse(),
                () -> assertThat(sut.getMarkerPosition("W")).isEqualTo(expectedMarkerPosition),
                () -> assertThat(sut.getMatchResultScores()).isEqualTo(expectedMatchScores),
                () -> assertThat(sut.getMarker()).isEqualTo(expectedMarker),
                () -> assertThat(sut.getPlayerNames()).isEqualTo(expectedPlayerNames)
        );
    }

    @Test
    @DisplayName("Test analyzing single standard match ")
    void testSingleStandardMatchFirstWins() {
        // given
        String[] testData = new String[]{"Victoria Braun", "W", "Elina Uhlmann", "21", "10", "21", "8", "H2H"};
        var expectedMarkerPosition = 1;
        var expectedMatchScores = List.of(21, 10, 21, 8);
        var expectedMarker = "W";
        List<String> expectedPlayerNames = List.of("Victoria Braun", "Elina Uhlmann");

        // when
        sut = new MatchResultAnalyzer(testData);

        // then
        assertAll("Test analyzed single match data",
                () -> assertThat(sut.isByeMatch()).isFalse(),
                () -> assertThat(sut.getMarkerPosition("W")).isEqualTo(expectedMarkerPosition),
                () -> assertThat(sut.getMatchResultScores()).isEqualTo(expectedMatchScores),
                () -> assertThat(sut.getMarker()).isEqualTo(expectedMarker),
                () -> assertThat(sut.getPlayerNames()).isEqualTo(expectedPlayerNames)
        );
    }

    @Test
    @DisplayName("Test analyzing single bye match first wins")
    void testSingleStandardMatchByeFirst() {
        // given
        String[] testData = new String[]{"Victoria Braun", "W", "Rast"};
        var expectedMarkerPosition = 1;
        var expectedMarker = "W";
        List<String> expectedPlayerNames = List.of("Victoria Braun");

        // when
        sut = new MatchResultAnalyzer(testData);

        // then
        assertAll("Test analyzed single match data",
                () -> assertThat(sut.isByeMatch()).isTrue(),
                () -> assertThat(sut.getMarkerPosition("W")).isEqualTo(expectedMarkerPosition),
                () -> assertThat(sut.getMatchResultScores().size()).isEqualTo(0),
                () -> assertThat(sut.getMarker()).isEqualTo(expectedMarker),
                () -> assertThat(sut.getPlayerNames()).isEqualTo(expectedPlayerNames)
        );
    }

    @Test
    @DisplayName("Test analyzing single bye match last wins")
    void testSingleStandardMatchByeLast() {
        // given
        String[] testData = new String[]{"Rast", "Frederik Volkert", "W"};
        var expectedMarkerPosition = 2;
        var expectedMarker = "W";
        List<String> expectedPlayerNames = List.of("Frederik Volkert");

        // when
        sut = new MatchResultAnalyzer(testData);

        // then
        assertAll("Test analyzed single match data",
                () -> assertThat(sut.isByeMatch()).isTrue(),
                () -> assertThat(sut.getMarkerPosition("W")).isEqualTo(expectedMarkerPosition),
                () -> assertThat(sut.getMatchResultScores().size()).isEqualTo(0),
                () -> assertThat(sut.getMarker()).isEqualTo(expectedMarker),
                () -> assertThat(sut.getPlayerNames()).isEqualTo(expectedPlayerNames)
        );
    }

    @Test
    @DisplayName("Test analyzing double standard match winner first")
    void testStandardDoubleMatchWinnerFirst() {
        // given
        String[] testData = new String[]{"Emily Bischoff", "Victoria Braun", "L", "Sidonie Krüger", "Miya-Melayn Salaria", "18", "21", "16", "21", "H2H"};
        var expectedMarkerPosition = 2;
        var expectedMatchScores = List.of(18, 21, 16, 21);
        var expectedMarker = "L";
        List<String> expectedPlayerNames = List.of("Emily Bischoff", "Victoria Braun", "Sidonie Krüger", "Miya-Melayn Salaria");

        // when
        sut = new MatchResultAnalyzer(testData);

        // then
        assertAll("Test analyzed single match data",
                () -> assertThat(sut.isByeMatch()).isFalse(),
                () -> assertThat(sut.getMarkerPosition("L")).isEqualTo(expectedMarkerPosition),
                () -> assertThat(sut.getMatchResultScores()).isEqualTo(expectedMatchScores),
                () -> assertThat(sut.getMarker()).isEqualTo(expectedMarker),
                () -> assertThat(sut.getPlayerNames()).isEqualTo(expectedPlayerNames)
        );
    }

    @Test
    @DisplayName("Test analyzing double standard match winner last")
    void testStandardDoubleMatchWinnerLast() {
        // given
        String[] testData = new String[]{"Yara Metzlaff", "Wilhelmine Witthus", "Emily Bischoff", "Victoria Braun", "W", "13", "21", "21", "19", "15", "21", "H2H"};
        var expectedMarkerPosition = 4;
        var expectedMatchScores = List.of(13, 21, 21, 19, 15, 21);
        var expectedMarker = "W";
        List<String> expectedPlayerNames = List.of("Yara Metzlaff", "Wilhelmine Witthus", "Emily Bischoff", "Victoria Braun");

        // when
        sut = new MatchResultAnalyzer(testData);

        // then
        assertAll("Test analyzed single match data",
                () -> assertThat(sut.isByeMatch()).isFalse(),
                () -> assertThat(sut.getMarkerPosition("W")).isEqualTo(expectedMarkerPosition),
                () -> assertThat(sut.getMatchResultScores()).isEqualTo(expectedMatchScores),
                () -> assertThat(sut.getMarker()).isEqualTo(expectedMarker),
                () -> assertThat(sut.getPlayerNames()).isEqualTo(expectedPlayerNames)
        );
    }

    @Test
    @DisplayName("Test analyzing double bye match winner last")
    void testStandardDoubleByeMatchWinnerLast() {
        // given
        String[] testData = new String[]{"Rast", "Emily Bischoff", "Victoria Braun", "W"};
        var expectedMarkerPosition = 3;
        var expectedMarker = "W";
        List<String> expectedPlayerNames = List.of("Emily Bischoff", "Victoria Braun");

        // when
        sut = new MatchResultAnalyzer(testData);

        // then
        assertAll("Test analyzed single match data",
                () -> assertThat(sut.isByeMatch()).isTrue(),
                () -> assertThat(sut.getMarkerPosition("W")).isEqualTo(expectedMarkerPosition),
                () -> assertThat(sut.getMatchResultScores().isEmpty()).isTrue(),
                () -> assertThat(sut.getMarker()).isEqualTo(expectedMarker),
                () -> assertThat(sut.getPlayerNames()).isEqualTo(expectedPlayerNames)
        );
    }

    @Test
    @DisplayName("Test analyzing mixed bye match winner first")
    void testStandardMixedByeMatchWinnerFirst() {
        // given
        String[] testData = new String[]{"Aarav Bhatia", "Victoria Braun", "W", "Rast"};
        var expectedMarkerPosition = 2;
        var expectedMarker = "W";
        List<String> expectedPlayerNames = List.of("Aarav Bhatia", "Victoria Braun");

        // when
        sut = new MatchResultAnalyzer(testData);

        // then
        assertAll("Test analyzed single match data",
                () -> assertThat(sut.isByeMatch()).isTrue(),
                () -> assertThat(sut.getMarkerPosition("W")).isEqualTo(expectedMarkerPosition),
                () -> assertThat(sut.getMatchResultScores().isEmpty()).isTrue(),
                () -> assertThat(sut.getMarker()).isEqualTo(expectedMarker),
                () -> assertThat(sut.getPlayerNames()).isEqualTo(expectedPlayerNames)
        );
    }
}
