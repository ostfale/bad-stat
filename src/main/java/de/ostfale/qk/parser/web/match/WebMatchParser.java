package de.ostfale.qk.parser.web.match;

import de.ostfale.qk.domain.discipline.Discipline;
import de.ostfale.qk.domain.discipline.DisciplineOrder;
import de.ostfale.qk.domain.discipline.DisciplineType;
import de.ostfale.qk.domain.tournament.Tournament;
import de.ostfale.qk.parser.discipline.DisciplineAgeParserService;
import de.ostfale.qk.parser.web.HtmlStructureParser;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.htmlunit.html.HtmlElement;

import java.util.List;

@ApplicationScoped
public class WebMatchParser {

    @Inject
    HtmlStructureParser htmlStructureParser;
    @Inject
    DisciplineAgeParserService disciplineAgeParserService;

    public void parseEliminationMatches(Tournament tournament, List<HtmlElement> matchGroupElements) {
        Log.debugf("WebMatchParser :: parse elimination matches website -> Tournament %s", tournament.getTournamentInfo().tournamentName());

        for (Discipline discipline : tournament.getDisciplines()) {
            if (discipline.getDisciplineOrder() != DisciplineOrder.NO_ORDER) {
                int orderValue = discipline.getDisciplineOrder().ordinal() - 1;
                processDisciplineMatcher(tournament, discipline.getDisciplineType(), matchGroupElements.get(orderValue));
            }
        }
    }

    private void processDisciplineMatcher(Tournament tournament, DisciplineType disciplineType, HtmlElement disciplineMatchGroupElement) {
        Log.debugf("WebMatchParser :: process discipline matcher -> Tournament %s | DisciplineType %s", tournament.getTournamentInfo().tournamentName(), disciplineType.name());

        List<HtmlElement> disciplineMatches = htmlStructureParser.getAllMatchesForMatchGroupContainer(disciplineMatchGroupElement);
        for (HtmlElement disciplineMatch : disciplineMatches) {

        }


        switch (disciplineType) {
            case SINGLE -> {
                System.out.println("Single discipline");
            }
            case DOUBLE -> {
                System.out.println("Double discipline");
            }
            case MIXED -> {
                System.out.println("Mixed discipline");
            }
            default -> {
                Log.errorf("WebMatchParser :: Unknown discipline type: %s", disciplineType.name());
            }
        }
    }

    public void parseCombinedMatches(Tournament tournament, List<HtmlElement> matchGroupElements) {
        Log.debugf("WebMatchParser :: parse combined matches website -> Tournament %s", tournament.getTournamentInfo().tournamentName());

    }
}
