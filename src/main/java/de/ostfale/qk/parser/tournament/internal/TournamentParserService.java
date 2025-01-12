package de.ostfale.qk.parser.tournament.internal;

import de.ostfale.qk.parser.HtmlParser;
import de.ostfale.qk.parser.discipline.api.DisciplineParser;
import de.ostfale.qk.parser.tournament.api.TournamentParser;
import de.ostfale.qk.parser.tournament.internal.model.*;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.htmlunit.html.HtmlDivision;
import org.htmlunit.html.HtmlElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class TournamentParserService implements TournamentParser {

    @Inject
    HtmlParser htmlParser;

    @Inject
    DisciplineParser disciplineParser;

    private static final Logger log = LoggerFactory.getLogger(TournamentParserService.class);

    final String TOURNAMENT_DISCIPLINES = "//div[contains(@class, 'module module--card')]";
    final String TOURNAMENT_DISCIPLINE_INFO = "//h5[contains(@class, 'module-divider')]";
    final String TOURNAMENT_MATCH_GROUP = ".//li[contains(@class, 'match-group__item')]";

    final String MATCH_ROUND_NAME = ".//li[contains(@class, 'match__header-title-item')]";
    final String MATCH_ROUND_LOCATION_DATE = ".//li[contains(@class, 'match__footer-list-item')]";
    final String MATCH_ROUND_DURATION = ".//div[contains(@class, 'match__header-aside')]";


    @Override
    public TournamentYearDTO parseTournamentYear(String year, HtmlElement content) {
        log.debug("Parsing tournament year {}", year);
        TournamentYearDTO tournamentYearDTO = new TournamentYearDTO(year);
        List<HtmlElement> tournamentElements = htmlParser.getAllTournaments(content);
        tournamentElements.forEach(tournamentElement -> {
            var tournamentInfo = parseTournamentInfo(tournamentElement);
            TournamentDTO tournamentDTO = new TournamentDTO(tournamentInfo);
            List<TournamentDisciplineDTO> disciplineDTOS = disciplineParser.parseTournamentDisciplines(tournamentElement);
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
        String[] orgaAndLocation = tournamentOrganisationElement.asNormalizedText().split("\\|");

        var tournamentName = tournamentNameElement.asNormalizedText();
        var tournamentOrganisation = orgaAndLocation[0].trim();
        var tournamentLocation = orgaAndLocation[1].trim();
        var tournamentId = tournamentIdArray[tournamentIdArray.length - 1];
        var tournamentDate = tournamentDateElement.asNormalizedText();
        return new TournamentInfoDTO(tournamentId, tournamentName, tournamentOrganisation, tournamentLocation, tournamentDate);
    }

  /*  @Override
    public List<TournamentDisciplineDTO> parseTournamentDisciplines(HtmlElement moduleCard) {
        log.debug("Parsing tournament disciplines for info and matches");
        List<TournamentDisciplineDTO> disciplineList = new ArrayList<>();

        // read all discipline header starting with
        List<HtmlElement> disciplineHeaderElements = htmlParser.getAllDisciplineInfos(moduleCard);
        for (HtmlElement disciplineHeaderElement : disciplineHeaderElements) {

            TournamentDisciplineDTO tournamentDisciplineDTO = getTournamentDisciplineInfoDTO(disciplineHeaderElement);
            disciplineList.add(tournamentDisciplineDTO);
        }
        return disciplineList;
    }*/


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

    private TournamentDisciplineDTO getTournamentDisciplineInfoDTO(HtmlElement headerElement) {
        String[] disciplineAge = headerElement.asNormalizedText().split(" ");
        var disciplineName = disciplineAge[1];
        var disciplineAgeGroup = disciplineAge[2];
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
