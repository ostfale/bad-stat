package de.ostfale.qk.parser.web.tournament;

import de.ostfale.qk.domain.tournament.Tournament;
import de.ostfale.qk.domain.tournament.TournamentInfo;
import de.ostfale.qk.parser.HtmlParserException;
import de.ostfale.qk.parser.web.BaseParser;
import de.ostfale.qk.parser.web.discipline.WebDisciplineParserService;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class WebTournamentParserService implements BaseParser {

    // TODO separate interfaces
    private final WebTournamentInfoParser tournamentInfoParser;

    private final WebDisciplineParserService webDisciplineParserService;

    public WebTournamentParserService(WebTournamentInfoParser tournamentInfoParser, WebDisciplineParserService webDisciplineParserService) {
        this.tournamentInfoParser = tournamentInfoParser;
        this.webDisciplineParserService = webDisciplineParserService;
    }

    public List<Tournament> parseAllYearlyTournamentsForPlayer(String currentPlayer, HtmlPage content) {
        Log.debug("WebTournamentParser :: parse all yearly tournaments website -> HtmlElement");
        List<HtmlElement> tournamentElements = htmlStructureParser.getAllTournaments(content.getActiveElement());
        List<Tournament> allTournaments = new ArrayList<>();

        for (HtmlElement moduleCard : tournamentElements) {
            try {
                TournamentInfo tournamentInfo = tournamentInfoParser.parseTournamentInfo(moduleCard);
                Log.infof("WebTournamentParser :: Parsing tournament info -> %s", tournamentInfo.tournamentName());
                var tournament = new Tournament(tournamentInfo);
                var result = webDisciplineParserService.parseTournamentDisciplines(currentPlayer,moduleCard);
                tournament.getDisciplines().addAll(result);
                allTournaments.add(tournament);
            }
            catch (HtmlParserException parserException) {
                Log.errorf("TournamentWebService :: Failed to parse tournaments page -> component:  %s", parserException.getParserError());
            }
            catch (Exception e) {
                Log.errorf("TournamentWebService :: Failed to scrape tournaments page %s ", e.getMessage());

            }
        }
        return allTournaments;
    }

    public List<Integer> getListOfYearsWithTournaments(HtmlPage content) {
        Log.debug("WebTournamentParser :: check the years with tournaments for a player ");
        List<Integer> years = new ArrayList<>();
        List<HtmlElement> tournamentElements = extractAvailableTournamentYears(content.getActiveElement());
        for (HtmlElement tournamentYear : tournamentElements) {
            String tournamentYearText = tournamentYear.asNormalizedText();
            if (!tournamentYearText.equalsIgnoreCase("älter")) {
                years.add(Integer.parseInt(tournamentYearText));
            }
        }
        return years;
    }

    public Integer getNumberOfTournaments(HtmlPage content) {
        Log.debug("WebTournamentParser :: get number of tournaments website ");
        List<HtmlElement> tournamentElements = htmlStructureParser.getAllTournaments(content.getActiveElement());
        return tournamentElements.size();
    }

    // read round name
    final String TOURNAMENT_YEARS_LIST = ".//li[contains(@class, 'page-nav__item js-page-nav__item')]";

    public List<HtmlElement> extractAvailableTournamentYears(HtmlElement element) {
        Log.debug("WebTournamentParserService :: Extract available tournament years from web element");
        return element.getByXPath(TOURNAMENT_YEARS_LIST);
    }
}
