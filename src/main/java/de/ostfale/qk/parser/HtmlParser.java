package de.ostfale.qk.parser;

import jakarta.inject.Singleton;
import org.htmlunit.html.HtmlElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Singleton
public class HtmlParser {

    private static final Logger log = LoggerFactory.getLogger(HtmlParser.class);

    final String TOURNAMENT_MODULE_CARD = "//div[contains(@class, 'module module--card')]";
    final String TOURNAMENT_DISCIPLINES_MATCH_GROUP = ".//ol[contains(@class, 'match-group')]";
    final String TOURNAMENT_DISCIPLINES_MATCH = ".//li[contains(@class, 'match-group__item')]";

    final String TOURNAMENT_NAME_ELEMENT = ".//h4[contains(@class, 'media__title media__title--medium')]";
    final String TOURNAMENT_ID_ELEMENT = ".//a[contains(@class, 'media__img')]";
    final String TOURNAMENT_ORGANISATION_ELEMENT = ".//small[contains(@class, 'media__subheading')]";
    final String TOURNAMENT_DATE_ELEMENT = ".//small[contains(@class, 'media__subheading media__subheading--muted')]";

    final String MATCH_HEADER_ELEMENT = ".//li[contains(@class, 'match__header-title-item')]";
    final String MATCH_FOOTER_ELEMENT = ".//li[contains(@class, 'match__footer-list-item')]";


    // all tournaments from the page for this year
    public List<HtmlElement> getAllTournaments(HtmlElement content) {
        log.debug("Parsing all tournament module cards elements");
        List<HtmlElement> tournaments = content.getByXPath(TOURNAMENT_MODULE_CARD);
        log.debug("Found {} tournament module cards", tournaments.size());
        return tournaments;
    }

    // all disciplines (match groups) played within this tournament
    public List<HtmlElement> getAllDisciplines(HtmlElement tournament) {
        log.debug("Parsing all disciplines within the tournament");
        List<HtmlElement> disciplines = tournament.getByXPath(TOURNAMENT_DISCIPLINES_MATCH_GROUP);
        log.debug("Found {} disciplines", disciplines.size());
        return disciplines;
    }

    // HtmlElement which contains the name of the tournament
    public HtmlElement getTournamentNameElement(HtmlElement element) {
        log.debug("Parsing tournament header name element");
        return element.getFirstByXPath(TOURNAMENT_NAME_ELEMENT);
    }

    public List<HtmlElement> getTournamentIdElement(HtmlElement element) {
        log.debug("Parsing tournament header id element");
        return element.getByXPath(TOURNAMENT_ID_ELEMENT);
    }

    public HtmlElement getTournamentOrganisationElement(HtmlElement element) {
        log.debug("Parsing tournament header organisation and location element");
        return element.getFirstByXPath(TOURNAMENT_ORGANISATION_ELEMENT);
    }

    public HtmlElement getTournamentDateElement(HtmlElement element) {
        log.debug("Parsing tournament date element");
        return element.getFirstByXPath(TOURNAMENT_DATE_ELEMENT);
    }

    // read match with the general info and the match result
    public List<HtmlElement> getFullMatchInfo(HtmlElement matchGroup) {
        log.debug("Parsing a single match with all info");
        List<HtmlElement> matches = matchGroup.getByXPath(TOURNAMENT_DISCIPLINES_MATCH);
        log.debug("Found {} matches", matches.size());
        return matches;
    }

    // read element which contains the round of the match
    public HtmlElement getMatchHeaderElement(HtmlElement singleMatch) {
        log.debug("Parsing match header info");
        return singleMatch.getFirstByXPath(MATCH_HEADER_ELEMENT);
    }

    // read element which contains the round of the match
    public HtmlElement getMatchFooterElement(HtmlElement singleMatch) {
        log.debug("Parsing match footer info");
        return singleMatch.getFirstByXPath(MATCH_FOOTER_ELEMENT);
    }
}

