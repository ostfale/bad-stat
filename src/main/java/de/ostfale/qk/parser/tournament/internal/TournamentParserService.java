package de.ostfale.qk.parser.tournament.internal;

import de.ostfale.qk.parser.HtmlParser;
import de.ostfale.qk.parser.discipline.api.DisciplineParser;
import de.ostfale.qk.parser.discipline.internal.model.DisciplineRawModel;
import de.ostfale.qk.parser.tournament.api.TournamentParser;
import de.ostfale.qk.parser.tournament.internal.model.TournamentRawModel;
import de.ostfale.qk.parser.tournament.internal.model.TournamentYearRawModel;
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
    public TournamentYearRawModel parseTournamentYear(String year, HtmlElement content) {
        log.debugf("Parsing tournament year {}", year);
        TournamentYearRawModel tournamentYearRawModel = new TournamentYearRawModel(year);
        List<HtmlElement> tournamentElements = htmlParser.getAllTournaments(content);
        tournamentElements.forEach(tournamentElement -> {
            TournamentRawModel tournamentRawModel = parseTournamentInfo(tournamentElement);
            List<DisciplineRawModel> disciplineDTOS = disciplineParser.parseDisciplines(tournamentElement);
            tournamentRawModel.getTournamentDisciplines().addAll(disciplineDTOS);
            tournamentYearRawModel.addTournament(tournamentRawModel);
        });
        return tournamentYearRawModel;
    }

    @Override
    public TournamentRawModel parseTournamentInfo(HtmlElement content) {
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

        return new TournamentRawModel(tournamentId, tournamentName, tournamentOrganisation, tournamentLocation, tournamentDate);
    }

    @Override
    public Integer parseNofTournaments(String year, HtmlElement content) {
        List<HtmlElement> tournamentElements = htmlParser.getAllTournaments(content);
        var tournaments =  tournamentElements.size();
        log.debugf("Found %d tournaments for year %s", tournaments, year);
        return tournaments;
    }
}
