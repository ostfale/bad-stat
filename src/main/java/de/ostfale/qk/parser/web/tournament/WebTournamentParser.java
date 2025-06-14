package de.ostfale.qk.parser.web.tournament;

import de.ostfale.qk.domain.tournament.Tournament;
import de.ostfale.qk.domain.tournament.TournamentInfo;
import de.ostfale.qk.parser.BaseWebParser;
import de.ostfale.qk.parser.HtmlParserException;
import de.ostfale.qk.parser.web.HtmlStructureParser;
import de.ostfale.qk.parser.web.discipline.WebDisciplineParser;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.htmlunit.html.HtmlElement;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class WebTournamentParser extends BaseWebParser {

    @Inject
    HtmlStructureParser htmlStructureParser;

    @Inject
    WebTournamentInfoParser tournamentInfoParser;

    @Inject
    WebDisciplineParser webDisciplineParser;

    public List<Tournament> parseAllYearlyTournamentsForPlayer(HtmlElement content) throws HtmlParserException {
        Log.debug("WebTournamentParser :: parse all yearly tournaments website -> HtmlElement");
        List<HtmlElement> tournamentElements = htmlStructureParser.getAllTournaments(content);
        List<Tournament> allTournaments = new ArrayList<>();

        for (HtmlElement tournamentElement : tournamentElements) {
            TournamentInfo tournamentInfo = tournamentInfoParser.parseTournamentInfo(tournamentElement);
            var tournament = new Tournament(tournamentInfo);
            webDisciplineParser.parseDisciplines(tournament,tournamentElement);
            allTournaments.add(tournament);
        }
        return allTournaments;
    }
}
