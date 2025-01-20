package de.ostfale.qk.parser.tournament.internal;

import de.ostfale.qk.parser.HtmlParser;
import de.ostfale.qk.parser.discipline.api.DisciplineParser;
import de.ostfale.qk.parser.discipline.internal.model.DisciplineDTO;
import de.ostfale.qk.parser.tournament.api.TournamentParser;
import de.ostfale.qk.parser.tournament.internal.model.TournamentDTO;
import de.ostfale.qk.parser.tournament.internal.model.TournamentInfoDTO;
import de.ostfale.qk.parser.tournament.internal.model.TournamentYearDTO;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.htmlunit.html.HtmlElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Singleton
public class TournamentParserService implements TournamentParser {

    @Inject
    HtmlParser htmlParser;

    @Inject
    DisciplineParser disciplineParser;

    private static final Logger log = LoggerFactory.getLogger(TournamentParserService.class);

    @Override
    public TournamentYearDTO parseTournamentYear(String year, HtmlElement content) {
        log.debug("Parsing tournament year {}", year);
        TournamentYearDTO tournamentYearDTO = new TournamentYearDTO(year);
        List<HtmlElement> tournamentElements = htmlParser.getAllTournaments(content);
        tournamentElements.forEach(tournamentElement -> {
            var tournamentInfo = parseTournamentInfo(tournamentElement);
            TournamentDTO tournamentDTO = new TournamentDTO(tournamentInfo);
            List<DisciplineDTO> disciplineDTOS = disciplineParser.parseDisciplines(tournamentElement);
            tournamentDTO.getTournamentDisciplines().addAll(disciplineDTOS);
            tournamentYearDTO.addTournament(tournamentDTO);
        });

        return tournamentYearDTO;
    }

    @Override
    public TournamentInfoDTO parseTournamentInfo(HtmlElement content) {
        log.debug("Parsing tournament info");
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
        return new TournamentInfoDTO(tournamentId, tournamentName, tournamentOrganisation, tournamentLocation, tournamentDate);
    }
}
