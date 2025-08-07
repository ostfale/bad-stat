package de.ostfale.qk.parser.web.tournament;

import de.ostfale.qk.parser.BaseParserTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Tag("unittest")
@DisplayName("Test tournament parser for pure elimination")
public class TournamentEliminationTest extends BaseParserTest {

    private static final String TEST_FILE_NAME = "tournaments/TournamentEliminationVB25.html";

    @Inject
    WebTournamentParserService sut;

    @BeforeEach
    void setUp() {
        sut = prepareWebTournamentParser();
        content = loadHtmlPage(TEST_FILE_NAME);
    }

    @Test
    @DisplayName("Test parsing a single tournament")
    void testParseAllTournaments() {
        // given
        var expectedNumberOfTournaments = 1;
        var playerName = "Victoria Braun";

        // when
        var result = sut.parseAllYearlyTournamentsForPlayer(playerName, content);

        // then
        assertThat(result.size()).isEqualTo(expectedNumberOfTournaments);
    }

    @Test
    @DisplayName("Test tournament header information")
    void testParseTournamentInfo() {
        // given
        var playerName = "Victoria Braun";
        var expectedTournamentName = "3. DBV A-RLT U15 und U17 Maintal 2025";
        var expectedOrganisation = "Deutscher Badminton Verband (U19)";
        var expectedLocation = "Maintal";
        var expectedDate = "12.04.2025 bis 13.04.2025";
        var expectedYear = 2025;

        // when
        var result = sut.parseAllYearlyTournamentsForPlayer(playerName, content).getFirst().getTournamentInfo();

        // then
        assertAll("Test content of tournament info header",
                () -> assertThat(result.tournamentName()).isEqualTo(expectedTournamentName),
                () -> assertThat(result.tournamentOrganizer()).isEqualTo(expectedOrganisation),
                () -> assertThat(result.tournamentLocation()).isEqualTo(expectedLocation),
                () -> assertThat(result.tournamentDate()).isEqualTo(expectedDate),
                () -> assertThat(result.tournamentYear()).isEqualTo(expectedYear)
        );
    }

    @Test
    @DisplayName("Test parsing a single match")
    void testParseSingleMatch() {
        // given
        var playerName = "Victoria Braun";
        var expectedRoundNames = List.of("Round of 32", "Round of 16");

        // when
        var result = sut.parseAllYearlyTournamentsForPlayer(playerName, content);
        var tournament = result.getFirst();
        var singleDiscipline = tournament.getSingleDiscipline();

        // then
        assertAll("Test single tournament results",
                () -> assertThat(singleDiscipline.hasGroupMatches()).isFalse(),
                () -> assertThat(singleDiscipline.hasEliminationMatches()).isTrue(),
                () -> assertThat(singleDiscipline.getEliminationMatches().size()).isEqualTo(2),
                () -> assertThat(singleDiscipline.getEliminationMatches().getFirst().getRoundName()).isEqualTo(expectedRoundNames.getFirst()),
                () -> assertThat(singleDiscipline.getEliminationMatches().get(1).getRoundName()).isEqualTo(expectedRoundNames.get(1))

        );
    }

    @Test
    @DisplayName("Test parsing a double match")
    void testParseDoubleMatch() {
        // given
        var playerName = "Victoria Braun";
        var expectedRoundNames = List.of("Round of 16", "Quarter final");

        // when
        var result = sut.parseAllYearlyTournamentsForPlayer(playerName, content);
        var tournament = result.getFirst();
        var doubleDiscipline = tournament.getDoubleDiscipline();

        // then
        assertAll("Test single tournament results",
                () -> assertThat(doubleDiscipline.hasGroupMatches()).isFalse(),
                () -> assertThat(doubleDiscipline.hasEliminationMatches()).isTrue(),
                () -> assertThat(doubleDiscipline.getEliminationMatches().size()).isEqualTo(2),
                () -> assertThat(doubleDiscipline.getEliminationMatches().getFirst().getRoundName()).isEqualTo(expectedRoundNames.getFirst()),
                () -> assertThat(doubleDiscipline.getEliminationMatches().get(1).getRoundName()).isEqualTo(expectedRoundNames.get(1))

        );
    }

    @Test
    @DisplayName("Test parsing a mixed match")
    void testParseMixedMatch() {
        // given
        var playerName = "Victoria Braun";
        var expectedRoundNames = List.of("Round of 32", "Round of 16", "Quarter final");

        // when
        var result = sut.parseAllYearlyTournamentsForPlayer(playerName, content);
        var tournament = result.getFirst();
        var mixedDiscipline = tournament.getMixedDiscipline();

        // then
        assertAll("Test single tournament results",
                () -> assertThat(mixedDiscipline.hasGroupMatches()).isFalse(),
                () -> assertThat(mixedDiscipline.hasEliminationMatches()).isTrue(),
                () -> assertThat(mixedDiscipline.getEliminationMatches().size()).isEqualTo(3),
                () -> assertThat(mixedDiscipline.getEliminationMatches().getFirst().getRoundName()).isEqualTo(expectedRoundNames.getFirst()),
                () -> assertThat(mixedDiscipline.getEliminationMatches().get(1).getRoundName()).isEqualTo(expectedRoundNames.get(1)),
                () -> assertThat(mixedDiscipline.getEliminationMatches().get(2).getRoundName()).isEqualTo(expectedRoundNames.get(2))
        );
    }
}
