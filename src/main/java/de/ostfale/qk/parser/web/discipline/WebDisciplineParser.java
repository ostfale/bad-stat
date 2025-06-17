package de.ostfale.qk.parser.web.discipline;

import de.ostfale.qk.domain.tournament.Tournament;
import de.ostfale.qk.parser.web.HtmlStructureParser;
import de.ostfale.qk.parser.web.match.WebMatchParser;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.htmlunit.html.HtmlElement;

import java.util.List;

@ApplicationScoped
public class WebDisciplineParser {

    @Inject
    HtmlStructureParser htmlStructureParser;

    @Inject
    WebDisciplineInfoParser webDisciplineInfoParser;

    @Inject
    WebMatchParser webMatchParser;

    public void parseDisciplines(Tournament tournament, HtmlElement moduleCardElement) {
        Log.debugf("WebDisciplineParser :: parse disciplines website -> Tournament %s", tournament.getTournamentInfo().tournamentName());
        List<HtmlElement> disciplineElements = htmlStructureParser.getAllDisciplineInfos(moduleCardElement);
        List<HtmlElement> matchGroupElements = htmlStructureParser.getEliminationGroupContainerList(moduleCardElement);
        disciplineElements.forEach(disciplineElement -> webDisciplineInfoParser.parseDisciplineInfos(tournament, disciplineElement));

        if (disciplineElements.size() == matchGroupElements.size()) {
            Log.debugf("WebDisciplineParser :: Pure elimination disciplines found for tournament: %s", tournament.getTournamentInfo().tournamentName());
            webMatchParser.parseEliminationMatches(tournament, matchGroupElements);
            Log.debugf("WebDisciplineParser :: Pure elimination disciplines parsed for tournament: %s", tournament.getTournamentInfo().tournamentName());
        } else if (disciplineElements.size() < matchGroupElements.size()) {
            Log.debugf("WebDisciplineParser :: Combined elimination disciplines found for tournament: %s", tournament.getTournamentInfo().tournamentName());
            webMatchParser.parseCombinedMatches(tournament,matchGroupElements);
        }
    }
}
