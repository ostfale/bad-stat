package de.ostfale.qk.domain.discipline;

public class DoubleDiscipline extends TournamentDiscipline{

    @Override
    public DisciplineType getDisciplineType() {
        disciplineType= DisciplineType.DOUBLE;
        return disciplineType;
    }
}
