package de.ostfale.qk.domain.discipline;

public class SingleDiscipline extends TournamentDiscipline {
    @Override
    public DisciplineType getDisciplineType() {
        disciplineType = DisciplineType.SINGLE;
        return disciplineType;
    }
}

