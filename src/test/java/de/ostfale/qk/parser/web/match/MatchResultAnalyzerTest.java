package de.ostfale.qk.parser.web.match;

import de.ostfale.qk.domain.match.MatchResultType;
import de.ostfale.qk.parser.HtmlParserException;
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
    void testSingleStandardMatchLastWins() throws HtmlParserException {
        // given
        String[] testData = new String[]{"Nils Barion", "Frederik Volkert", "W", "12", "21", "10", "21", "H2H"};
        var expectedMatchElementList = List.of(testData);
        var expectedMarkerPosition = 2;
        var expectedMatchScores = List.of(12, 21, 10, 21);
        var expectedMarker = "W";
        List<String> expectedPlayerNames = List.of("Nils Barion", "Frederik Volkert");

        // when
        sut = new MatchResultAnalyzer(testData);

        // then
        assertAll("Test analyzed single match data",
                () -> assertThat(sut.isByeMatch()).isFalse(),
                () -> assertThat(sut.getMarkerPosition()).isEqualTo(expectedMarkerPosition),
                () -> assertThat(sut.getMatchResultScores()).isEqualTo(expectedMatchScores),
                () -> assertThat(sut.getMatchResultElements()).isEqualTo(testData),
                () -> assertThat(sut.getMatchResultElementList()).isEqualTo(expectedMatchElementList),
                () -> assertThat(sut.getMarker()).isEqualTo(expectedMarker),
                () -> assertThat(sut.getPlayerNames()).isEqualTo(expectedPlayerNames)
        );
    }

    @Test
    @DisplayName("Test analyzing single standard match ")
    void testSingleStandardMatchFirstWins() throws HtmlParserException {
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
                () -> assertThat(sut.getMatchResultScores()).isEqualTo(expectedMatchScores),
                () -> assertThat(sut.getMarker()).isEqualTo(expectedMarker),
                () -> assertThat(sut.getPlayerNames()).isEqualTo(expectedPlayerNames)
        );
    }

    @Test
    @DisplayName("Test analyzing single bye match first wins")
    void testSingleStandardMatchByeFirst() throws HtmlParserException {
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
                () -> assertThat(sut.getMatchResultScores().size()).isEqualTo(0),
                () -> assertThat(sut.getMarker()).isEqualTo(expectedMarker),
                () -> assertThat(sut.getPlayerNames()).isEqualTo(expectedPlayerNames)
        );
    }

    @Test
    @DisplayName("Test analyzing single bye match with 'Kein Spiel' ")
    void testSingleByeMatchWithNoGame() throws HtmlParserException {
        // given
        String[] testData = new String[]{"Rast", "Kein Spiel", "Max Hahn", "W"};
        var expectedMarkerPosition = 3;
        var expectedMarker = "W";
        List<String> expectedPlayerNames = List.of("Max Hahn");

        // when
        sut = new MatchResultAnalyzer(testData);

        // then
        assertAll("Test analyzed single match data",
                () -> assertThat(sut.isByeMatch()).isTrue(),
                () -> assertThat(sut.getMatchResultScores().size()).isEqualTo(0),
                () -> assertThat(sut.getMarker()).isEqualTo(expectedMarker),
                () -> assertThat(sut.getPlayerNames()).isEqualTo(expectedPlayerNames)
        );
    }

    @Test
    @DisplayName("Test analyzing single bye match last wins")
    void testSingleStandardMatchByeLast() throws HtmlParserException {
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
                () -> assertThat(sut.getMatchResultScores().size()).isEqualTo(0),
                () -> assertThat(sut.getMarker()).isEqualTo(expectedMarker),
                () -> assertThat(sut.getPlayerNames()).isEqualTo(expectedPlayerNames)
        );
    }

    @Test
    @DisplayName("Test analyzing double standard match winner first")
    void testStandardDoubleMatchWinnerFirst() throws HtmlParserException {
        // given
        String[] testData = new String[]{"Emily Bischoff", "Victoria Braun", "L", "Sidonie Krüger", "Miya-Melayn Salaria", "18", "21", "16", "21", "H2H"};
        var expectedMarkerPosition = 2;
        var expectedMatchScores = List.of(18, 21, 16, 21);
        var expectedMarker = "L";
        var expectedMatchResultType = MatchResultType.REGULAR.getDisplayName();
        List<String> expectedPlayerNames = List.of("Emily Bischoff", "Victoria Braun", "Sidonie Krüger", "Miya-Melayn Salaria");

        // when
        sut = new MatchResultAnalyzer(testData);

        // then
        assertAll("Test analyzed single match data",
                () -> assertThat(sut.isByeMatch()).isFalse(),
                () -> assertThat(sut.getMatchResultScores()).isEqualTo(expectedMatchScores),
                () -> assertThat(sut.getMarker()).isEqualTo(expectedMarker),
                () -> assertThat(sut.getMarkerPosition()).isEqualTo(expectedMarkerPosition),
                () -> assertThat(sut.getMatchResultType()).isEqualTo(expectedMatchResultType),
                () -> assertThat(sut.getPlayerNames()).isEqualTo(expectedPlayerNames)
        );
    }

    @Test
    @DisplayName("Test player name retrieval")
    void testPlayerNamesWithoutMarker() throws HtmlParserException {
        // given
        String[] testData = new String[]{"Emily Bischoff", "Victoria Braun", "L", "Sidonie Krüger", "Miya-Melayn Salaria", "18", "21", "16", "21", "H2H"};

        var expectedFirstPlayerName = "Emily Bischoff";
        var expectedSecondPlayerName = "Victoria Braun";
        var expectedThirdPlayerName = "Sidonie Krüger";
        var expectedFourthPlayerName = "Miya-Melayn Salaria";

        // when
        sut = new MatchResultAnalyzer(testData);

        // then
        assertAll("Test extracted player names",
                () -> assertThat(sut.getFirstPlayerName(false)).isEqualTo(expectedFirstPlayerName),
                () -> assertThat(sut.getSecondPlayerName(false)).isEqualTo(expectedSecondPlayerName),
                () -> assertThat(sut.getThirdPlayerName(false)).isEqualTo(expectedThirdPlayerName),
                () -> assertThat(sut.getFourthPlayerName(false)).isEqualTo(expectedFourthPlayerName)
        );
    }

    @Test
    @DisplayName("Test player name retrieval with marker")
    void testPlayerNamesWithMarker() throws HtmlParserException {
        // given
        String[] testData = new String[]{"Emily Bischoff", "Victoria Braun", "L", "Sidonie Krüger", "Miya-Melayn Salaria", "18", "21", "16", "21", "H2H"};

        var expectedFirstPlayerName = "Emily Bischoff (L)";
        var expectedSecondPlayerName = "Victoria Braun (L)";
        var expectedThirdPlayerName = "Sidonie Krüger (L)";
        var expectedFourthPlayerName = "Miya-Melayn Salaria (L)";

        // when
        sut = new MatchResultAnalyzer(testData);

        // then
        assertAll("Test extracted player names",
                () -> assertThat(sut.getFirstPlayerName(true)).isEqualTo(expectedFirstPlayerName),
                () -> assertThat(sut.getSecondPlayerName(true)).isEqualTo(expectedSecondPlayerName),
                () -> assertThat(sut.getThirdPlayerName(true)).isEqualTo(expectedThirdPlayerName),
                () -> assertThat(sut.getFourthPlayerName(true)).isEqualTo(expectedFourthPlayerName)
        );
    }

    @Test
    @DisplayName("Test analyzing double standard match winner last")
    void testStandardDoubleMatchWinnerLast() throws HtmlParserException {
        // given
        String[] testData = new String[]{"Yara Metzlaff", "Wilhelmine Witthus", "Emily Bischoff", "Victoria Braun", "W", "13", "21", "21", "19", "15", "21", "H2H"};
        var expectedMarkerPosition = 4;
        var expectedMatchScores = List.of(13, 21, 21, 19, 15, 21);
        var expectedMarker = "W";
        var expectedMatchResultType = MatchResultType.REGULAR.getDisplayName();
        List<String> expectedPlayerNames = List.of("Yara Metzlaff", "Wilhelmine Witthus", "Emily Bischoff", "Victoria Braun");

        // when
        sut = new MatchResultAnalyzer(testData);

        // then
        assertAll("Test analyzed single match data",
                () -> assertThat(sut.isByeMatch()).isFalse(),
                () -> assertThat(sut.getMatchResultScores()).isEqualTo(expectedMatchScores),
                () -> assertThat(sut.getMarker()).isEqualTo(expectedMarker),
                () -> assertThat(sut.getMarkerPosition()).isEqualTo(expectedMarkerPosition),
                () -> assertThat(sut.getMatchResultType()).isEqualTo(expectedMatchResultType),
                () -> assertThat(sut.getPlayerNames()).isEqualTo(expectedPlayerNames)
        );
    }

    @Test
    @DisplayName("Test analyzing double bye match winner last")
    void testStandardDoubleByeMatchWinnerLast() throws HtmlParserException {
        // given
        String[] testData = new String[]{"Rast", "Emily Bischoff", "Victoria Braun", "W"};
        var expectedMarkerPosition = 3;
        var expectedMarker = "W";
        var expectedMatchResultType = MatchResultType.BYE.getDisplayName();
        List<String> expectedPlayerNames = List.of("Emily Bischoff", "Victoria Braun");

        // when
        sut = new MatchResultAnalyzer(testData);

        // then
        assertAll("Test analyzed single match data",
                () -> assertThat(sut.isByeMatch()).isTrue(),
                () -> assertThat(sut.getMatchResultScores().isEmpty()).isTrue(),
                () -> assertThat(sut.getMarker()).isEqualTo(expectedMarker),
                () -> assertThat(sut.getMarkerPosition()).isEqualTo(expectedMarkerPosition),
                () -> assertThat(sut.getMatchResultType()).isEqualTo(expectedMatchResultType),
                () -> assertThat(sut.getPlayerNames()).isEqualTo(expectedPlayerNames)
        );
    }

    @Test
    @DisplayName("Test analyzing mixed bye match winner first")
    void testStandardMixedByeMatchWinnerFirst() throws HtmlParserException {
        // given
        String[] testData = new String[]{"Aarav Bhatia", "Victoria Braun", "W", "Rast"};
        var expectedMarkerPosition = 2;
        var expectedMarker = "W";
        var expectedMatchResultType = MatchResultType.BYE.getDisplayName();
        List<String> expectedPlayerNames = List.of("Aarav Bhatia", "Victoria Braun");

        // when
        sut = new MatchResultAnalyzer(testData);

        // then
        assertAll("Test analyzed single match data",
                () -> assertThat(sut.isByeMatch()).isTrue(),
                () -> assertThat(sut.getMatchResultScores().isEmpty()).isTrue(),
                () -> assertThat(sut.getMarker()).isEqualTo(expectedMarker),
                () -> assertThat(sut.getMarkerPosition()).isEqualTo(expectedMarkerPosition),
                () -> assertThat(sut.getMatchResultType()).isEqualTo(expectedMatchResultType),
                () -> assertThat(sut.getPlayerNames()).isEqualTo(expectedPlayerNames)
        );
    }

    @Test
    @DisplayName("Test analyzing single match with 'Walkover L' marker")
    void testSingleSpecialWalkoverL() throws HtmlParserException {
        // given
        String[] testData = new String[]{"Soheyl Safari Araghi", "Walkover L", "Shager Thangjam", "H2H"};
        var expectedMarkerPosition = 1;
        var expectedMarker = "L";
        var expectedMatchResultType = MatchResultType.WALKOVER.getDisplayName();

        List<String> expectedPlayerNames = List.of("Soheyl Safari Araghi", "Shager Thangjam");

        // when
        sut = new MatchResultAnalyzer(testData);

        // then
        assertAll("Test analyzed special single match data",
                () -> assertThat(sut.isWalkOverMatch()).isTrue(),
                () -> assertThat(sut.getMatchResultScores().isEmpty()).isTrue(),
                () -> assertThat(sut.getMarker()).isEqualTo(expectedMarker),
                () -> assertThat(sut.getMarkerPosition()).isEqualTo(expectedMarkerPosition),
                () -> assertThat(sut.getMatchResultType()).isEqualTo(expectedMatchResultType),
                () -> assertThat(sut.getPlayerNames()).isEqualTo(expectedPlayerNames)
        );
    }

    @Test
    @DisplayName("Test analyzing single match with 'Retired L' marker")
    void testSingleSpecialWalkoverLWithNumbersAfterName() throws HtmlParserException {
        // given
        String[] testData = new String[]{"Matti Richter", "Retired L", "Bruno Seichter", "13", "20", "H2H"};
        var expectedMarkerPosition = 1;
        var expectedMarker = "L";
        var expectedMatchResultType = MatchResultType.RETIRED.getDisplayName();
        List<String> expectedPlayerNames = List.of("Matti Richter", "Bruno Seichter");
        var expectedMatchScores = List.of(13, 20);

        // when
        sut = new MatchResultAnalyzer(testData);

        // then
        assertAll("Test analyzed special single match data",
                () -> assertThat(sut.isRetiredMatch()).isTrue(),
                () -> assertThat(sut.getMatchResultScores().isEmpty()).isFalse(),
                () -> assertThat(sut.getMarker()).isEqualTo(expectedMarker),
                () -> assertThat(sut.getMatchResultScores()).isEqualTo(expectedMatchScores),
                () -> assertThat(sut.getMarkerPosition()).isEqualTo(expectedMarkerPosition),
                () -> assertThat(sut.getMatchResultType()).isEqualTo(expectedMatchResultType),
                () -> assertThat(sut.getPlayerNames()).isEqualTo(expectedPlayerNames)
        );
    }

    @Test
    @DisplayName("Test analyzing single match where first player retired in set 2")
    void testSingleMatchFirstRetiredInSecondSet() throws HtmlParserException {
        // given
        String[] testData = new String[]{"Leo Hanxiang Luo", "Retired", "Soheyl Safari Araghi", "W", "19", "21", "5", "11", "H2H"};
        var expectedMarker = "W";
        var expectedMarkerPosition = 3;
        var expectedRetiredPosition = 1;
        List<String> expectedPlayerNames = List.of("Leo Hanxiang Luo", "Soheyl Safari Araghi");
        var expectedMatchResultType = MatchResultType.RETIRED.getDisplayName();
        var expectedMatchScores = List.of(19, 21, 5, 11);

        // when
        sut = new MatchResultAnalyzer(testData);

        // then
        assertAll("Test analyzed special single match data",
                () -> assertThat(sut.isRetiredMatch()).isTrue(),
                () -> assertThat(sut.getMatchResultScores().isEmpty()).isFalse(),
                () -> assertThat(sut.getMarker()).isEqualTo(expectedMarker),
                () -> assertThat(sut.getMarkerPosition()).isEqualTo(expectedMarkerPosition),
                () -> assertThat(sut.getPlayerNames()).isEqualTo(expectedPlayerNames),
                () -> assertThat(sut.getMatchResultType()).isEqualTo(expectedMatchResultType),
                () -> assertThat(sut.getMatchResultScores()).isEqualTo(expectedMatchScores),
                () -> assertThat(sut.getRetiredPosition()).isEqualTo(expectedRetiredPosition)
        );
    }

    @Test
    @DisplayName("Test analyzing single match where second player retired in set 2")
    void testSingleMatchSecondRetiredInSecondSet() throws HtmlParserException {
        // given
        String[] testData = new String[]{"Soheyl Safari Araghi", "W", "Hannes Merget", "Retired", "23", "21", "8", "8", "H2H"};
        var expectedMarker = "W";
        var expectedMarkerPosition = 1;
        var expectedRetiredPosition = 3;
        List<String> expectedPlayerNames = List.of("Soheyl Safari Araghi","Hannes Merget");
        var expectedMatchResultType = MatchResultType.RETIRED.getDisplayName();
        var expectedMatchScores = List.of(23, 21, 8, 8);

        // when
        sut = new MatchResultAnalyzer(testData);

        // then
        assertAll("Test analyzed special single match data",
                () -> assertThat(sut.isRetiredMatch()).isTrue(),
                () -> assertThat(sut.getMatchResultScores().isEmpty()).isFalse(),
                () -> assertThat(sut.getMarker()).isEqualTo(expectedMarker),
                () -> assertThat(sut.getMarkerPosition()).isEqualTo(expectedMarkerPosition),
                () -> assertThat(sut.getPlayerNames()).isEqualTo(expectedPlayerNames),
                () -> assertThat(sut.getMatchResultType()).isEqualTo(expectedMatchResultType),
                () -> assertThat(sut.getMatchResultScores()).isEqualTo(expectedMatchScores),
                () -> assertThat(sut.getRetiredPosition()).isEqualTo(expectedRetiredPosition)
        );
    }
}
