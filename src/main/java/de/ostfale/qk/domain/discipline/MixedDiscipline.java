package de.ostfale.qk.domain.discipline;

public class MixedDiscipline extends TournamentDiscipline {

    @Override
    public DisciplineType getDisciplineType() {
        disciplineType = DisciplineType.MIXED;
        return disciplineType;
    }
}
