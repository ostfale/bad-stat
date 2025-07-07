package de.ostfale.qk.parser.web.discipline;

import de.ostfale.qk.parser.web.BaseParser;
import io.quarkus.logging.Log;
import org.htmlunit.html.HtmlElement;

import java.util.List;

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
     *  A discipline can have a group phase which is the second sequence after the elimination phase
     *  in the html structure. There is only one discipline header (h4) but maximum 2 subheaders (h5) (1 subheader
     *  means elimination, 2 subheaders means elimination and group phase
     *
     * @param moduleCard an HtmlElement representing the tournament's module card containing discipline or group matches
     * @return a list of HtmlElements that match the XPath criteria for discipline subheaders
     */
    default List<HtmlElement> extractDisciplineSubString(HtmlElement moduleCard) {
        Log.debugf("HtmlStructureParser :: Parsing tournament header -> title element");
        return moduleCard.getByXPath(DISCIPLINE_DISPLAY_STRING_ELEMENT);
    }
}
