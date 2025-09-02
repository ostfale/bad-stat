package de.ostfale.qk.domain.tourcal;

import de.ostfale.qk.app.TimeHandlerFacade;
import io.quarkus.logging.Log;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlannedTournaments implements TimeHandlerFacade {

    private List<PlannedTournament> thisYearsTournaments = new ArrayList<>();
    private List<PlannedTournament> nextYearsTournaments = new ArrayList<>();
    private final List<PlannedTournament> allPlannedTournaments = new ArrayList<>();

    public PlannedTournaments(List<PlannedTournament> thisYearsTournaments) {
        this.thisYearsTournaments = thisYearsTournaments;
    }

    public PlannedTournaments() {
    }

    public List<PlannedTournament> getThisYearsTournaments() {
        return thisYearsTournaments;
    }

    public List<PlannedTournament> getAllRemainingTournaments(LocalDate checkDate) {
        var foundTournaments = thisYearsTournaments.stream()
                .filter(tournament -> isTournamentAfterDate(tournament, checkDate))
                .toList();
        Log.debugf("PlannedTournaments:: get all future tournaments : found %d ", foundTournaments.size());
        return foundTournaments;
    }

    private boolean isTournamentAfterDate(PlannedTournament tournament, LocalDate checkDate) {
        var tournamentStartDate = parseDateToTournamentFormat(tournament.startDate());
        return tournamentStartDate.isAfter(checkDate);
    }


    public List<PlannedTournament> getNextYearsTournaments() {
        return nextYearsTournaments;
    }

    public List<PlannedTournament> getAllPlannedTournaments() {
        if (allPlannedTournaments.isEmpty()) {
            allPlannedTournaments.addAll(thisYearsTournaments);
            allPlannedTournaments.addAll(nextYearsTournaments);
        }
        return allPlannedTournaments;
    }

    public void addPlannedTournament(PlannedTournament plannedTournament) {
        allPlannedTournaments.add(plannedTournament);
    }

    public void addAllPlannedTournaments(List<PlannedTournament> plannedTournaments) {
        this.allPlannedTournaments.addAll(plannedTournaments);
    }

    public void setNextYearsTournaments(List<PlannedTournament> nextYearsTournaments) {
        this.nextYearsTournaments = nextYearsTournaments;
    }

    public void setThisYearsTournaments(List<PlannedTournament> thisYearsTournaments) {
        this.thisYearsTournaments = thisYearsTournaments;
    }
}
