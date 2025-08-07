package de.ostfale.qk.parser.web.discipline;

import de.ostfale.qk.domain.discipline.DisciplineInfo;
import de.ostfale.qk.domain.discipline.DisciplineType;
import de.ostfale.qk.domain.discipline.TournamentDiscipline;
import de.ostfale.qk.domain.match.DisciplineMatch;
import de.ostfale.qk.parser.HtmlParserException;
import de.ostfale.qk.parser.web.match.MatchParserService;
import de.ostfale.qk.parser.web.match.MatchPlayers;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.htmlunit.html.HtmlElement;

import java.util.ArrayList;
import java.util.List;

import static de.ostfale.qk.domain.discipline.DisciplineType.*;

@ApplicationScoped
public class WebDisciplineParserService implements WebDisciplineParser {

    private final WebDisciplineInfoParserService webDisciplineInfoParserService;
    private final MatchParserService matchParserService;

    public WebDisciplineParserService(MatchParserService matchParserService, WebDisciplineInfoParserService webDisciplineInfoParserService) {
        this.matchParserService = matchParserService;
        this.webDisciplineInfoParserService = webDisciplineInfoParserService;
    }

    private List<MatchPlayers> getMatchPlayersForAllMatches(String currentPlayer, List<HtmlElement> allMatchGroups) throws HtmlParserException {
        List<MatchPlayers> matchPlayersList = new ArrayList<>();
        for (HtmlElement matchGroupElement : allMatchGroups) {
            //    Log.debug("WebDisciplineParserService :: get match players for match group with player -> %s", currentPlayer);
            List<HtmlElement> disciplineMatches = htmlStructureParser.getAllMatchesForMatchGroupContainer(matchGroupElement);
            var lastMatch = disciplineMatches.getLast();
            var matchPlayer = matchParserService.parseMatch(currentPlayer, lastMatch);
            matchPlayersList.add(matchPlayer);
        }
        return matchPlayersList;
    }

    public List<TournamentDiscipline> parseTournamentDisciplines(String currentPlayer, HtmlElement moduleCardElement) throws HtmlParserException {
        Log.debugf("WebDisciplineParser :: parse tournament matches for all disciplines for player: %s", currentPlayer);

        List<HtmlElement> disciplineSubInfoElements = extractDisciplineSubString(moduleCardElement);
        List<HtmlElement> disciplineMatchesList = extractAllDisciplinesMatchElements(moduleCardElement);

        List<TournamentDiscipline> tournamentDisciplines = extractAllDisciplinesWithInfo(moduleCardElement);
        List<DisciplineInfo> disciplineSubInfoList = extractSubHeaderInformation(disciplineSubInfoElements);

        var matchPlayersList = getMatchPlayersForAllMatches(currentPlayer, disciplineMatchesList);

        int subInfoIndex = 0;
        for (int disciplineIndex = 0; disciplineIndex < tournamentDisciplines.size(); disciplineIndex++) {
            TournamentDiscipline currentTournamentDiscipline = tournamentDisciplines.get(disciplineIndex);
            DisciplineType currentDisciplineType = tournamentDisciplines.get(disciplineIndex).getDisciplineType();
            Log.debugf("WebDisciplineParserService :: parse tournament matches for discipline type %s", currentDisciplineType.name());

            DisciplineInfo currentDisciplineSubInfo = disciplineSubInfoList.get(subInfoIndex);

            /* Start parsing the matches and differentiate between elimination and group matches  */

            // first case (default): general discipline type is the same as subheader discipline type
            if (currentDisciplineType == currentDisciplineSubInfo.disciplineType()) {
                Log.debugf("WebDisciplineParserService :: current discipline type is the same as subheader discipline type -> %s", currentDisciplineType.name());
                currentTournamentDiscipline.setDisciplineName(currentDisciplineSubInfo.originalString());
                var disciplineMatchGroupElement = disciplineMatchesList.get(subInfoIndex++);
                var matches = processMatches(currentDisciplineType, disciplineMatchGroupElement);
                currentTournamentDiscipline.getEliminationMatches().addAll(matches);
            }

            if (isNextMatchGroupSameDisciplineType(matchPlayersList, subInfoIndex-1)) {
                Log.debug("WebDisciplineParserService :: next match group has same match players as current one!");
                currentDisciplineSubInfo = disciplineSubInfoList.get(subInfoIndex);
                currentTournamentDiscipline.setDisciplineName(currentDisciplineSubInfo.originalString());
                currentTournamentDiscipline.setGroupName(currentDisciplineSubInfo.originalString());
                var disciplineMatchGroupElement = disciplineMatchesList.get(subInfoIndex++);
                var matches = processMatches(currentDisciplineType, disciplineMatchGroupElement);
                currentTournamentDiscipline.getGroupMatches().addAll(matches);
                continue;
            }

            if (currentDisciplineSubInfo.isGroupSubHeader()) {
                Log.debug("WebDisciplineParserService :: current discipline type contains group only!");
                var discipline = currentDisciplineType.getDisplayString() + " / " + currentDisciplineSubInfo.originalString();
                currentTournamentDiscipline.setDisciplineName(discipline);
                var disciplineMatchGroupElement = disciplineMatchesList.get(disciplineIndex);
                var matches = processMatches(currentDisciplineType, disciplineMatchGroupElement);
                currentTournamentDiscipline.getGroupMatches().addAll(matches);
            }
        }
        return tournamentDisciplines;
    }

    private boolean isNextMatchGroupSameDisciplineType(List<MatchPlayers> matchPlayersList, int currentDisciplineIndex) {
        var isCurrentAndNextMatchGroupSameDisciplineType = false;
        MatchPlayers currentMatchPlayer = matchPlayersList.get(currentDisciplineIndex);
        int nextDisciplineIndex = currentDisciplineIndex + 1;
        if (nextDisciplineIndex < matchPlayersList.size()) {
            MatchPlayers nextMatchPlayer = matchPlayersList.get(nextDisciplineIndex);
            isCurrentAndNextMatchGroupSameDisciplineType = currentMatchPlayer.equals(nextMatchPlayer);
        }
        Log.debugf("WebDisciplineParser :: is current and next match group same discipline type -> %s", isCurrentAndNextMatchGroupSameDisciplineType);
        return isCurrentAndNextMatchGroupSameDisciplineType;
    }

    private List<DisciplineMatch> processMatches(DisciplineType disciplineType, HtmlElement disciplineMatchGroupElement) throws HtmlParserException {
        Log.debugf("WebDisciplineParser :: process matches ->  DisciplineType %s", disciplineType.name());
        List<DisciplineMatch> matches = new ArrayList<>();
        List<HtmlElement> disciplineMatches = htmlStructureParser.getAllMatchesForMatchGroupContainer(disciplineMatchGroupElement);
        for (HtmlElement disciplineMatch : disciplineMatches) {
            switch (disciplineType) {
                case SINGLE -> {
                    Log.debug("WebMatchParser :: Parse single matches!");
                    DisciplineMatch singleMatch = matchParserService.parseMatch(SINGLE, disciplineMatch);
                    matches.add(singleMatch);
                }
                case DOUBLE -> {
                    Log.debug("WebMatchParser :: Parse double matches!");
                    DisciplineMatch doubleMatch = matchParserService.parseMatch(DOUBLE, disciplineMatch);
                    matches.add(doubleMatch);
                }
                case MIXED -> {
                    Log.debug("WebMatchParser :: Parse mixed matches");
                    DisciplineMatch mixedMatch = matchParserService.parseMatch(MIXED, disciplineMatch);
                    matches.add(mixedMatch);
                }
                default -> {
                    Log.errorf("WebMatchParser :: Unknown discipline type: %s", disciplineType.name());
                }
            }
        }
        return matches;
    }

    private List<DisciplineInfo> extractSubHeaderInformation(List<HtmlElement> subHeaderElementList) throws HtmlParserException {
        Log.debugf("WebDisciplineParser :: extract sub header information");
        List<DisciplineInfo> disciplineInfos = new ArrayList<>();
        for (HtmlElement subHeaderElement : subHeaderElementList) {
            var subHeaderInfo = webDisciplineInfoParserService.extractDisciplineSubHeaderInfo(subHeaderElement.asNormalizedText());
            disciplineInfos.add(subHeaderInfo);
        }
        return disciplineInfos;
    }

    private List<TournamentDiscipline> extractAllDisciplinesWithInfo(HtmlElement moduleCardElement) throws HtmlParserException {
        List<HtmlElement> disciplineInfoElements = extractDisciplineInfo(moduleCardElement);
        List<TournamentDiscipline> tournamentDisciplines = new ArrayList<>();
        for (HtmlElement disciplineInfoElement : disciplineInfoElements) {
            TournamentDiscipline tournamentDiscipline = new TournamentDiscipline();
            var disciplineInfo = webDisciplineInfoParserService.extractDisciplineHeaderInfo(disciplineInfoElement.asNormalizedText());
            tournamentDiscipline.setDisciplineInfo(disciplineInfo);
            tournamentDisciplines.add(tournamentDiscipline);
        }
        return tournamentDisciplines;
    }
}
