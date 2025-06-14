package de.ostfale.qk.domain.discipline;

import de.ostfale.qk.domain.match.Match;

import java.util.ArrayList;
import java.util.List;

public abstract class TournamentDiscipline implements Discipline {

    protected final List<Match> treeMatches = new ArrayList<>();
    protected final List<Match> groupMatches = new ArrayList<>();

    protected AgeClass disciplineAgeClass = AgeClass.UOX;

    @Override
    public DisciplineType getDisciplineType() {
        throw new UnsupportedOperationException("TournamentDiscipline does not support getDisciplineType()");
    }

    @Override
    public boolean hasTreeMatches() {
        return !treeMatches.isEmpty();
    }

    @Override
    public boolean hasGroupMatches() {
        return !groupMatches.isEmpty();
    }

    @Override
    public boolean hasMatches() {
        return hasTreeMatches() || hasGroupMatches();
    }

    public AgeClass getDisciplineAgeClass() {
        return disciplineAgeClass;
    }

    public void setDisciplineAgeClass(AgeClass disciplineAgeClass) {
        this.disciplineAgeClass = disciplineAgeClass;
    }
}
