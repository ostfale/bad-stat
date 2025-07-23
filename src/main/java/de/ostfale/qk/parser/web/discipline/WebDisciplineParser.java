package de.ostfale.qk.parser.web.discipline;

import de.ostfale.qk.domain.discipline.AgeClass;
import de.ostfale.qk.domain.discipline.DisciplineType;
import de.ostfale.qk.parser.web.BaseParser;
import io.quarkus.logging.Log;
import org.htmlunit.html.HtmlElement;

import java.util.List;
import java.util.Map;

import static de.ostfale.qk.domain.discipline.AgeClass.*;
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

    String DISCIPLINE_DISPLAY_STRING_ELEMENT = ".//h5[contains(@class, 'module-divider')]";

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

    Map<String, DisciplineMapping> DISCIPLINE_SUB_HEADER_MAPPING = Map.<String, DisciplineMapping>ofEntries(
            Map.entry("JE-U9 A", new DisciplineMapping(SINGLE, U9)),
            Map.entry("ME-U9 A", new DisciplineMapping(SINGLE, U9)),

            Map.entry("JE-U11 A", new DisciplineMapping(SINGLE, U11)),
            Map.entry("ME-U11 A", new DisciplineMapping(SINGLE, U11)),
            Map.entry("MD-U11 A", new DisciplineMapping(DOUBLE, U11)),
            Map.entry("JD-U11 A", new DisciplineMapping(DOUBLE, U11)),

            Map.entry("U13JE", new DisciplineMapping(SINGLE, U13)),
            Map.entry("U13MX", new DisciplineMapping(MIXED, U13)),
            Map.entry("U13JD", new DisciplineMapping(DOUBLE, U13)),
            Map.entry("U13MD", new DisciplineMapping(DOUBLE, U13)),
            Map.entry("JE-U13 A", new DisciplineMapping(SINGLE, U13)),
            Map.entry("ME-U13 A", new DisciplineMapping(SINGLE, U13)),
            Map.entry("MD-U13 A", new DisciplineMapping(DOUBLE, U13)),
            Map.entry("JD-U13 A", new DisciplineMapping(DOUBLE, U13)),
            Map.entry("MX-U13 A", new DisciplineMapping(MIXED, U13)),
            Map.entry("U13B Mädcheneinzel", new DisciplineMapping(SINGLE, U13)),

            Map.entry("U15JE", new DisciplineMapping(SINGLE, U15)),
            Map.entry("U15JD", new DisciplineMapping(DOUBLE, U15)),
            Map.entry("U15MD", new DisciplineMapping(DOUBLE, U15)),
            Map.entry("JE-U15 A", new DisciplineMapping(SINGLE, U15)),
            Map.entry("ME-U15 A", new DisciplineMapping(SINGLE, U15)),
            Map.entry("MD-U15 A", new DisciplineMapping(DOUBLE, U15)),
            Map.entry("JD-U15 A", new DisciplineMapping(DOUBLE, U15)),
            Map.entry("MX-U15 A", new DisciplineMapping(MIXED, U15)),
            Map.entry("U15B Jungeneinzel", new DisciplineMapping(SINGLE, U15)),
            Map.entry("Boys Singles U15", new DisciplineMapping(SINGLE, U15)),
            Map.entry("Boys Doubles U15", new DisciplineMapping(DOUBLE, U15)),
            Map.entry("Girls Doubles U15", new DisciplineMapping(DOUBLE, U15)),
            Map.entry("Girls Singles U15", new DisciplineMapping(SINGLE, U15)),
            Map.entry("Mixed Doubles U15", new DisciplineMapping(MIXED, U15)),

            Map.entry("JE-U17 A", new DisciplineMapping(SINGLE, U17)),
            Map.entry("ME-U17 A", new DisciplineMapping(SINGLE, U17)),
            Map.entry("MD-U17 A", new DisciplineMapping(DOUBLE, U17)),
            Map.entry("JD-U17 A", new DisciplineMapping(DOUBLE, U17)),
            Map.entry("MX-U17 A", new DisciplineMapping(MIXED, U17)),

            Map.entry("JE-U19 A", new DisciplineMapping(SINGLE, U19)),
            Map.entry("ME-U19 A", new DisciplineMapping(SINGLE, U19)),
            Map.entry("MD-U19 A", new DisciplineMapping(DOUBLE, U19)),
            Map.entry("JD-U19 A", new DisciplineMapping(DOUBLE, U19)),
            Map.entry("MX-U19 A", new DisciplineMapping(MIXED, U19)),

            Map.entry("HE-U22 A", new DisciplineMapping(SINGLE, U22)),
            Map.entry("DE-U22 A", new DisciplineMapping(SINGLE, U22)),
            Map.entry("HD-U22 A", new DisciplineMapping(DOUBLE, U22)),
            Map.entry("DD-U22 A", new DisciplineMapping(DOUBLE, U22)),
            Map.entry("MX-U22 A", new DisciplineMapping(MIXED, U22)),

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
            Map.entry("Women's Doubles", new DisciplineMapping(DOUBLE, UOX))
    );

    record DisciplineMapping(DisciplineType disciplineType, AgeClass ageClass) {
    }
}
