package de.ostfale.qk.parser;

import jakarta.inject.Singleton;
import org.htmlunit.html.HtmlElement;
import org.jboss.logging.Logger;

import java.util.List;


@Singleton
public class HtmlParser {

    private static final Logger log = Logger.getLogger(HtmlParser.class);

    final String TOURNAMENT_MODULE_CARD = "//div[contains(@class, 'module module--card')]";

    final String TOURNAMENT_DISCIPLINES_MATCH_INFO = ".//h4[contains(@class, 'module-divider')]";
    final String TOURNAMENT_DISCIPLINES_MATCH_GROUP = ".//ol[contains(@class, 'match-group')]";
    final String TOURNAMENT_DISCIPLINES_MATCH = ".//li[contains(@class, 'match-group__item')]";

    final String TOURNAMENT_NAME_ELEMENT = ".//h4[contains(@class, 'media__title media__title--medium')]";
    final String TOURNAMENT_ID_ELEMENT = ".//a[contains(@class, 'media__img')]";
    final String TOURNAMENT_ORGANISATION_ELEMENT = ".//small[contains(@class, 'media__subheading')]";
    final String TOURNAMENT_DATE_ELEMENT = ".//small[contains(@class, 'media__subheading media__subheading--muted')]";

    final String DISCIPLINE_MODE = ".//h5[contains(@class, 'module-divider')]";

    final String MATCH_GROUP_ELEMENT = ".//li[contains(@class, 'match-group__item')]";
    final String MATCH_HEADER_ELEMENT = ".//li[contains(@class, 'match__header-title-item')]";
    final String MATCH_BODY_ELEMENT = ".//div[contains(@class, 'match__body')]";
    final String MATCH_FOOTER_ELEMENT = ".//li[contains(@class, 'match__footer-list-item')]";


    // all tournaments from the page for this year
    public List<HtmlElement> getAllTournaments(HtmlElement content) {
        log.debugf("Parsing all tournament module cards elements");
        List<HtmlElement> tournaments = content.getByXPath(TOURNAMENT_MODULE_CARD);
        log.debugf("Found {} tournament module cards", tournaments.size());
        return tournaments;
    }

    // HtmlElements which contain header information about discipline and age class
    public List<HtmlElement> getAllDisciplineInfos(HtmlElement tournament) {
        log.debugf("Parsing all discipline header info elements");
        List<HtmlElement> disciplines = tournament.getByXPath(TOURNAMENT_DISCIPLINES_MATCH_INFO);
        log.debugf("Found {} discipline header info elements", disciplines.size());
        return disciplines;
    }


    // all disciplines (match groups) played within this tournament
    public List<HtmlElement> getAllDisciplines(HtmlElement matchGroup) {
        log.debugf("Parsing all disciplines within the tournament");
        List<HtmlElement> disciplines = matchGroup.getByXPath(TOURNAMENT_DISCIPLINES_MATCH_GROUP);
        log.debugf("Found {} disciplines", disciplines.size());
        return disciplines;
    }

    // check the existence of a group phase within the discipline
    public List<HtmlElement> getDisciplineTreeGroupContainerList(HtmlElement discipline) {
        log.debugf("Parsing discipline mode for a group hase");
        List<HtmlElement> mode = discipline.getByXPath(DISCIPLINE_MODE);
        log.debugf("Found {} mode", mode.size());
        return mode;
    }

    // HtmlElement which contains the name of the tournament
    public HtmlElement getTournamentNameElement(HtmlElement element) {
        log.debugf("Parsing tournament header name element");
        return element.getFirstByXPath(TOURNAMENT_NAME_ELEMENT);
    }

    // List of HtmlElement to extract the tournament id information
    public List<HtmlElement> getTournamentIdElement(HtmlElement element) {
        log.debugf("Parsing tournament header id element");
        return element.getByXPath(TOURNAMENT_ID_ELEMENT);
    }

    // HtmlElement which contains the information who organized the tournament
    public HtmlElement getTournamentOrganisationElement(HtmlElement element) {
        log.debugf("Parsing tournament header organisation and location element");
        return element.getFirstByXPath(TOURNAMENT_ORGANISATION_ELEMENT);
    }

    // HtmlElement which contains the date the tournament happened
    public HtmlElement getTournamentDateElement(HtmlElement element) {
        log.debugf("Parsing tournament date element");
        return element.getFirstByXPath(TOURNAMENT_DATE_ELEMENT);
    }

    // read match with the general info and the match result
    public List<HtmlElement> getAllMatchesForDisciplineContainer(HtmlElement matchGroup) {
        log.debugf("Parsing a single match with all info");
        List<HtmlElement> matches = matchGroup.getByXPath(TOURNAMENT_DISCIPLINES_MATCH);
        log.debugf("Found {} matches", matches.size());
        return matches;
    }

    // read element which contains the round of the match
    public HtmlElement getMatchHeaderElement(HtmlElement singleMatch) {
        log.debugf("Parsing match header info");
        return singleMatch.getFirstByXPath(MATCH_HEADER_ELEMENT);
    }

    // read element which contains the player, sets and the result
    public HtmlElement getMatchBodyElement(HtmlElement singleMatch) {
        log.debugf("Parsing match body info");
        return singleMatch.getFirstByXPath(MATCH_BODY_ELEMENT);
    }

    // read element which contains the round of the match
    public HtmlElement getMatchFooterElement(HtmlElement singleMatch) {
        log.debugf("Parsing match footer info");
        return singleMatch.getFirstByXPath(MATCH_FOOTER_ELEMENT);
    }
}

