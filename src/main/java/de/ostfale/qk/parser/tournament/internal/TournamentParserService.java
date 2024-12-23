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
    final String TOURNAMENT_MATCH_GROUP = ".//li[contains(@class, 'match-group__item')]";

    final String MATCH_ROUND_NAME = ".//li[contains(@class, 'match__header-title-item')]";
    final String MATCH_ROUND_LOCATION_DATE = ".//li[contains(@class, 'match__footer-list-item')]";
    final String MATCH_ROUND_DURATION = ".//div[contains(@class, 'match__header-aside')]";

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
            // read header info of the discipline which contains a list of matches
            var tournamentDisciplineInfo = getTournamentDisciplineInfoDTO(moduleCard);
            disciplineList.add(tournamentDisciplineInfo);

            // each match group has a round, a timestamp (when played) and a location (where played)
            List<HtmlElement> matchGroupList = content.getByXPath(TOURNAMENT_MATCH_GROUP);
            matchGroupList.forEach(matchGroup -> {
                tournamentDisciplineInfo.addMatchInfo(getTournamentMatchInfo(matchGroup));
            });
        });
        return disciplineList;
    }

    private TournamentMatchInfo getTournamentMatchInfo(HtmlElement matchGroup) {
        HtmlElement matchRoundNameDiv = matchGroup.getFirstByXPath(MATCH_ROUND_NAME);
        HtmlElement matchRoundDurationDiv = matchGroup.getFirstByXPath(MATCH_ROUND_DURATION);
        List<HtmlElement> matchRoundDateLocDiv = matchGroup.getByXPath(MATCH_ROUND_LOCATION_DATE);

        var matchRoundName = matchRoundNameDiv != null ? matchRoundNameDiv.asNormalizedText() :"";
        var matchRoundDate = matchRoundDateLocDiv.getFirst() != null ? matchRoundDateLocDiv.getFirst().asNormalizedText() : "";
        var matchRoundCourt = matchRoundDateLocDiv.getLast() != null ? matchRoundDateLocDiv.getLast().asNormalizedText() : "";
        var matchRoundDuration = matchRoundDurationDiv != null ? matchRoundDurationDiv.asNormalizedText() : "";
        var matchInfo = new TournamentMatchInfo(matchRoundName, matchRoundDate, matchRoundCourt, matchRoundDuration);
        log.debug("Tournament match info: {}", matchInfo);
        return matchInfo;
    }

    private TournamentDisciplineInfoDTO getTournamentDisciplineInfoDTO(HtmlDivision moduleCard) {
        HtmlElement discipline = moduleCard.getFirstByXPath(TOURNAMENT_DISCIPLINE_INFO);
        String[] disciplineAge = discipline.asNormalizedText().split(" ");
        var disciplineName = disciplineAge[0];
        var disciplineAgeGroup = disciplineAge[1];
        var disciplineInfo = new TournamentDisciplineInfoDTO(disciplineName, disciplineAgeGroup);
        log.debug("Tournament discipline info: {}", disciplineInfo);
        return disciplineInfo;
    }
}
