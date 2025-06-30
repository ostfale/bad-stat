package de.ostfale.qk.domain.discipline;

public class SingleDiscipline extends TournamentDiscipline {
    @Override
    public DisciplineType getDisciplineType() {
        return DisciplineType.SINGLE;
    }
}

