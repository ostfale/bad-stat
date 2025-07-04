package de.ostfale.qk.parser.web.discipline;

import de.ostfale.qk.domain.discipline.*;
import de.ostfale.qk.domain.match.DisciplineMatch;
import de.ostfale.qk.domain.tournament.Tournament;
import de.ostfale.qk.parser.match.internal.WebMatchParserService;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.htmlunit.html.HtmlElement;

import java.util.Comparator;
import java.util.List;

@ApplicationScoped
public class WebDisciplineParserService implements WebDisciplineParser{

    private final WebDisciplineInfoParserService webDisciplineInfoParserService;

    @Inject
    WebMatchParserService webMatchParserService;

    public WebDisciplineParserService(WebDisciplineInfoParserService webDisciplineInfoParserService) {
        this.webDisciplineInfoParserService = webDisciplineInfoParserService;
    }

    public void parseDisciplines(Tournament tournament, HtmlElement moduleCardElement) {
        Log.debugf("WebDisciplineParser :: parse disciplines website -> Tournament %s", tournament.getTournamentInfo().tournamentName());
        List<HtmlElement> disciplineOrGroupName = htmlStructureParser.getDisciplineOrGroupName(moduleCardElement);
        List<HtmlElement> disciplineElements = htmlStructureParser.getAllDisciplineInfos(moduleCardElement);
        disciplineElements.forEach(disciplineElement -> webDisciplineInfoParserService.parseDisciplineInfos(tournament, disciplineElement));

        if (disciplineElements.size() == disciplineOrGroupName.size()) {
            Log.debugf("WebDisciplineParser :: Pure elimination disciplines found for tournament: %s", tournament.getTournamentInfo().tournamentName());
            parseEliminationMatches(tournament, moduleCardElement);
        } else if (disciplineElements.size() < disciplineOrGroupName.size()) {
            Log.debugf("WebDisciplineParser :: Combined elimination disciplines found for tournament: %s", tournament.getTournamentInfo().tournamentName());
            parseCombinedMatches(tournament, moduleCardElement);
        }
    }

    private void parseEliminationMatches(Tournament tournament, HtmlElement moduleCardElement) {
        Log.debugf("WebDisciplineParser :: parse elimination matches website -> Tournament %s", tournament.getTournamentInfo().tournamentName());

        // sort order of disciplines played in the tournament (filter for played disciplines first)
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
        });
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
                    SingleDiscipline singleDiscipline = tournament.getSingleDiscipline();
                    singleDiscipline.addEliminationMatch(singleMatch);
                }
                case DOUBLE -> {
                    Log.debugf("WebMatchParser :: Parse double matches for -> Tournament %s", tournament.getTournamentInfo().tournamentName());
                    DisciplineMatch doubleMatch = webMatchParserService.parseDoubleMatch(disciplineMatch);
                    DoubleDiscipline doubleDiscipline = tournament.getDoubleDiscipline();
                    doubleDiscipline.addEliminationMatch(doubleMatch);
                }
                case MIXED -> {
                    Log.debugf("WebMatchParser :: Parse mixed matches for -> Tournament %s", tournament.getTournamentInfo().tournamentName());
                    DisciplineMatch mixedMatch = webMatchParserService.parseDoubleMatch(disciplineMatch);
                    MixedDiscipline mixedDiscipline = tournament.getMixedDiscipline();
                    mixedDiscipline.addEliminationMatch(mixedMatch);
                }
                default -> {
                    Log.errorf("WebMatchParser :: Unknown discipline type: %s", disciplineType.name());
                }
            }
        }
    }
}
