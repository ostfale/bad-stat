package de.ostfale.qk.parser.tournament.internal;

import de.ostfale.qk.parser.tournament.api.TournamentParser;
import de.ostfale.qk.parser.tournament.internal.model.TournamentDisciplineDTO;
import de.ostfale.qk.parser.tournament.internal.model.TournamentInfoDTO;
import de.ostfale.qk.parser.tournament.internal.model.TournamentMatchInfoDTO;
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
    public TournamentInfoDTO parseHeader(HtmlDivision content) {
        log.info("Parsing tournament header ");

        HtmlElement tournamentNameElement = content.getFirstByXPath(TOURNAMENT_NAME);
        HtmlElement tournamentOrgElement = content.getFirstByXPath(TOURNAMENT_ORGANISATION);
        HtmlElement tournamentDateElement = content.getFirstByXPath(TOURNAMENT_DATE);
        List<HtmlElement> tournamentIdElements = content.getByXPath(TOURNAMENT_ID);

        //  <a href="/sport/tournament?id=DDAD417D-28AD-4C58-A5BD-38D34E647136" class="media__img">
        var tournamentIdArray = tournamentIdElements.getFirst().getAttribute("href").split("=");
        var orgaAndLocation = tournamentOrgElement.asNormalizedText().split("\\|");

        var tournamentName = tournamentNameElement.asNormalizedText();
        var tournamentOrganisation = orgaAndLocation[0].trim();
        var tournamentLocation = orgaAndLocation[1].trim();
        var tournamentId = tournamentIdArray[tournamentIdArray.length - 1];
        var tournamentDate = tournamentDateElement.asNormalizedText();

        return new TournamentInfoDTO(tournamentId, tournamentName, tournamentOrganisation, tournamentLocation, tournamentDate);
    }

    @Override
    public List<TournamentDisciplineDTO> parseDisciplines(HtmlDivision content) {
        List<HtmlDivision> moduleCards = content.getByXPath(TOURNAMENT_DISCIPLINES);   // find all module--card for tournament
        List<TournamentDisciplineDTO> disciplineList = new ArrayList<>();
        moduleCards.forEach(moduleCard -> {

            // read header info of the discipline which contains a list of matches
            var tournamentDisciplineInfo = getTournamentDisciplineInfoDTO(moduleCard);
            disciplineList.add(tournamentDisciplineInfo);

            // each match group has a round, a timestamp (when played) and a location (where played)
            List<HtmlElement> matchGroupList = content.getByXPath(TOURNAMENT_MATCH_GROUP);
            matchGroupList.forEach(matchGroup -> {

                var tournamentMatchInfo = getTournamentMatchInfo(matchGroup);

                // read a match info with the player and the results
            //    var matchDTO = getTournamentMatchDTO(matchGroup);
             //   tournamentMatchInfo.setTournamentMatchDTO(matchDTO);

                tournamentDisciplineInfo.addMatchInfo(tournamentMatchInfo);
            });
        });
        return disciplineList;
    }

    final String MATCH_ROUND_PLAYER = "//div[contains(@class, 'match__body')]";
    final String MATCH_ROUND_PLAYER_WON = ".//div[contains(@class, 'match__row has-won')]";
    final String MATCH_ROUND_PLAYER_LOST = "//div[contains(@class, 'match__row')]";
    final String MATCH_PLAYER_INFO = "//span[contains(@class, 'match__row-title-value-content')]";

    private TournamentMatchInfoDTO getTournamentMatchInfo(HtmlElement matchGroup) {
        HtmlElement matchRoundNameDiv = matchGroup.getFirstByXPath(MATCH_ROUND_NAME);
        HtmlElement matchRoundDurationDiv = matchGroup.getFirstByXPath(MATCH_ROUND_DURATION);
        List<HtmlElement> matchRoundDateLocDiv = matchGroup.getByXPath(MATCH_ROUND_LOCATION_DATE);

        var matchRoundName = matchRoundNameDiv != null ? matchRoundNameDiv.asNormalizedText() : "";
        var matchRoundDate = matchRoundDateLocDiv.getFirst() != null ? matchRoundDateLocDiv.getFirst().asNormalizedText() : "";
        var matchRoundCourt = matchRoundDateLocDiv.getLast() != null ? matchRoundDateLocDiv.getLast().asNormalizedText() : "";
        var matchRoundDuration = matchRoundDurationDiv != null ? matchRoundDurationDiv.asNormalizedText() : "";
        var matchInfo = new TournamentMatchInfoDTO(matchRoundName, matchRoundDate, matchRoundCourt, matchRoundDuration);
        log.debug("Tournament match info: {}", matchInfo);
        return matchInfo;
    }

    private TournamentDisciplineDTO getTournamentDisciplineInfoDTO(HtmlDivision moduleCard) {
        HtmlElement discipline = moduleCard.getFirstByXPath(TOURNAMENT_DISCIPLINE_INFO);
        String[] disciplineAge = discipline.asNormalizedText().split(" ");
        var disciplineName = disciplineAge[0];
        var disciplineAgeGroup = disciplineAge[1];
        var disciplineInfo = new TournamentDisciplineDTO(disciplineName, disciplineAgeGroup);
        log.debug("Tournament discipline info: {}", disciplineInfo);
        return disciplineInfo;
    }


   /* private List<TournamentMatchDTO> getTournamentMatchDTO(HtmlElement matchElement) {
        List<TournamentMatchDTO> allMatchResults = new ArrayList<>();

        List<HtmlDivision> allMatches = matchElement.getByXPath(MATCH_ROUND_PLAYER);
        allMatches.forEach(match -> {
            HtmlElement matchWon = match.getFirstByXPath(MATCH_ROUND_PLAYER_WON);
            String playerWon = matchWon.asNormalizedText().split("\n")[0];
            String[] matchInfo = match.asNormalizedText().split("\n");
            String playerLost = playerWon.equalsIgnoreCase(matchInfo[0]) ? matchInfo[1] : matchInfo[0];
            MatchPlayerDTO playerHasWon = new MatchPlayerDTO(playerWon, "");
            MatchPlayerGroupDTO playerGroupWon = new MatchPlayerGroupDTO(playerHasWon, true);
            MatchPlayerDTO playerHasLost = new MatchPlayerDTO(playerLost, "");
            MatchPlayerGroupDTO playerGroupLost = new MatchPlayerGroupDTO(playerHasLost, false);

            TournamentMatchDTO tournamentMatchDTO = new TournamentMatchDTO(playerGroupWon, playerGroupLost, List.of());
            allMatchResults.add(tournamentMatchDTO);
        });
        return allMatchResults;
    }*/
}
