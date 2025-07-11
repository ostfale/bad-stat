package de.ostfale.qk.parser.web.discipline;

import de.ostfale.qk.domain.discipline.DisciplineInfo;
import de.ostfale.qk.domain.discipline.DisciplineType;
import de.ostfale.qk.domain.discipline.TournamentDiscipline;
import de.ostfale.qk.domain.match.DisciplineMatch;
import de.ostfale.qk.parser.HtmlParserException;
import de.ostfale.qk.parser.web.match.WebMatchParserService;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.htmlunit.html.HtmlElement;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class WebDisciplineParserService implements WebDisciplineParser {

    private final WebDisciplineInfoParserService webDisciplineInfoParserService;
    private final WebMatchParserService webMatchParserService;

    public WebDisciplineParserService(WebDisciplineInfoParserService webDisciplineInfoParserService, WebMatchParserService webMatchParserService) {
        this.webDisciplineInfoParserService = webDisciplineInfoParserService;
        this.webMatchParserService = webMatchParserService;
    }

    public List<TournamentDiscipline> parseTournamentDisciplines(HtmlElement moduleCardElement) throws HtmlParserException {
        Log.debugf("WebDisciplineParser :: parse tournament matches for all disciplines");

        List<TournamentDiscipline> tournamentDisciplines = extractAllDisciplinesWithInfo(moduleCardElement);
        List<DisciplineInfo> disciplineSubInfoList = extractSubHeaderInformation(extractDisciplineSubString(moduleCardElement));
        List<HtmlElement> disciplineMatchesList = extractAllDisciplinesMatchElements(moduleCardElement);

        for (int currentIndex = 0; currentIndex < tournamentDisciplines.size(); currentIndex++) {
            processDiscipline(
                    tournamentDisciplines.get(currentIndex),
                    disciplineSubInfoList,
                    disciplineMatchesList,
                    currentIndex
            );
        }
        return tournamentDisciplines;
    }

    private void processDiscipline(TournamentDiscipline discipline, List<DisciplineInfo> disciplineSubInfoList, List<HtmlElement> disciplineMatchesList, int currentIndex) {
        DisciplineType disciplineType = discipline.getDisciplineType();
        Log.debugf("Processing discipline type: %s", disciplineType.name());

        DisciplineInfo currentSubInfo = disciplineSubInfoList.get(currentIndex);
        DisciplineInfo nextSubInfo = getNextDisciplineSubInfo(disciplineSubInfoList, currentIndex);

        processEliminationMatches(discipline, disciplineType, currentSubInfo, disciplineMatchesList.get(currentIndex));
        processGroupMatches(discipline, disciplineType, currentSubInfo, nextSubInfo, disciplineMatchesList, currentIndex);
    }

    private void processEliminationMatches(TournamentDiscipline discipline, DisciplineType disciplineType, DisciplineInfo currentSubInfo, HtmlElement matchElement) {
        if (disciplineType == currentSubInfo.disciplineType()) {
            Log.debugf("Processing elimination matches for: %s", disciplineType.name());
            discipline.setDisciplineName(currentSubInfo.originalString());
            discipline.getEliminationMatches().addAll(processMatches(disciplineType, matchElement));
        }
    }

    private void processGroupMatches(TournamentDiscipline discipline, DisciplineType disciplineType, DisciplineInfo currentSubInfo,
                                     DisciplineInfo nextSubInfo, List<HtmlElement> matchesList, int currentIndex) {
        if (nextSubInfo != null && nextSubInfo.isGroupSubHeader()) {
            Log.debugf("Processing group matches for: %s", disciplineType.name());
            discipline.setDisciplineName(currentSubInfo.originalString());
            discipline.setGroupName(nextSubInfo.originalString());

            var groupMatchElement = matchesList.get(currentIndex + 1);
            discipline.getGroupMatches().addAll(processMatches(disciplineType, groupMatchElement));
        }
    }

    private DisciplineInfo getNextDisciplineSubInfo(List<DisciplineInfo> subInfoList, int currentIndex) {
        int nextIndex = currentIndex + 1;
        return nextIndex < subInfoList.size() ? subInfoList.get(nextIndex) : null;
    }


    private List<DisciplineMatch> processMatches(DisciplineType disciplineType, HtmlElement disciplineMatchGroupElement) {
        Log.debugf("WebDisciplineParser :: process matches ->  DisciplineType %s", disciplineType.name());
        List<DisciplineMatch> matches = new ArrayList<>();
        List<HtmlElement> disciplineMatches = htmlStructureParser.getAllMatchesForMatchGroupContainer(disciplineMatchGroupElement);
        for (HtmlElement disciplineMatch : disciplineMatches) {
            switch (disciplineType) {
                case SINGLE -> {
                    Log.debug("WebMatchParser :: Parse single matches!");
                    DisciplineMatch singleMatch = webMatchParserService.parseSingleMatch(disciplineMatch);
                    matches.add(singleMatch);
                }
                case DOUBLE -> {
                    Log.debug("WebMatchParser :: Parse double matches!");
                    DisciplineMatch doubleMatch = webMatchParserService.parseDoubleMatch(disciplineMatch);
                    matches.add(doubleMatch);
                }
                case MIXED -> {
                    Log.debug("WebMatchParser :: Parse mixed matches");
                    DisciplineMatch mixedMatch = webMatchParserService.parseMixedMatch(disciplineMatch);
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
