package de.ostfale.qk.domain.tourcal;

import java.util.List;
import java.util.function.Predicate;

public class PlannedTournaments {

    private final List<PlannedTournament> allPlannedTournaments;

    public PlannedTournaments(List<PlannedTournament> allPlannedTournaments) {
        this.allPlannedTournaments = allPlannedTournaments;
    }

    public void addPlannedTournament(PlannedTournament plannedTournament) {
        allPlannedTournaments.add(plannedTournament);
    }

    public void addAllPlannedTournaments(List<PlannedTournament> plannedTournaments) {
        this.allPlannedTournaments.addAll(plannedTournaments);
    }

    public List<PlannedTournament> getAllPlannedTournaments() {
        return allPlannedTournaments;
    }

    public List<PlannedTournament> getFilteredTournaments(List<Predicate<PlannedTournament>> filters) {
        return allPlannedTournaments.stream()
                .filter(tournament -> filters.stream()
                        .allMatch(filter -> filter.test(tournament)))
                .toList();
    }


}
