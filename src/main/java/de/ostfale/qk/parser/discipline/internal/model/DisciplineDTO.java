package de.ostfale.qk.parser.discipline.internal.model;

import de.ostfale.qk.parser.match.internal.model.Match;

import java.util.ArrayList;
import java.util.List;

public class DisciplineDTO {

    private final String disciplineName;
    private final Discipline discipline;
    private final AgeClass ageClass;
    private final List<Match> treeMatches = new ArrayList<>();
    private final List<Match> groupMatches = new ArrayList<>();

    public DisciplineDTO(String disciplineName, Discipline discipline, AgeClass ageClass) {
        this.disciplineName = disciplineName;
        this.discipline = discipline;
        this.ageClass = ageClass;
    }

    public DisciplineDTO( String discipline, String ageClass) {
        this.disciplineName = discipline;
        this.discipline = Discipline.fromString(discipline);
        this.ageClass = AgeClass.fromString(ageClass);
    }

    public void addTreeMatch(Match match) {
        this.treeMatches.add(match);
    }

    public void addGroupMatch(Match match) {
        this.groupMatches.add(match);
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public AgeClass getAgeClass() {
        return ageClass;
    }

    public List<Match> getTreeMatches() {
        return treeMatches;
    }

    public List<Match> getGroupMatches() {
        return groupMatches;
    }
}
