package de.ostfale.qk.domain.discipline;

public class MixedDiscipline extends TournamentDiscipline{

    @Override
    public DisciplineType getDisciplineType() {
        return DisciplineType.MIXED;
    }
}
