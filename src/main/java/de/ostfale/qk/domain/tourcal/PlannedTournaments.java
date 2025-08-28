package de.ostfale.qk.domain.tourcal;

import de.ostfale.qk.app.TimeHandlerFacade;
import io.quarkus.logging.Log;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PlannedTournaments implements TimeHandlerFacade {

    private final List<PlannedTournament> allPlannedTournaments;

    Predicate<Integer> isThisYearsTournament = year -> year == getActualCalendarYear();
    Predicate<Integer> isNextYearsTournament = year -> year == getActualCalendarYear() + 1;

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

    public List<PlannedTournament> getAllTournamentsForThisYear() {
        return getTournamentsByYear(isThisYearsTournament);
    }

    public List<PlannedTournament> getAllNotYetFinishedTournaments(LocalDate checkDate) {
        var foundTournaments = allPlannedTournaments.stream()
                .filter(tournament -> parseDateToTournamentFormat(tournament.startDate()).isAfter(checkDate))
                .toList();
        Log.debugf("PlannedTournaments:: get all future tournaments : found %d ", foundTournaments.size());
        return foundTournaments;
    }

    public int getNumberOfAllPlannedTournaments() {
        var nofAllTournaments = allPlannedTournaments.size();
        Log.debugf("PlannedTournaments:: number of all tournament : found %d ", nofAllTournaments);
        return nofAllTournaments;
    }

    public List<PlannedTournament> getAllTournamentsForNextYear() {
        return getTournamentsByYear(isNextYearsTournament);
    }

    private List<PlannedTournament> getTournamentsByYear(Predicate<Integer> yearPredicate) {
        return allPlannedTournaments.stream()
                .filter(tournament -> yearPredicate.test(
                        parseDateToTournamentFormat(tournament.startDate()).getYear()))
                .collect(Collectors.toList());
    }

}
