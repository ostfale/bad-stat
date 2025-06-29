package de.ostfale.qk.parser.set;

import de.ostfale.qk.parser.BaseParserTest;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Tag("unit")
@DisplayName("Test match set results")
class MatchSetParserTest extends BaseParserTest {

    private MatchSetParserService sut;

    @BeforeEach
    void setUp() {
        sut = new MatchSetParserService();
    }

    @Test
    @DisplayName("Test set data extraction for match with standard sets")
    void testParseMatchSetsForStandardMatchSets() {
        // given
        String testFileName = "matches/SingleMatchStandard.html";
        HtmlPage page = loadHtmlPage(testFileName);

        // when
        var result = sut.parseMatchSets(page.getActiveElement());

        // then
        assertAll("Verify parsed match set results",
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.size()).isEqualTo(2),
                () -> assertThat(result.getFirst().getSetNumber()).isEqualTo(SetNo.FIRST),
                () -> assertThat(result.getLast().getSetNumber()).isEqualTo(SetNo.SECOND),
                () -> assertThat(result.getFirst().getFirstValue()).isEqualTo(21),
                () -> assertThat(result.getFirst().getSecondValue()).isEqualTo(10),
                () -> assertThat(result.getLast().getFirstValue()).isEqualTo(21),
                () -> assertThat(result.getLast().getSecondValue()).isEqualTo(8),
                () -> assertThat(result.getFirst().isRegularSet()).isTrue(),
                () -> assertThat(result.getLast().isRegularSet()).isTrue()
        );
    }

    @Test
    @DisplayName("Test set data extraction with a Rast set")
    void testParseMatchSetsWithRast() {
        // given
        String testFileName = "matches/SingleMatchRast.html";
        HtmlPage page = loadHtmlPage(testFileName);

        var expectedSetResult = "Rast";

        // when
        var result = sut.parseMatchSets(page.getActiveElement());

        // then
        assertAll("Test set data extraction for set with Rast",
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.size()).isEqualTo(1),
                () -> assertThat(result.getFirst().isRegularSet()).isFalse(),
                () -> assertThat(result.getFirst().getNonRegularResult()).isEqualTo(expectedSetResult)
        );
    }
}
