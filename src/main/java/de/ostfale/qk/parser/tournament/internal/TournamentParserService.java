package de.ostfale.qk.parser.tournament.internal;

import de.ostfale.qk.parser.tournament.api.TournamentParser;
import jakarta.inject.Singleton;
import org.htmlunit.html.HtmlDivision;
import org.htmlunit.html.HtmlElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class TournamentParserService implements TournamentParser {

    private static final Logger log = LoggerFactory.getLogger(TournamentParserService.class);

    final String TOURNAMENT_NAME = ".//h4[contains(@class, 'media__title media__title--medium')]";
    final String TOURNAMENT_ORGANISATION = ".//small[contains(@class, 'media__subheading')]";
    final String TOURNAMENT_DATE = ".//small[contains(@class, 'media__subheading media__subheading--muted')]";
    final String TOURNAMENT_ID = "//a[contains(@class, 'media__img')]";

    final String TOURNAMENT_DISCIPLINES = "//div[contains(@class, 'module module--card')]";
    final String TOURNAMENT_DISCIPLINE_INFO = "//h5[contains(@class, 'module-divider')]";
    final String TOURNAMENT_DISCIPLINE_INFO_LINK = "//a[contains(@class, 'nav-link')]";

    @Override
    public TournamentHeaderInfoDTO parseHeader(HtmlDivision content) {
        log.info("Parsing tournament header ");

        HtmlElement tournamentNameElement = content.getFirstByXPath(TOURNAMENT_NAME);
        HtmlElement tournamentOrgElement = content.getFirstByXPath(TOURNAMENT_ORGANISATION);
        HtmlElement tournamentDateElement = content.getFirstByXPath(TOURNAMENT_DATE);
        List<HtmlElement>tournamentIdElements = content.getByXPath(TOURNAMENT_ID);

        //  <a href="/sport/tournament?id=DDAD417D-28AD-4C58-A5BD-38D34E647136" class="media__img">
        var tournamentIdArray = tournamentIdElements.getFirst().getAttribute("href").split("=");
        var orgaAndLocation = tournamentOrgElement.asNormalizedText().split("\\|");

        var tournamentName = tournamentNameElement.asNormalizedText();
        var tournamentOrganisation = orgaAndLocation[0].trim();
        var tournamentLocation = orgaAndLocation[1].trim();
        var tournamentId = tournamentIdArray[ tournamentIdArray.length - 1];
        var tournamentDate = tournamentDateElement.asNormalizedText();

        return new TournamentHeaderInfoDTO(tournamentId,tournamentName, tournamentOrganisation, tournamentLocation,  tournamentDate);
    }

    @Override
    public List<TournamentDisciplineInfoDTO> parseDisciplines(HtmlDivision content) {
        List<HtmlDivision> moduleCards = content.getByXPath(TOURNAMENT_DISCIPLINES);   // find all module--card for tournament
        List<TournamentDisciplineInfoDTO> disciplineList = new ArrayList<>();
        moduleCards.forEach(moduleCard -> {
            var tournamentDisciplineInfo = getTournamentDisciplineInfoDTO(moduleCard);
            disciplineList.add(tournamentDisciplineInfo);
        });
        return disciplineList;
    }

    private TournamentDisciplineInfoDTO getTournamentDisciplineInfoDTO(HtmlDivision moduleCard) {
        HtmlElement discipline = moduleCard.getFirstByXPath(TOURNAMENT_DISCIPLINE_INFO);
        String[] disciplineAge = discipline.asNormalizedText().split(" ");
        var disciplineName = disciplineAge[0];
        var disciplineAgeGroup = disciplineAge[1];
        return new TournamentDisciplineInfoDTO(disciplineName, disciplineAgeGroup);
    }
}
