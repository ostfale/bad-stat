package de.ostfale.qk.domain.tournament;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.ostfale.qk.domain.discipline.Discipline;
import de.ostfale.qk.domain.discipline.DisciplineType;

import java.util.ArrayList;
import java.util.List;

public class Tournament {

    private final TournamentInfo tournamentInfo;
    private final List<Discipline> disciplines = new ArrayList<>();

    public Tournament(TournamentInfo tournamentInfo) {
        this.tournamentInfo = tournamentInfo;
    }

    public Tournament() {
        this.tournamentInfo = new TournamentInfo();
    }

    public TournamentInfo getTournamentInfo() {
        return tournamentInfo;
    }

    public List<Discipline> getDisciplines() {
        return disciplines;
    }

    @JsonIgnore
    public Discipline getSingleDiscipline() {
        return getDisciplineByType(DisciplineType.SINGLE);
    }

    @JsonIgnore
    public Discipline getDoubleDiscipline() {
        return getDisciplineByType(DisciplineType.DOUBLE);
    }

    @JsonIgnore
    public Discipline getMixedDiscipline() {
        return getDisciplineByType(DisciplineType.MIXED);
    }

    @JsonIgnore
    public Discipline getDisciplineByType(DisciplineType disciplineType) {
        return disciplines.stream()
                .filter(discipline -> discipline.getDisciplineType() == disciplineType)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("DisciplineType " + disciplineType + " is not supported."));
    }
}
