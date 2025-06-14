package de.ostfale.qk.parser.discipline.model;

import de.ostfale.qk.domain.discipline.AgeClass;
import de.ostfale.qk.domain.discipline.DisciplineType;
import de.ostfale.qk.parser.match.internal.model.Match;

import java.util.ArrayList;
import java.util.List;

public class DisciplineParserModel {

    private final String disciplineName;
    private final DisciplineType disciplineType;
    private final AgeClass ageClass;
    private final List<Match> matches = new ArrayList<>();

    public DisciplineParserModel(DisciplineType disciplineType, AgeClass ageClass) {
        this.disciplineName = disciplineType.getDisplayString() + " " + ageClass.name();
        this.disciplineType = disciplineType;
        this.ageClass = ageClass;
    }

    public void addMatch(Match match) {
        this.matches.add(match);
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public DisciplineType getDiscipline() {
        return disciplineType;
    }

    public AgeClass getAgeClass() {
        return ageClass;
    }

    public List<Match> getMatches() {
        return matches;
    }
}
