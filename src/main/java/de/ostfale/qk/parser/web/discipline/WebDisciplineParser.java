package de.ostfale.qk.parser.web.discipline;

import de.ostfale.qk.domain.discipline.AgeClass;
import de.ostfale.qk.domain.discipline.DisciplineType;
import de.ostfale.qk.parser.web.BaseParser;
import io.quarkus.logging.Log;
import org.htmlunit.html.HtmlElement;

import java.util.List;
import java.util.Map;

import static de.ostfale.qk.domain.discipline.AgeClass.U15;
import static de.ostfale.qk.domain.discipline.AgeClass.UOX;
import static de.ostfale.qk.domain.discipline.DisciplineType.*;

public interface WebDisciplineParser extends BaseParser {

    String DISCIPLINES_MATCHES_INFO = ".//h4[contains(@class, 'module-divider')]";

    /**
     * The h4 element is the top header for a discipline and can have a subheader (h5) with elimination
     * matches or a subheader h5 with group matches if the tournament has a group phase
     *
     * @param moduleCard contains complete tournament
     * @return HtmlElement for each discipline in a match
     */
    default List<HtmlElement> extractDisciplineInfo(HtmlElement moduleCard) {
        Log.debugf("Parsing all discipline header info elements");
        List<HtmlElement> disciplines = moduleCard.getByXPath(DISCIPLINES_MATCHES_INFO);
        Log.debugf("Found {} discipline header info elements", disciplines.size());
        return disciplines;
    }

    String DISCIPLINE_DISPLAY_STRING_ELEMENT = "//h5[contains(@class, 'module-divider')]";

    /**
     * A discipline can have a group phase which is the second sequence after the elimination phase
     * in the html structure. There is only one discipline header (h4) but maximum 2 subheaders (h5) (1 subheader
     * means elimination, 2 subheaders means elimination and group phase
     *
     * @param moduleCard an HtmlElement representing the tournament's module card containing discipline or group matches
     * @return a list of HtmlElements that match the XPath criteria for discipline subheaders
     */
    default List<HtmlElement> extractDisciplineSubString(HtmlElement moduleCard) {
        Log.debugf("HtmlStructureParser :: Parsing tournament header -> title element");
        return moduleCard.getByXPath(DISCIPLINE_DISPLAY_STRING_ELEMENT);
    }

    String DISCIPLINE_ALL_MATCHES = ".//ol[contains(@class, 'match-group')]";

    /**
     * Within a moduleCard element there are also elements ('match-groups') which contain a group of matches for this discipline in
     * an elimination phase or group matches within a group phase before the elimination
     *
     * @param moduleCard a HtmlElement representing the tournament's matches
     * @return a list of HtmlElements which represents each a group of matches for a discipline of group
     */
    default List<HtmlElement> extractAllDisciplinesMatchElements(HtmlElement moduleCard) {
        Log.debug("HtmlStructureParser :: Parsing all disciplines and return container with all matches for all disciplines (whole tournament");
        return moduleCard.getByXPath(DISCIPLINE_ALL_MATCHES);
    }

    Map<String, DisciplineMapping> DISCIPLINE_SUB_HEADER_MAPPING = Map.ofEntries(
            Map.entry("DD", new DisciplineMapping(DOUBLE, UOX)),
            Map.entry("DE", new DisciplineMapping(SINGLE, UOX)),
            Map.entry("HE", new DisciplineMapping(SINGLE, UOX)),
            Map.entry("HD", new DisciplineMapping(DOUBLE, UOX)),
            Map.entry("MX", new DisciplineMapping(MIXED, UOX)),
            Map.entry("MX LK1", new DisciplineMapping(MIXED, UOX)),
            Map.entry("Mixed Doubles", new DisciplineMapping(MIXED, UOX)),
            Map.entry("Men's Singles", new DisciplineMapping(SINGLE, UOX)),
            Map.entry("Men's Doubles", new DisciplineMapping(DOUBLE, UOX)),
            Map.entry("Women's Singles", new DisciplineMapping(SINGLE, UOX)),
            Map.entry("Women's Doubles", new DisciplineMapping(DOUBLE, UOX)),
            Map.entry("Mixed Doubles U15", new DisciplineMapping(MIXED, U15))
    );

    record DisciplineMapping(DisciplineType disciplineType, AgeClass ageClass) {
    }
}
