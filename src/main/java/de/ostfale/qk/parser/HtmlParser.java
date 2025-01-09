package de.ostfale.qk.parser;

import jakarta.inject.Singleton;
import org.htmlunit.html.HtmlDivision;
import org.htmlunit.html.HtmlElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Singleton
public class HtmlParser {

    private static final Logger log = LoggerFactory.getLogger(HtmlParser.class);

    final String TOURNAMENT_MODULE_CARD = "//div[contains(@class, 'module module--card')]";
    final String TOURNAMENT_DISCIPLINES_MATCH_GROUP = ".//ol[contains(@class, 'match-group')]";
    final String TOURNAMENT_DISCIPLINES_MATCH = ".//div[contains(@class, 'match')]";


    // all tournaments from the page for this year
    List<HtmlDivision> getAllTournaments(HtmlElement content) {
        log.debug("Parsing all tournament module cards elements");
        List<HtmlDivision> tournaments = content.getByXPath(TOURNAMENT_MODULE_CARD);
        log.debug("Found {} tournament module cards", tournaments.size());
        return tournaments;
    }

    // all disciplines (match groups) played within this tournament
    List<HtmlDivision> getAllDisciplines(HtmlDivision tournament) {
        log.debug("Parsing all disciplines within the tournament");
        List<HtmlDivision> disciplines = tournament.getByXPath(TOURNAMENT_DISCIPLINES_MATCH_GROUP);
        log.debug("Found {} disciplines", disciplines.size());
        return disciplines;
    }

    // read match with the general info and the match result
    List<HtmlDivision> getFullMatchInfo(HtmlDivision matchGroup) {
        log.debug("Parsing a single match with all info");
        List<HtmlDivision> matches = matchGroup.getByXPath(TOURNAMENT_DISCIPLINES_MATCH);
        log.debug("Found {} matches", matches.size());
        return matches;
    }
}

