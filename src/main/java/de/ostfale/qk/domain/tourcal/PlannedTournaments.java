package de.ostfale.qk.domain.tourcal;

import de.ostfale.qk.app.TimeHandlerFacade;
import io.quarkus.logging.Log;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlannedTournaments implements TimeHandlerFacade {

    private List<PlannedTournament> thisYearsTournaments = new ArrayList<>();
    private List<PlannedTournament> nextYearsTournaments = new ArrayList<>();

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
        return Stream.concat(thisYearsTournaments.stream(), nextYearsTournaments.stream())
                .collect(Collectors.toList());
    }

    public void setNextYearsTournaments(List<PlannedTournament> nextYearsTournaments) {
        this.nextYearsTournaments = nextYearsTournaments;
    }
}
