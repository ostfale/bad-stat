package de.ostfale.qk.domain.discipline;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.ostfale.qk.domain.match.DisciplineMatch;

import java.util.ArrayList;
import java.util.List;

public class TournamentDiscipline implements Discipline {

    protected final List<DisciplineMatch> eliminationMatches = new ArrayList<>();
    protected final List<DisciplineMatch> groupMatches = new ArrayList<>();

    protected AgeClass disciplineAgeClass = AgeClass.UOX;
    protected DisciplineOrder disciplineOrder = DisciplineOrder.NO_ORDER;

    // provide discipline name (h5) and group name (for combined tournaments)
    protected String disciplineName;
    protected String groupName = "";
    protected DisciplineType disciplineType;

    public TournamentDiscipline() {
    }

    @Override
    public DisciplineType getDisciplineType() {
        return disciplineType;
    }

    @JsonIgnore
    @Override
    public boolean hasEliminationMatches() {
        return !eliminationMatches.isEmpty();
    }

    @JsonIgnore
    @Override
    public boolean hasGroupMatches() {
        return !groupMatches.isEmpty();
    }

    @JsonIgnore
    @Override
    public boolean hasMatches() {
        return hasEliminationMatches() || hasGroupMatches();
    }

    @JsonIgnore
    @Override
    public DisciplineOrder getDisciplineOrder() {
        return disciplineOrder;
    }

    @Override
    public List<DisciplineMatch> getEliminationMatches() {
        return eliminationMatches;
    }

    @Override
    public List<DisciplineMatch> getGroupMatches() {
        return groupMatches;
    }

    @JsonIgnore
    public void addEliminationMatch(DisciplineMatch match) {
        eliminationMatches.add(match);
    }

    @JsonIgnore
    public void addGroupMatch(DisciplineMatch match) {
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
