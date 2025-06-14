package de.ostfale.qk.domain.tournament;

import de.ostfale.qk.domain.discipline.*;

import java.util.List;

public class Tournament {

    private final TournamentInfo tournamentInfo;
    private final List<Discipline> disciplines;

    public Tournament(TournamentInfo tournamentInfo) {
        this.disciplines = List.of(new SingleDiscipline(), new DoubleDiscipline(), new MixedDiscipline());
        this.tournamentInfo = tournamentInfo;
    }

    public TournamentInfo getTournamentInfo() {
        return tournamentInfo;
    }

    public List<Discipline> getDisciplines() {
        return disciplines;
    }

    public Discipline getDisciplineByType(DisciplineType disciplineType) {
        return disciplines.stream()
                .filter(discipline -> discipline.getDisciplineType() == disciplineType)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("DisciplineType " + disciplineType + " is not supported."));
    }
}
