package de.ostfale.qk.parser.tournament;

import de.ostfale.qk.parser.HtmlParser;
import de.ostfale.qk.parser.HtmlParserException;
import de.ostfale.qk.parser.discipline.DisciplineParser;
import de.ostfale.qk.parser.discipline.model.DisciplineParserModel;
import de.ostfale.qk.parser.tournament.model.TournamentParserModel;
import de.ostfale.qk.parser.tournament.model.TournamentYearParserModel;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.htmlunit.html.HtmlElement;
import org.jboss.logging.Logger;

import java.util.List;

@Singleton
public class TournamentParserService implements TournamentParser {

    @Inject
    HtmlParser htmlParser;

    @Inject
    DisciplineParser disciplineParser;

    private static final Logger log = Logger.getLogger(TournamentParserService.class);

    @Override
    public TournamentYearParserModel parseTournamentYear(String year, HtmlElement content) {
        log.debugf("Parsing tournament year {}", year);
        TournamentYearParserModel tournamentYearParserModel = new TournamentYearParserModel(year);
        List<HtmlElement> tournamentElements = htmlParser.getAllTournaments(content);
        tournamentElements.forEach(tournamentElement -> {
            try {
                TournamentParserModel tournamentParserModel = parseTournamentInfo(tournamentElement);
                List<DisciplineParserModel> disciplineDTOS = disciplineParser.parseDisciplines(tournamentElement);
                tournamentParserModel.getTournamentDisciplines().addAll(disciplineDTOS);
                tournamentYearParserModel.addTournament(tournamentParserModel);
            } catch (HtmlParserException e) {
                log.errorf("TournamentParser :: year %s -> %s ", year, e.getParserError());
            }
        });
        return tournamentYearParserModel;
    }

    @Override
    public TournamentParserModel parseTournamentInfo(HtmlElement content) {
        log.debug("Parsing tournament general info");
        HtmlElement tournamentNameElement = htmlParser.getTournamentNameElement(content);
        HtmlElement tournamentDateElement = htmlParser.getTournamentDateElement(content);
        List<HtmlElement> tournamentIdElements = htmlParser.getTournamentIdElement(content);
        var tournamentIdArray = tournamentIdElements.getFirst().getAttribute("href").split("=");

        HtmlElement tournamentOrganisationElement = htmlParser.getTournamentOrganisationElement(content);
        String[] organizerAndLocation = tournamentOrganisationElement.asNormalizedText().split("\\|");

        var tournamentName = tournamentNameElement.asNormalizedText();
        var tournamentOrganisation = organizerAndLocation[0].trim();
        var tournamentLocation = organizerAndLocation[1].trim();
        var tournamentId = tournamentIdArray[tournamentIdArray.length - 1];
        var tournamentDate = tournamentDateElement.asNormalizedText();

        return new TournamentParserModel(tournamentId, tournamentName, tournamentOrganisation, tournamentLocation, tournamentDate);
    }

    @Override
    public Integer parseNofTournaments(HtmlElement content) {
        List<HtmlElement> tournamentElements = htmlParser.getAllTournaments(content);
        return tournamentElements.size();
    }
}
