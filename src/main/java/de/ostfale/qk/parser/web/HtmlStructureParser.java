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

    // list all matches for a discipline -> elimination (1 element) + group (2 elements)
    String DISCIPLINE_ALL_MATCHES = ".//ol[contains(@class, 'match-group')]";

    public List<HtmlElement> getAllDisciplineMatches(HtmlElement module_card) {
        Log.debug("HtmlStructureParser :: Parsing all disciplines and return container with all matches for a discipline");
        List<HtmlElement> disciplineMatchesContainer = module_card.getByXPath(DISCIPLINE_ALL_MATCHES);
        Log.debugf("Found {} discipline matches", disciplineMatchesContainer.size());
        return disciplineMatchesContainer;
    }

    // extract name before the matches which can be the name of the discipline or the name of a group in combined tournament
    String DISCIPLINE_MODE = ".//h5[contains(@class, 'module-divider')]";

    public List<HtmlElement> getDisciplineOrGroupName(HtmlElement discipline) {
        Log.debug("HtmlStructureParser ::Parsing discipline all matches in elimination and group phase");
        return discipline.getByXPath(DISCIPLINE_MODE);
    }

    /* MATCH INFORMATION */

    // read match with the general info and the match result -> returns a list of single match containers
    String DISCIPLINE_MATCH = ".//li[contains(@class, 'match-group__item')]";

    public List<HtmlElement> getAllMatchesForMatchGroupContainer(HtmlElement matchGroup) {
        Log.debug("HtmlStructureParser ::Extract all match elements for a match group");
        List<HtmlElement> matches = matchGroup.getByXPath(DISCIPLINE_MATCH);
        Log.debugf("HtmlStructureParser :: Found %d matches in match group", matches.size());
        return matches;
    }

   // read round name
   final String MATCH_ROUND_NAME = ".//li[contains(@class, 'match__header-title-item')]";

    public HtmlElement getMatchRoundNameElement(HtmlElement element) {
        Log.debugf("HtmlStructureParser :: Parsing match header info -> round name element");
        return element.getFirstByXPath(MATCH_ROUND_NAME);
    }

    // read match information -> player, sets, irregular results
    final String MATCH_BODY = ".//div[contains(@class, 'match__body')]";

    public HtmlElement getMatchBodyElement(HtmlElement element) {
        Log.debugf("HtmlStructureParser :: Parsing match body info -> player, sets, irregular results");
        return element.getFirstByXPath(MATCH_BODY);
    }

}
