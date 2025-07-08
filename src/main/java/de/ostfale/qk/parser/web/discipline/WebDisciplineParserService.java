package de.ostfale.qk.parser.web.discipline;

import de.ostfale.qk.domain.discipline.Discipline;
import de.ostfale.qk.domain.discipline.DisciplineInfo;
import de.ostfale.qk.domain.discipline.DisciplineType;
import de.ostfale.qk.domain.discipline.TournamentDiscipline;
import de.ostfale.qk.domain.match.DisciplineMatch;
import de.ostfale.qk.domain.tournament.Tournament;
import de.ostfale.qk.parser.HtmlParserException;
import de.ostfale.qk.parser.web.match.WebMatchParserService;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.htmlunit.html.HtmlElement;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class WebDisciplineParserService implements WebDisciplineParser{

    private final WebDisciplineInfoParserService webDisciplineInfoParserService;
    private final WebMatchParserService webMatchParserService;

    public WebDisciplineParserService(WebDisciplineInfoParserService webDisciplineInfoParserService,WebMatchParserService webMatchParserService) {
        this.webDisciplineInfoParserService = webDisciplineInfoParserService;
        this.webMatchParserService = webMatchParserService;
    }

    public void parseDisciplines(Tournament tournament, HtmlElement moduleCardElement) throws HtmlParserException {
        Log.debugf("WebDisciplineParser :: parse disciplines website -> Tournament %s", tournament.getTournamentInfo().tournamentName());

        List<TournamentDiscipline> tournamentDisciplines = extractAllDisciplinesWithInfo(moduleCardElement);
        tournament.getDisciplines().addAll(tournamentDisciplines);

        List<HtmlElement> disciplineInfoElements = extractDisciplineInfo(moduleCardElement);
        List<HtmlElement> disciplineSubInfoElements = extractDisciplineSubString(moduleCardElement);
        var disciplineSubInfos = extractSubHeaderInformation(disciplineSubInfoElements);

        if (disciplineInfoElements.size() == disciplineSubInfoElements.size()) {
            Log.debugf("WebDisciplineParser :: Pure elimination disciplines found for tournament: %s", tournament.getTournamentInfo().tournamentName());
            parseEliminationMatches(tournament, moduleCardElement);
        } else if (disciplineInfoElements.size() < disciplineSubInfoElements.size()) {
            Log.debugf("WebDisciplineParser :: Combined elimination disciplines found for tournament: %s", tournament.getTournamentInfo().tournamentName());
            parseCombinedMatches(tournament, moduleCardElement);
        }
    }

    private List<DisciplineInfo> extractSubHeaderInformation(List<HtmlElement> subHeaderElementList) throws HtmlParserException {
        Log.debugf("WebDisciplineParser :: extract sub header information");
        List<DisciplineInfo> disciplineInfos = new ArrayList<>();
        for(HtmlElement subHeaderElement : subHeaderElementList) {
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

    private void parseEliminationMatches(Tournament tournament, HtmlElement moduleCardElement) {
        Log.debugf("WebDisciplineParser :: parse elimination matches website -> Tournament %s", tournament.getTournamentInfo().tournamentName());

      /*  // sort order of disciplines played in the tournament (filter for played disciplines first)
        var sortedDisciplines = tournament.getDisciplines().stream()
                .filter(discipline -> discipline.getDisciplineOrder() != DisciplineOrder.NO_ORDER)
                .sorted(Comparator.comparing(Discipline::getDisciplineOrder)).toList();

        // contains all matches grouped by the played discipline (3 disciplines played = 3 groups found)
        List<HtmlElement> disciplineMatchesList = htmlStructureParser.getAllDisciplineMatches(moduleCardElement);

        // first discipline processes first list of matches from match-group
        sortedDisciplines.forEach(discipline -> {
            var disciplineType = discipline.getDisciplineType();
            var disciplineIndex = discipline.getDisciplineOrder().ordinal() - 1;
            var matchGroupForDiscipline = disciplineMatchesList.get(disciplineIndex);
            processDisciplineMatches(tournament, disciplineType, matchGroupForDiscipline);
        });*/
    }

    private void parseCombinedMatches(Tournament tournament, HtmlElement moduleCardElement) {
        Log.debugf("WebMatchParser :: parse combined matches website -> Tournament %s", tournament.getTournamentInfo().tournamentName());
    }

    private void processDisciplineMatches(Tournament tournament, DisciplineType disciplineType, HtmlElement disciplineMatchGroupElement) {
        Log.debugf("WebDisciplineParser :: process discipline matcher -> Tournament %s | DisciplineType %s", tournament.getTournamentInfo().tournamentName(), disciplineType.name());

        List<HtmlElement> disciplineMatches = htmlStructureParser.getAllMatchesForMatchGroupContainer(disciplineMatchGroupElement);
        for (HtmlElement disciplineMatch : disciplineMatches) {
            switch (disciplineType) {
                case SINGLE -> {
                    Log.debugf("WebMatchParser :: Parse single matches for -> Tournament %s", tournament.getTournamentInfo().tournamentName());

                    DisciplineMatch singleMatch = webMatchParserService.parseSingleMatch(disciplineMatch);
                    Discipline singleDiscipline = tournament.getSingleDiscipline();
                    singleDiscipline.addEliminationMatch(singleMatch);
                }
                case DOUBLE -> {
                    Log.debugf("WebMatchParser :: Parse double matches for -> Tournament %s", tournament.getTournamentInfo().tournamentName());
                    DisciplineMatch doubleMatch = webMatchParserService.parseDoubleMatch(disciplineMatch);
                    Discipline doubleDiscipline = tournament.getDoubleDiscipline();
                    doubleDiscipline.addEliminationMatch(doubleMatch);
                }
                case MIXED -> {
                    Log.debugf("WebMatchParser :: Parse mixed matches for -> Tournament %s", tournament.getTournamentInfo().tournamentName());
                    DisciplineMatch mixedMatch = webMatchParserService.parseMixedMatch(disciplineMatch);
                    Discipline mixedDiscipline = tournament.getMixedDiscipline();
                    mixedDiscipline.addEliminationMatch(mixedMatch);
                }
                default -> {
                    Log.errorf("WebMatchParser :: Unknown discipline type: %s", disciplineType.name());
                }
            }
        }
    }
}
