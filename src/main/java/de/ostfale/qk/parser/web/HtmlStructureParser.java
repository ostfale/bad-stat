package de.ostfale.qk.parser.web;

import io.quarkus.logging.Log;
import jakarta.inject.Singleton;
import org.htmlunit.html.HtmlElement;

import java.util.List;

@Singleton
public class HtmlStructureParser implements HtmlStructureElements {

    public List<HtmlElement> getAllTournaments(HtmlElement content) {
        List<HtmlElement> tournaments = content.getByXPath(TOURNAMENT_MODULE_CARD);
        Log.debugf("HtmlStructureParser :: Parsing all tournament for a year and found: %d", tournaments.size());
        return tournaments;
    }

    /* TOURNAMENT HEADER INFORMATION */

    // extract HTML elements from tournament header
    public HtmlElement extractTournamentTitleElement(HtmlElement element) {
        Log.debugf("HtmlStructureParser :: Parsing tournament header -> title element");
        return element.getFirstByXPath(TOURNAMENT_TITLE_ELEMENT);
    }

    public HtmlElement extractTournamentOrgAndLocationElement(HtmlElement element) {
        Log.debugf("HtmlStructureParser :: Parsing tournament header -> organisation and location element");
        return element.getFirstByXPath(TOURNAMENT_LOC_ORGAN_ELEMENT);
    }

    public HtmlElement extractTournamentDateElement(HtmlElement element) {
        Log.debugf("HtmlStructureParser :: Parsing tournament header -> date element");
        return element.getFirstByXPath(TOURNAMENT_DATE_ELEMENT);
    }
}
