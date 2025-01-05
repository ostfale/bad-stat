package de.ostfale.qk.parser.discipline.internal.model;

import de.ostfale.qk.parser.match.internal.model.Match;

import java.util.ArrayList;
import java.util.List;

public class DisciplineDTO {

    private final Discipline discipline;
    private final List<Match> treeMatches = new ArrayList<>();
    private final List<Match> groupMatches = new ArrayList<>();

    public DisciplineDTO(Discipline discipline) {
        this.discipline = discipline;
    }

    public void addTreeMatch(Match match) {
        this.treeMatches.add(match);
    }

    public void addGroupMatch(Match match) {
        this.groupMatches.add(match);
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public List<Match> getTreeMatches() {
        return treeMatches;
    }

    public List<Match> getGroupMatches() {
        return groupMatches;
    }
}
