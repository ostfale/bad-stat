package de.ostfale.qk.parser.web.tournament;

import de.ostfale.qk.domain.tournament.Tournament;
import de.ostfale.qk.domain.tournament.TournamentInfo;
import de.ostfale.qk.parser.HtmlParserException;
import de.ostfale.qk.parser.web.discipline.WebDisciplineParserService;
import io.quarkus.logging.Log;
import jakarta.inject.Singleton;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class WebTournamentParserService implements WebTournamentParser {

    // TODO separate interfaces
    private final WebTournamentInfoParser tournamentInfoParser;

    private final WebDisciplineParserService webDisciplineParserService;

    public WebTournamentParserService(WebTournamentInfoParser tournamentInfoParser, WebDisciplineParserService webDisciplineParserService) {
        this.tournamentInfoParser = tournamentInfoParser;
        this.webDisciplineParserService = webDisciplineParserService;
    }

    public List<Tournament> parseAllYearlyTournamentsForPlayer(HtmlPage content) throws HtmlParserException {
        Log.debug("WebTournamentParser :: parse all yearly tournaments website -> HtmlElement");
        List<HtmlElement> tournamentElements = htmlStructureParser.getAllTournaments(content.getActiveElement());
        List<Tournament> allTournaments = new ArrayList<>();

        for (HtmlElement moduleCard : tournamentElements) {
            TournamentInfo tournamentInfo = tournamentInfoParser.parseTournamentInfo(moduleCard);
            var tournament = new Tournament(tournamentInfo);
            webDisciplineParserService.parseDisciplines(tournament,moduleCard);
            allTournaments.add(tournament);
        }
        return allTournaments;
    }
}
