package de.ostfale.qk.parser.web.set;

import de.ostfale.qk.domain.match.MatchResultType;
import de.ostfale.qk.domain.set.MatchSet;
import de.ostfale.qk.parser.BaseParserTest;
import de.ostfale.qk.parser.HtmlParserException;
import de.ostfale.qk.parser.web.HtmlStructureParser;
import io.quarkus.logging.Log;
import org.htmlunit.html.HtmlElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Tag("unittest")
@DisplayName("Test parsing different types of sets")
class SetParserServiceTest extends BaseParserTest {

    private static final String MATCH_RESULT_SEPARATOR = "\n";

    private static final String SINGLE_REGULAR_SETS_FILE = "sets/SingleRegularSets.html";
    private static final String SINGLE_FIRST_RETIRED_SETS_FILE = "sets/SingleFirstRetiredSets.html";
    private static final String SINGLE_LAST_RETIRED_SETS_FILE = "sets/SingleLastRetiredSets.html";
    private static final String SINGLE_LAST_RETIRED_WITHOUT_POINT_SETS_FILE = "sets/SingleLastRetiredWithoutPointSet.html";

    private SetParser sut;

    @BeforeEach
    void setUp() {
        sut = new SetParserService();
    }

    @Test
    @DisplayName("Test a regular single set ")
    void testRegularSingleSet() throws HtmlParserException {
        // given
        content = loadHtmlPage(SINGLE_REGULAR_SETS_FILE);
        String[] rawSetElements = extractMatchBodyElements(content.getActiveElement());

        var expectedNumberOfSets = 3;
        var expectedDisplayStringFirstSet = "[Satz 1] 13 : 21";
        var expectedDisplayStringSecondSet = "[Satz 2] 21 : 10";
        var expectedDisplayStringThirdSet = "[Satz 3] 18 : 21";

        // when
        List<MatchSet> result = sut.parseSets(rawSetElements);

        // then
        assertAll("Test regular single sets",
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.size()).isEqualTo(expectedNumberOfSets),
                () -> assertThat(result.getFirst().getDisplayString()).isEqualTo(expectedDisplayStringFirstSet),
                () -> assertThat(result.get(1).getDisplayString()).isEqualTo(expectedDisplayStringSecondSet),
                () -> assertThat(result.getLast().getDisplayString()).isEqualTo(expectedDisplayStringThirdSet)
        );
    }

    @Test
    @DisplayName("Test a single set with retired flag for second player without score")
    void testRetiredForSecondPlayerWithoutScore() throws HtmlParserException {
        // given
        content = loadHtmlPage(SINGLE_LAST_RETIRED_WITHOUT_POINT_SETS_FILE);
        String[] rawSetElements = extractMatchBodyElements(content.getActiveElement());

        var expectedNumberOfSets = 1;
        var expectedDisplayStringFirstSet = "[Satz 1]  0 :  0";

        // when
        List<MatchSet> result = sut.parseSets(rawSetElements);

        // then
        assertAll("Test single sets with retired flag for first player",
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.size()).isEqualTo(expectedNumberOfSets),
                () -> assertThat(result.getFirst().getDisplayString()).isEqualTo(expectedDisplayStringFirstSet),
                () -> assertThat(result.getFirst().getMatchResultType()).isEqualTo(MatchResultType.RETIRED)
        );
    }

    @Test
    @DisplayName("Test a single set with retired flag for first player")
    void testRegularSingleSetRetiredFirstPlayer() throws HtmlParserException {
        // given
        content = loadHtmlPage(SINGLE_FIRST_RETIRED_SETS_FILE);
        String[] rawSetElements = extractMatchBodyElements(content.getActiveElement());

        var expectedNumberOfSets = 1;
        var expectedDisplayStringFirstSet = "[Satz 1] 13 : 20";

        // when
        List<MatchSet> result = sut.parseSets(rawSetElements);

        // then
        assertAll("Test single sets with retired flag for first player",
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.size()).isEqualTo(expectedNumberOfSets),
                () -> assertThat(result.getFirst().getDisplayString()).isEqualTo(expectedDisplayStringFirstSet)
        );
    }

    @Test
    @DisplayName("Test a single set with retired flag for second player")
    void testRegularSingleSetRetiredSecondPlayer() throws HtmlParserException {
        // given
        content = loadHtmlPage(SINGLE_LAST_RETIRED_SETS_FILE);
        String[] rawSetElements = extractMatchBodyElements(content.getActiveElement());

        var expectedNumberOfSets = 2;
        var expectedDisplayStringFirstSet = "[Satz 1] 23 : 21";
        var expectedDisplayStringSecondSet = "[Satz 2]  8 :  8";

        // when
        List<MatchSet> result = sut.parseSets(rawSetElements);

        // then
        assertAll("Test single sets with retired flag",
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.size()).isEqualTo(expectedNumberOfSets),
                () -> assertThat(result.getFirst().getDisplayString()).isEqualTo(expectedDisplayStringFirstSet),
                () -> assertThat(result.getLast().getDisplayString()).isEqualTo(expectedDisplayStringSecondSet)
        );
    }

    private String[] extractMatchBodyElements(HtmlElement matchGroupElement) {
        Log.debug("BaseParser :: extractMatchBodyElements");
        Objects.requireNonNull(matchGroupElement, "matchGroupElement must not be null");
        HtmlElement matchBody = new HtmlStructureParser().getMatchBodyElement(matchGroupElement);
        return matchBody.asNormalizedText().split(MATCH_RESULT_SEPARATOR);
    }
}
