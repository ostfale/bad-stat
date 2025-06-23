package de.ostfale.qk.domain.discipline;

import de.ostfale.qk.domain.match.Match;

import java.util.ArrayList;
import java.util.List;

public abstract class TournamentDiscipline implements Discipline {

    protected final List<Match> eliminationMatches = new ArrayList<>();
    protected final List<Match> groupMatches = new ArrayList<>();

    protected AgeClass disciplineAgeClass = AgeClass.UOX;
    protected DisciplineOrder disciplineOrder = DisciplineOrder.NO_ORDER;

    // provide discipline name (h5) and group name (for combined tournaments)
    protected String disciplineName;
    protected String groupName = "";

    @Override
    public DisciplineType getDisciplineType() {
        throw new UnsupportedOperationException("TournamentDiscipline does not support getDisciplineType()");
    }

    @Override
    public boolean hasTreeMatches() {
        return !eliminationMatches.isEmpty();
    }

    @Override
    public boolean hasGroupMatches() {
        return !groupMatches.isEmpty();
    }

    @Override
    public boolean hasMatches() {
        return hasTreeMatches() || hasGroupMatches();
    }

    @Override
    public DisciplineOrder getDisciplineOrder() {
        return disciplineOrder;
    }

    public List<Match> getEliminationMatches() {
        return eliminationMatches;
    }

    public List<Match> getGroupMatches() {
        return groupMatches;
    }

    public void addEliminationMatch(Match match) {
        eliminationMatches.add(match);
    }

    public void addGroupMatch(Match match) {
        groupMatches.add(match);
    }

    public void setDisciplineOrder(DisciplineOrder disciplineOrder) {
        this.disciplineOrder = disciplineOrder;
    }

    public AgeClass getDisciplineAgeClass() {
        return disciplineAgeClass;
    }

    public void setDisciplineAgeClass(AgeClass disciplineAgeClass) {
        this.disciplineAgeClass = disciplineAgeClass;
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public String getGroupName() {
        return groupName;
    }
}
