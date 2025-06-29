package de.ostfale.qk.parser.web.tournament;

import de.ostfale.qk.domain.discipline.DoubleDiscipline;
import de.ostfale.qk.domain.discipline.MixedDiscipline;
import de.ostfale.qk.domain.discipline.SingleDiscipline;
import de.ostfale.qk.parser.BaseParserTest;
import de.ostfale.qk.parser.HtmlParserException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@QuarkusTest
@DisplayName("Test tournament parser for pure elimination")
public class TournamentEliminationTest extends BaseParserTest {

    private static final String TEST_FILE_NAME = "tournaments/TournamentEliminationVB25.html";

    @Inject
    WebTournamentParserService sut;

    private HtmlElement content;

    @BeforeEach
    void setUp() {
        HtmlPage page = loadHtmlPage(TEST_FILE_NAME);
        content = page.getActiveElement();
    }

    @Test
    @DisplayName("Test parsing a single tournament")
    void testParseAllTournaments() throws HtmlParserException {
        // given
        var expectedNumberOfTournaments = 1;

        // when
        var result = sut.parseAllYearlyTournamentsForPlayer(content);

        // then
        assertThat(result.size()).isEqualTo(expectedNumberOfTournaments);
    }

    @Test
    @DisplayName("Test tournament header information")
    void testParseTournamentInfo() throws HtmlParserException {
        // given
        var expectedTournamentName = "3. DBV A-RLT U15 und U17 Maintal 2025";
        var expectedOrganisation = "Deutscher Badminton Verband (U19)";
        var expectedLocation = "Maintal";
        var expectedDate = "12.04.2025 bis 13.04.2025";
        var expectedYear = 2025;

        // when
        var result = sut.parseAllYearlyTournamentsForPlayer(content).getFirst().getTournamentInfo();

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
    void testParseSingleMatch() throws HtmlParserException {
        // given
        var expectedRoundNames = List.of("Round of 32", "Round of 16");

        // when
        var result = sut.parseAllYearlyTournamentsForPlayer(content);
        var tournament = result.getFirst();
        SingleDiscipline singleDiscipline = tournament.getSingleDiscipline();

        // then
        assertAll("Test single tournament results",
                () -> assertThat(singleDiscipline.hasMatches()).isTrue(),
                () -> assertThat(singleDiscipline.hasGroupMatches()).isFalse(),
                () -> assertThat(singleDiscipline.hasEliminationMatches()).isTrue(),
                () -> assertThat(singleDiscipline.getEliminationMatches().size()).isEqualTo(2),
                () -> assertThat(singleDiscipline.getEliminationMatches().getFirst().getRoundName()).isEqualTo(expectedRoundNames.getFirst()),
                () -> assertThat(singleDiscipline.getEliminationMatches().get(1).getRoundName()).isEqualTo(expectedRoundNames.get(1))

        );
    }

    @Test
    @DisplayName("Test parsing a double match")
    void testParseDoubleMatch() throws HtmlParserException {
        // given
        var expectedRoundNames = List.of("Round of 16", "Quarter final");

        // when
        var result = sut.parseAllYearlyTournamentsForPlayer(content);
        var tournament = result.getFirst();
        DoubleDiscipline doubleDiscipline = tournament.getDoubleDiscipline();

        // then
        assertAll("Test single tournament results",
                () -> assertThat(doubleDiscipline.hasMatches()).isTrue(),
                () -> assertThat(doubleDiscipline.hasGroupMatches()).isFalse(),
                () -> assertThat(doubleDiscipline.hasEliminationMatches()).isTrue(),
                () -> assertThat(doubleDiscipline.getEliminationMatches().size()).isEqualTo(2),
                () -> assertThat(doubleDiscipline.getEliminationMatches().getFirst().getRoundName()).isEqualTo(expectedRoundNames.getFirst()),
                () -> assertThat(doubleDiscipline.getEliminationMatches().get(1).getRoundName()).isEqualTo(expectedRoundNames.get(1))

        );
    }

    @Test
    @DisplayName("Test parsing a mixed match")
    void testParseMixedMatch() throws HtmlParserException {
        // given
        var expectedRoundNames = List.of("Round of 32", "Round of 16", "Quarter final");

        // when
        var result = sut.parseAllYearlyTournamentsForPlayer(content);
        var tournament = result.getFirst();
        MixedDiscipline mixedDiscipline = tournament.getMixedDiscipline();

        // then
        assertAll("Test single tournament results",
                () -> assertThat(mixedDiscipline.hasMatches()).isTrue(),
                () -> assertThat(mixedDiscipline.hasGroupMatches()).isFalse(),
                () -> assertThat(mixedDiscipline.hasEliminationMatches()).isTrue(),
                () -> assertThat(mixedDiscipline.getEliminationMatches().size()).isEqualTo(3),
                () -> assertThat(mixedDiscipline.getEliminationMatches().getFirst().getRoundName()).isEqualTo(expectedRoundNames.getFirst()),
                () -> assertThat(mixedDiscipline.getEliminationMatches().get(1).getRoundName()).isEqualTo(expectedRoundNames.get(1)),
                () -> assertThat(mixedDiscipline.getEliminationMatches().get(2).getRoundName()).isEqualTo(expectedRoundNames.get(2))
        );
    }
}
