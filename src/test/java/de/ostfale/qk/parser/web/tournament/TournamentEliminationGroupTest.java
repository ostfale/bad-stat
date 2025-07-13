package de.ostfale.qk.parser.web.tournament;


import de.ostfale.qk.parser.BaseParserTest;
import de.ostfale.qk.parser.HtmlParserException;
import jakarta.inject.Inject;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Tag("unittest")
@DisplayName("Test tournament with elimination and  group matches for female single discipline")
public class TournamentEliminationGroupTest extends BaseParserTest {

    private static final String TEST_FILE_NAME = "tournaments/TournamentSingleWithGroup.html";

    @Inject
    WebTournamentParserService sut;

    @BeforeEach
    void setUp() {
        sut = prepareWebTournamentParser();
        content = loadHtmlPage(TEST_FILE_NAME);
    }

    @Test
    @DisplayName("Test parsing a single tournament")
    void testParseAllTournaments() throws HtmlParserException {
        // given
        var expectedNumberOfTournaments = 1;

        // when
        var result = sut.parseAllYearlyTournamentsForPlayer(content);

        // then
        AssertionsForClassTypes.assertThat(result.size()).isEqualTo(expectedNumberOfTournaments);
    }

    @Test
    @DisplayName("Test tournament header information")
    void testParseTournamentInfo() throws HtmlParserException {
        // given
        var expectedTournamentName = "VICTOR International Junior Cup (HAM, U09-U19, U22) 2025";
        var expectedOrganisation = "HAM - TSG Bergedorf";
        var expectedLocation = "Hamburg";
        var expectedDate = "07.06.2025 bis 09.06.2025";
        var expectedYear = 2025;

        // when
        var result = sut.parseAllYearlyTournamentsForPlayer(content);
        var tInfo = result.getFirst().getTournamentInfo();

        // then
        assertAll("Test content of tournament info header",
                () -> assertThat(tInfo.tournamentName()).isEqualTo(expectedTournamentName),
                () -> assertThat(tInfo.tournamentOrganizer()).isEqualTo(expectedOrganisation),
                () -> assertThat(tInfo.tournamentLocation()).isEqualTo(expectedLocation),
                () -> assertThat(tInfo.tournamentDate()).isEqualTo(expectedDate),
                () -> assertThat(tInfo.tournamentYear()).isEqualTo(expectedYear)
        );
    }
}
