package de.ostfale.qk.parser.web.discipline;

import de.ostfale.qk.domain.tournament.Tournament;
import de.ostfale.qk.parser.web.HtmlStructureParser;
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

    public void parseDisciplines(Tournament tournament, HtmlElement moduleCardElement) {
        Log.debugf("WebDisciplineParser :: parse disciplines website -> Tournament %s", tournament.getTournamentInfo().tournamentName());
        List<HtmlElement> disciplineElements = htmlStructureParser.getAllDisciplineInfos(moduleCardElement);
        disciplineElements.forEach( disciplineElement-> webDisciplineInfoParser.parseDisciplineInfos(tournament,disciplineElement));

    }
}
