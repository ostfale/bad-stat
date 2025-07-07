package de.ostfale.qk.domain.discipline;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.ostfale.qk.domain.match.DisciplineMatch;

import java.util.ArrayList;
import java.util.List;

public class TournamentDiscipline implements Discipline {

    private DisciplineInfo disciplineInfo;

    private final List<DisciplineMatch> eliminationMatches = new ArrayList<>();
    private final List<DisciplineMatch> groupMatches = new ArrayList<>();

    // provide discipline name (h5) and group name (for combined tournaments)
    private String disciplineName ="";
    private String groupName = "";

    public TournamentDiscipline() {
    }

    @Override
    public DisciplineInfo getDisciplineInfo() {
        return disciplineInfo;
    }

    @JsonIgnore
    @Override
    public DisciplineType getDisciplineType() {
        return disciplineInfo.disciplineType() != null ? disciplineInfo.disciplineType() : null;
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

    public void setDisciplineInfo(DisciplineInfo disciplineInfo) {
        this.disciplineInfo = disciplineInfo;
    }

    @JsonIgnore
    public AgeClass getDisciplineAgeClass() {
        return disciplineInfo.ageClass();
    }

    public void setDisciplineName(String disciplineName) {
        this.disciplineName = disciplineName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public String getGroupName() {
        return groupName;
    }
}
