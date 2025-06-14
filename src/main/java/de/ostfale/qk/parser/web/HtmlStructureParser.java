package de.ostfale.qk.parser.web;

import io.quarkus.logging.Log;
import jakarta.inject.Singleton;
import org.htmlunit.html.HtmlElement;

import java.util.List;

@Singleton
public class HtmlStructureParser implements HtmlStructureElements {

    /* YEARLY TOURNAMENTS FOR A PLAYER */

    // returns a list of HtmlElements each contains (module__card) with a tournament
    public List<HtmlElement> getAllTournaments(HtmlElement content) {
        List<HtmlElement> tournaments = content.getByXPath(TOURNAMENT_MODULE_CARD);
        Log.debugf("HtmlStructureParser :: Parsing all tournament for a year and found: %d", tournaments.size());
        return tournaments;
    }

    /* TOURNAMENT HEADER INFORMATION */

    // extract HTML elements from tournament header
    public HtmlElement extractTournamentTitleElement(HtmlElement element) {
        Log.debugf("HtmlStructureParser :: Parsing tournament header -> title element");
        return element.getFirstByXPath(TOURNAMENT_HEADER_TITLE_ELEMENT);
    }

    public HtmlElement extractTournamentOrgAndLocationElement(HtmlElement element) {
        Log.debugf("HtmlStructureParser :: Parsing tournament header -> organisation and location element");
        return element.getFirstByXPath(TOURNAMENT_HEADER_LOC_ORGAN_ELEMENT);
    }

    public HtmlElement extractTournamentDateElement(HtmlElement element) {
        Log.debugf("HtmlStructureParser :: Parsing tournament header -> date element");
        return element.getFirstByXPath(TOURNAMENT_DATE_ELEMENT);
    }


    /* DISCIPLINE INFORMATION */

    // within a module__card will be 1-3 disciplines with a separate header
    public List<HtmlElement> getAllDisciplineInfos(HtmlElement tournament) {
        Log.debugf("Parsing all discipline header info elements");
        List<HtmlElement> disciplines = tournament.getByXPath(DISCIPLINES_MATCHES_INFO);
        Log.debugf("Found {} discipline header info elements", disciplines.size());
        return disciplines;
    }
}
