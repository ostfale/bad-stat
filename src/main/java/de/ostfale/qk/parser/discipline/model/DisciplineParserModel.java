package de.ostfale.qk.parser.discipline.model;

import de.ostfale.qk.domain.discipline.AgeClass;
import de.ostfale.qk.domain.discipline.Discipline;
import de.ostfale.qk.parser.match.internal.model.Match;

import java.util.ArrayList;
import java.util.List;

public class DisciplineParserModel {

    private final String disciplineName;
    private final Discipline discipline;
    private final AgeClass ageClass;
    private final List<Match> matches = new ArrayList<>();

    public DisciplineParserModel(String disciplineName, Discipline discipline, AgeClass ageClass) {
        this.disciplineName = disciplineName;
        this.discipline = discipline;
        this.ageClass = ageClass;
    }

    public DisciplineParserModel(String disciplineName, String ageClass) {
        this.disciplineName = disciplineName + " " + ageClass;
        this.discipline = Discipline.lookup(disciplineName);
        this.ageClass = AgeClass.fromString(ageClass);
    }

    public void addMatch(Match match) {
        this.matches.add(match);
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

    public List<Match> getMatches() {
        return matches;
    }
}
