package de.ostfale.qk.domain.tournament;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.ostfale.qk.domain.discipline.*;
import io.quarkus.logging.Log;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Tournament {

    private final TournamentInfo tournamentInfo;
    private final List<TournamentDiscipline> disciplines;

    public Tournament(TournamentInfo tournamentInfo) {
        this.disciplines = List.of(new SingleDiscipline(), new DoubleDiscipline(), new MixedDiscipline());
        this.tournamentInfo = tournamentInfo;
    }

    public Tournament() {
        this.disciplines = List.of(new SingleDiscipline(), new DoubleDiscipline(), new MixedDiscipline());
        this.tournamentInfo = new TournamentInfo();
    }

    public TournamentInfo getTournamentInfo() {
        return tournamentInfo;
    }

    public List<TournamentDiscipline> getDisciplines() {
        return disciplines;
    }

    @JsonIgnore
    public List<TournamentDiscipline> getDisciplinesSortedByOrder() {
        Log.debugf("Tournament :: getDisciplinesSortedByOrder");
        return disciplines.stream().sorted(Comparator.comparing(Discipline::getDisciplineOrder)).toList();
    }

    @JsonIgnore
    public SingleDiscipline getSingleDiscipline() {
        return (SingleDiscipline) getDisciplineByType(DisciplineType.SINGLE);
    }

    @JsonIgnore
    public DoubleDiscipline getDoubleDiscipline() {
        return (DoubleDiscipline) getDisciplineByType(DisciplineType.DOUBLE);
    }

    @JsonIgnore
    public MixedDiscipline getMixedDiscipline() {
        return (MixedDiscipline) getDisciplineByType(DisciplineType.MIXED);
    }

    @JsonIgnore
    public Discipline getDisciplineByType(DisciplineType disciplineType) {
        return disciplines.stream()
                .filter(discipline -> discipline.getDisciplineType() == disciplineType)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("DisciplineType " + disciplineType + " is not supported."));
    }

    @JsonIgnore
    public DisciplineOrder getNextDisciplineOrder() {
        Log.debugf("Tournament :: getNextDisciplineOrder");

        DisciplineOrder currentHighestOrder = disciplines.stream()
                .map(Discipline::getDisciplineOrder)
                .max(Enum::compareTo)
                .orElse(DisciplineOrder.NO_ORDER);

        return ORDER_TRANSITIONS.getOrDefault(currentHighestOrder, DisciplineOrder.NO_ORDER);
    }


    private static final Map<DisciplineOrder, DisciplineOrder> ORDER_TRANSITIONS = Map.of(
            DisciplineOrder.NO_ORDER, DisciplineOrder.FIRST,
            DisciplineOrder.FIRST, DisciplineOrder.SECOND,
            DisciplineOrder.SECOND, DisciplineOrder.THIRD,
            DisciplineOrder.THIRD, DisciplineOrder.NO_ORDER
    );

}
