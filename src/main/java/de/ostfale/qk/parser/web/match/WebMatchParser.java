package de.ostfale.qk.parser.web.match;

import de.ostfale.qk.domain.discipline.*;
import de.ostfale.qk.domain.match.DisciplineMatch;
import de.ostfale.qk.domain.tournament.Tournament;
import de.ostfale.qk.parser.discipline.DisciplineAgeParserService;
import de.ostfale.qk.parser.web.HtmlStructureParser;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.htmlunit.html.HtmlElement;

import java.util.Comparator;
import java.util.List;

@ApplicationScoped
public class WebMatchParser {

    @Inject
    HtmlStructureParser htmlStructureParser;

    @Inject
    DisciplineAgeParserService disciplineAgeParserService;

    public void parseEliminationMatches(Tournament tournament, HtmlElement moduleCardElement) {
        Log.debugf("WebMatchParser :: parse elimination matches website -> Tournament %s", tournament.getTournamentInfo().tournamentName());

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

    private void processDisciplineMatches(Tournament tournament, DisciplineType disciplineType, HtmlElement disciplineMatchGroupElement) {
        Log.debugf("WebMatchParser :: process discipline matcher -> Tournament %s | DisciplineType %s", tournament.getTournamentInfo().tournamentName(), disciplineType.name());

        List<HtmlElement> disciplineMatches = htmlStructureParser.getAllMatchesForMatchGroupContainer(disciplineMatchGroupElement);
        for (HtmlElement disciplineMatch : disciplineMatches) {
            switch (disciplineType) {
                case SINGLE -> {
                    Log.debugf("WebMatchParser :: Parse single matches for -> Tournament %s", tournament.getTournamentInfo().tournamentName());
                    DisciplineMatch singleMatch = parseSingleMatch(disciplineMatch);
                    SingleDiscipline  singleDiscipline = tournament.getSingleDiscipline();
                    singleDiscipline.addEliminationMatch(singleMatch);
                }
                case DOUBLE -> {
                    Log.debugf("WebMatchParser :: Parse double matches for -> Tournament %s", tournament.getTournamentInfo().tournamentName());
                    DisciplineMatch doubleMatch = parseDoubleMatch(disciplineMatch);
                    DoubleDiscipline doubleDiscipline = tournament.getDoubleDiscipline();
                    doubleDiscipline.addEliminationMatch(doubleMatch);
                }
                case MIXED -> {
                    Log.debugf("WebMatchParser :: Parse mixed matches for -> Tournament %s", tournament.getTournamentInfo().tournamentName());
                    DisciplineMatch mixedMatch = parseDoubleMatch(disciplineMatch);
                    MixedDiscipline mixedDiscipline = tournament.getMixedDiscipline();
                    mixedDiscipline.addEliminationMatch(mixedMatch);
                }
                default -> {
                    Log.errorf("WebMatchParser :: Unknown discipline type: %s", disciplineType.name());
                }
            }
        }
    }

    private DisciplineMatch parseSingleMatch(HtmlElement singleMatchElement) {
        DisciplineMatch singleMatch = new DisciplineMatch();
        HtmlElement matchRoundNameDiv = htmlStructureParser.getMatchRoundNameElement(singleMatchElement);
        var matchRoundName = matchRoundNameDiv != null ? matchRoundNameDiv.asNormalizedText() : "";
        singleMatch.setRoundName(matchRoundName);
        return singleMatch;
    }

    private DisciplineMatch parseDoubleMatch(HtmlElement doubleMatchElement) {
        DisciplineMatch doubleMatch = new DisciplineMatch();
        HtmlElement matchRoundNameDiv = htmlStructureParser.getMatchRoundNameElement(doubleMatchElement);
        var matchRoundName = matchRoundNameDiv != null ? matchRoundNameDiv.asNormalizedText() : "";
        doubleMatch.setRoundName(matchRoundName);
        return doubleMatch;
    }

    public DisciplineMatch parseCombinedMatches(Tournament tournament, HtmlElement moduleCardElement) {
        Log.debugf("WebMatchParser :: parse combined matches website -> Tournament %s", tournament.getTournamentInfo().tournamentName());
        return null;
    }

    private static record PlayerParserModel(
            String name,
            String id
    ){}


}
